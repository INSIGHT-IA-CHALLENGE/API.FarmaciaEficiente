package br.com.fiap.farmacia.eficiente.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.farmacia.eficiente.models.Estoque;
import br.com.fiap.farmacia.eficiente.models.Status;

public interface EstoqueRepository extends JpaRepository<Estoque, Integer>{
    
    List<Estoque> findByPostoIdAndStatus(Integer idPosto, Status status);
}
