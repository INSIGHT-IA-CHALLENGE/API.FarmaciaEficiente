package br.com.fiap.farmacia.eficiente.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.fiap.farmacia.eficiente.exception.RestNotAuthorizedException;
import br.com.fiap.farmacia.eficiente.models.Status;
import br.com.fiap.farmacia.eficiente.models.Usuario;
import br.com.fiap.farmacia.eficiente.repository.UsuarioRepository;

public class AuthService {

    public Usuario getUsuarioLogado(UsuarioRepository usuarioRepository) {
        String email = "";

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            email = authentication.getName();
        } catch (JWTVerificationException e) {
            throw new RestNotAuthorizedException("Token inválido");
        }

        return usuarioRepository
                .findByEmail(email)
                .filter(usuario -> usuario.getStatus().equals(Status.ATIVO))
                .orElseThrow(() -> new RestNotAuthorizedException("Usuario não encontrada"));
    }
}
