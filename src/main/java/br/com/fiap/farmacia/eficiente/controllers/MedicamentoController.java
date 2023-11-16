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
import br.com.fiap.farmacia.eficiente.models.Medicamento;
import br.com.fiap.farmacia.eficiente.models.Status;
import br.com.fiap.farmacia.eficiente.models.TipoUsuario;
import br.com.fiap.farmacia.eficiente.models.Usuario;
import br.com.fiap.farmacia.eficiente.repository.MedicamentoRepository;
import br.com.fiap.farmacia.eficiente.repository.UsuarioRepository;
import br.com.fiap.farmacia.eficiente.services.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/medicamento")
public class MedicamentoController {
    
    AuthService authService = new AuthService();

    @Autowired
    MedicamentoRepository repository;

    @Autowired
    UsuarioRepository usuarioRepository;


    @PostMapping("/cadastrar")
    public ResponseEntity<EntityModel<Medicamento>> cadastrar(@RequestBody @Valid Medicamento medicamento) {

        Usuario usuarioLogado = authService.getUsuarioLogado(usuarioRepository);
        if(usuarioLogado.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new RestNotAuthorizedException("O usuário logado não tem permissão para atualizar os postos");
        }

        repository.save(medicamento);

        return ResponseEntity
            .created(medicamento.toEntityModel().getRequiredLink("self").toUri())
            .body(medicamento.toEntityModel());
    }

    @PutMapping("/atualizar")
    public EntityModel<Medicamento> update(@RequestBody @Valid Medicamento medicamento) {

        Usuario usuarioLogado = authService.getUsuarioLogado(usuarioRepository);
        if(usuarioLogado.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new RestNotAuthorizedException("O usuário logado não tem permissão para atualizar os postos");
        }

        getMedicamento(medicamento.getId());
        repository.save(medicamento);

        return medicamento.toEntityModel();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Medicamento> destroy(@PathVariable Integer id) {
        Usuario usuarioLogado = authService.getUsuarioLogado(usuarioRepository);
        if(usuarioLogado.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new RestNotAuthorizedException("O usuário logado não tem permissão para atualizar os postos");
        }

        Medicamento medicamento = getMedicamento(id);
        medicamento.setStatus(Status.INATIVO);
        repository.save(medicamento);

        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<Medicamento>> listAll(){
        return ResponseEntity.ok(repository.findByStatus(Status.ATIVO));
    }

    @GetMapping("{id}")
    public EntityModel<Medicamento> show(@PathVariable Integer id){
        Medicamento medicamento = getMedicamento(id);
        return medicamento.toEntityModel();
    }

    private Medicamento getMedicamento(Integer id){
        if(id == null || id <= 0){
            throw new RestNotFoundException("Medicamento não encontrado");
        }

        return repository
            .findById(id)
            .filter(medicamento -> medicamento.getStatus() == Status.ATIVO)
            .orElseThrow(() -> new RestNotFoundException("Medicamento não encontrado"));
    }
}
