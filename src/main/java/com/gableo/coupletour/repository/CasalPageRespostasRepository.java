package com.gableo.coupletour.repository;

import com.gableo.coupletour.model.CasalPageRespostas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface CasalPageRespostasRepository extends JpaRepository<CasalPageRespostas, Long> {
    Optional<CasalPageRespostas> findByRelacionamentoIdAndUsuarioId(Long relacionamentoId, Long usuarioId);
    List<CasalPageRespostas> findByRelacionamentoId(Long relacionamentoId);
}
