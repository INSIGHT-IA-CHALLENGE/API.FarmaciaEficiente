package br.com.fiap.farmacia.eficiente.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import br.com.fiap.farmacia.eficiente.models.Estoque;
import br.com.fiap.farmacia.eficiente.models.Status;
import br.com.fiap.farmacia.eficiente.models.TipoUsuario;
import br.com.fiap.farmacia.eficiente.models.Usuario;
import br.com.fiap.farmacia.eficiente.repository.EstoqueRepository;
import br.com.fiap.farmacia.eficiente.repository.UsuarioRepository;
import br.com.fiap.farmacia.eficiente.services.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    AuthService authService = new AuthService();

    @Autowired
    EstoqueRepository repository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping("/cadastrar")
    public ResponseEntity<Estoque> cadastrar(@RequestBody @Valid Estoque estoque) {

        Usuario usuarioLogado = authService.getUsuarioLogado(usuarioRepository);
        if (usuarioLogado.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new RestNotAuthorizedException("O usuário logado não tem permissão para atualizar os postos");
        }

        try {
            return ResponseEntity.status(201).body(repository.save(estoque));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Estoque> update(@RequestBody @Valid Estoque estoque) {
        Usuario usuarioLogado = authService.getUsuarioLogado(usuarioRepository);
        if (usuarioLogado.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new RestNotAuthorizedException("O usuário logado não tem permissão para atualizar os postos");
        }

        Estoque refEstoque = getEstoque(estoque.getId());
        estoque.setPosto(refEstoque.getPosto());
        estoque.setMedicamento(refEstoque.getMedicamento());

        return ResponseEntity.ok(repository.save(estoque));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Estoque> destroy(@PathVariable Integer id) {
        Usuario usuarioLogado = authService.getUsuarioLogado(usuarioRepository);
        if (usuarioLogado.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new RestNotAuthorizedException("O usuário logado não tem permissão para atualizar os postos");
        }

        Estoque estoque = getEstoque(id);
        estoque.setStatus(Status.INATIVO);
        repository.save(estoque);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("{idPosto}")
    public ResponseEntity<List<Estoque>> listByPosto(@PathVariable Integer idPosto) {
        return ResponseEntity.ok(repository.findByPostoIdAndStatus(idPosto, Status.ATIVO));
    }

    private Estoque getEstoque(Integer id){
        if(id == null || id <= 0) {
            throw new RestNotFoundException("O estoque não foi encontrado");
        }

        return repository
            .findById(id)
            .filter(estoque -> estoque.getStatus().equals(Status.ATIVO))
            .orElseThrow(() -> new RestNotFoundException("O estoque não foi encontrado"));
    }
}
