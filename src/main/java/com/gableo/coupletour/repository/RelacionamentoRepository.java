package com.gableo.coupletour.repository;

import com.gableo.coupletour.model.Relacionamento;
import com.gableo.coupletour.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RelacionamentoRepository extends JpaRepository<Relacionamento, Long> {

    @Query("SELECT r FROM Relacionamento r WHERE (r.usuarioA = :user OR r.usuarioB = :user) AND r.relacionamentoAtivo = true")
    Optional<Relacionamento> findAtivoByUser(@Param("user") Usuario user);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Relacionamento r WHERE (r.usuarioA = :user OR r.usuarioB = :user) AND r.relacionamentoAtivo = true")
    boolean existsAtivoByUser(@Param("user") Usuario user);
}
