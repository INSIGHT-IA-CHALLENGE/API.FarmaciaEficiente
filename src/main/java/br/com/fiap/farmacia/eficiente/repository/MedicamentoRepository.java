package br.com.fiap.farmacia.eficiente.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.farmacia.eficiente.models.Medicamento;
import br.com.fiap.farmacia.eficiente.models.Status;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer>{
    
    List<Medicamento> findByStatus(Status status);
}
