package br.com.fiap.farmacia.eficiente.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.farmacia.eficiente.exception.RestNotAuthorizedException;
import br.com.fiap.farmacia.eficiente.exception.RestNotFoundException;
import br.com.fiap.farmacia.eficiente.models.Posto;
import br.com.fiap.farmacia.eficiente.models.Status;
import br.com.fiap.farmacia.eficiente.models.TipoUsuario;
import br.com.fiap.farmacia.eficiente.models.Usuario;
import br.com.fiap.farmacia.eficiente.repository.EnderecoRepository;
import br.com.fiap.farmacia.eficiente.repository.PostoRepository;
import br.com.fiap.farmacia.eficiente.repository.UsuarioRepository;
import br.com.fiap.farmacia.eficiente.services.AuthService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/posto")
public class PostoController {
    
    AuthService authService = new AuthService();

    @Autowired
    PostoRepository repository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EnderecoRepository enderecoRepository;


    @PostMapping("/cadastrar")
    public ResponseEntity<EntityModel<Posto>> cadastrar(@RequestBody @Valid Posto posto) {

        Usuario usuarioLogado = authService.getUsuarioLogado(usuarioRepository);
        if(usuarioLogado.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new RestNotAuthorizedException("O usuário logado não tem permissão para cadastrar postos");
        }

        enderecoRepository.save(posto.getEndereco());
        repository.save(posto);

        return ResponseEntity
            .created(posto.toEntityModel().getRequiredLink("self").toUri())
            .body(posto.toEntityModel());
    }

    @PutMapping("/atualizar")
    public EntityModel<Posto> update(@RequestBody @Valid Posto posto){
        Usuario usuarioLogado = authService.getUsuarioLogado(usuarioRepository);
        if(usuarioLogado.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new RestNotAuthorizedException("O usuário logado não tem permissão para atualizar os postos");
        }

        enderecoRepository.save(posto.getEndereco());
        repository.save(posto);

        return posto.toEntityModel();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Posto> destroy(@PathVariable Integer id){
        Usuario usuarioLogado = authService.getUsuarioLogado(usuarioRepository);
        if(usuarioLogado.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new RestNotAuthorizedException("O usuário logado não tem permissão para atualizar os postos");
        }

        Posto posto = getPosto(id);
        posto.setStatus(Status.INATIVO);
        repository.save(posto);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public EntityModel<Posto> show(@PathVariable Integer id) {
        Posto posto = getPosto(id);
        return posto.toEntityModel();
    }

    @GetMapping
    public ResponseEntity<List<Posto>> listAll() {
        return ResponseEntity.ok(repository.findByStatus(Status.ATIVO));
    }
    

    private Posto getPosto(Integer id){
        return repository
            .findById(id)
            .filter(posto -> posto.getStatus().equals(Status.ATIVO))
            .orElseThrow(() -> new RestNotFoundException("Posto não encontrado"));
    }
}
