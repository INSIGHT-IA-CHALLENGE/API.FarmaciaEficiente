package br.com.fiap.farmacia.eficiente.models;

public record Token(
    String token,
    String type,
    String prefix
) {}
