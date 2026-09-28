package com.gableo.coupletour.repository;

import com.gableo.coupletour.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
