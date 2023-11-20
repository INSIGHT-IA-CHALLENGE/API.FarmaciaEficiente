package br.com.fiap.farmacia.eficiente.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.farmacia.eficiente.exception.RestNotAuthorizedException;
import br.com.fiap.farmacia.eficiente.exception.RestNotFoundException;
import br.com.fiap.farmacia.eficiente.exception.RestValueInvalidException;
import br.com.fiap.farmacia.eficiente.models.Estoque;
import br.com.fiap.farmacia.eficiente.models.Retirada;
import br.com.fiap.farmacia.eficiente.models.Usuario;
import br.com.fiap.farmacia.eficiente.repository.EstoqueRepository;
import br.com.fiap.farmacia.eficiente.repository.RetiradaRepository;
import br.com.fiap.farmacia.eficiente.repository.UsuarioRepository;
import br.com.fiap.farmacia.eficiente.services.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/retirada")
public class RetiradaController {
    
    AuthService authService = new AuthService();

    @Autowired
    RetiradaRepository repository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EstoqueRepository estoqueRepository;


    @PostMapping("/cadastrar")
    public ResponseEntity<Retirada> cadastrar(@RequestBody @Valid Retirada retirada) {

        Usuario usuarioLogado = authService.getUsuarioLogado(usuarioRepository);
        if (usuarioLogado.getId() != retirada.getUsuario().getId()) {
            throw new RestNotAuthorizedException("Usuario logado é diferente do usuario da retirada");
        }

        Estoque estoque = estoqueRepository
                            .findById(retirada.getEstoque().getId())
                            .orElseThrow(() -> new RestNotFoundException("Estoque não encontrado"));

        if(estoque.getQuantidade() < 1) {
            throw new RestValueInvalidException("Medicamento fora de estoque");
        }

        estoque.setQuantidade(estoque.getQuantidade() - 1);
        estoqueRepository.save(estoque);

        try {
            return ResponseEntity.status(201).body(repository.save(retirada));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("{idUsuario}")
    public ResponseEntity<List<Retirada>> getRetirada(@PathVariable Integer idUsuario) {
        Usuario usuarioLogado = authService.getUsuarioLogado(usuarioRepository);
        if (usuarioLogado.getId() != idUsuario) {
            throw new RestNotAuthorizedException("Usuario logado é diferente do usuario da retirada");
        }

        return ResponseEntity.ok(repository.findByUsuarioIdOrderByIdDesc(idUsuario));
    }
}
