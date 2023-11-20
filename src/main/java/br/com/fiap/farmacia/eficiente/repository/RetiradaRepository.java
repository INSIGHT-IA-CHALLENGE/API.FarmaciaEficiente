package br.com.fiap.farmacia.eficiente.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.farmacia.eficiente.models.Retirada;

public interface RetiradaRepository extends JpaRepository<Retirada, Integer> {
    
    List<Retirada> findByUsuarioIdOrderByIdDesc(Integer idUsuario);
}
