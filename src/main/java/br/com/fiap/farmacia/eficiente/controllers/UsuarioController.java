package br.com.fiap.farmacia.eficiente.controllers;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.farmacia.eficiente.exception.RestConflictException;
import br.com.fiap.farmacia.eficiente.models.Credencial;
import br.com.fiap.farmacia.eficiente.models.Status;
import br.com.fiap.farmacia.eficiente.models.Token;
import br.com.fiap.farmacia.eficiente.models.Usuario;
import br.com.fiap.farmacia.eficiente.repository.EnderecoRepository;
import br.com.fiap.farmacia.eficiente.repository.UsuarioRepository;
import br.com.fiap.farmacia.eficiente.security.TokenService;
import br.com.fiap.farmacia.eficiente.services.AuthService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    Logger log = LoggerFactory.getLogger(UsuarioController.class);
    AuthService authService = new AuthService();

    @Autowired
    UsuarioRepository repository;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    TokenService tokenService;

    @PostMapping("/cadastrar")
    public ResponseEntity<EntityModel<Usuario>> create(@RequestBody @Valid Usuario usuario) {
        try {
            log.info("Cadastrando usuario" + usuario);

            enderecoRepository.save(usuario.getEndereco());

            usuario.setSenha(encoder.encode(usuario.getSenha()));
            repository.save(usuario);

            return ResponseEntity
                    .created(usuario.toEntityModel().getRequiredLink("self").toUri())
                    .body(usuario.toEntityModel());

        } catch (DataIntegrityViolationException e) {
            throw new RestConflictException("Já existe um usuario com este email ou telefone");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody @Valid Credencial credencial) {
        try {
            if (repository.findByEmail(credencial.email()).isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            manager.authenticate(credencial.toAuthentication());

            var token = tokenService.generateToken(credencial);

            return ResponseEntity.ok(token);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public EntityModel<Usuario> show() {

        return authService.getUsuarioLogado(repository).toEntityModel();
    }

    @DeleteMapping
    public ResponseEntity<Usuario> destroy() {
        Usuario usuario = authService.getUsuarioLogado(repository);

        usuario.setStatus(Status.INATIVO);
        repository.save(usuario);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public EntityModel<Usuario> update(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioLogado = authService.getUsuarioLogado(repository);

            usuario.setId(usuarioLogado.getId());
            usuario.setEmail(usuarioLogado.getEmail());
            usuario.setTipoUsuario(usuarioLogado.getTipoUsuario());

            if (usuario.getSenha().isEmpty())
                usuario.setSenha(usuarioLogado.getSenha());
            else
                usuario.setSenha(encoder.encode(usuario.getSenha()));

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }

            enderecoRepository.save(usuario.getEndereco());
            repository.save(usuario);
            return usuario.toEntityModel();

        } catch (DataIntegrityViolationException e) {
            throw new RestConflictException("Já existe um usuario com este email");
        }

    }

}
