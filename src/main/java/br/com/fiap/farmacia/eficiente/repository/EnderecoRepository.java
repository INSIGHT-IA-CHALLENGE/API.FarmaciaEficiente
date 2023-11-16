package br.com.fiap.farmacia.eficiente.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.farmacia.eficiente.models.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    
}
