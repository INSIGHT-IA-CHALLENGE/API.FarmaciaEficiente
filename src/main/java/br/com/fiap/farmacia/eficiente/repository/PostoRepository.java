package br.com.fiap.farmacia.eficiente.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.farmacia.eficiente.models.Posto;
import br.com.fiap.farmacia.eficiente.models.Status;

public interface PostoRepository extends JpaRepository<Posto, Integer>{
    List<Posto> findByStatus(Status status);
}
