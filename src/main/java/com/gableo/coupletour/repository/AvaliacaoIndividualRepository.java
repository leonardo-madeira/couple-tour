package com.gableo.coupletour.repository;

import com.gableo.coupletour.model.AvaliacaoIndividual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoIndividualRepository extends JpaRepository<AvaliacaoIndividual, Long> {
    boolean existsByRelacionamentoIdAndUsuarioIdAndLugarId(Long relacionamentoId, Long usuarioId, Long lugarId);
}
