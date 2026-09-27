package com.gableo.coupletour.repository;

import com.gableo.coupletour.model.Lugar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LugarRepository extends JpaRepository<Lugar, Long> {

    @Query("SELECT l FROM Lugar l WHERE LOWER(l.nome) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(l.categoria.nome) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Lugar> buscarPorNomeOuCategoria(@Param("query") String query);

    @Query("SELECT l FROM Lugar l JOIN FETCH l.categoria")
    List<Lugar> findAllWithCategoria();
}
