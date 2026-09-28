package com.gableo.coupletour.repository;

import com.gableo.coupletour.model.Relacionamento;
import com.gableo.coupletour.model.Seguidor;
import com.gableo.coupletour.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.gableo.coupletour.dto.CasalSeguidoProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SeguidorRepository extends JpaRepository<Seguidor, Long> {
    Optional<Seguidor> findByRelacionamentoAndSeguidor(Relacionamento relacionamento, Usuario seguidor);
    boolean existsByRelacionamentoAndSeguidor(Relacionamento relacionamento, Usuario seguidor);
    int countByRelacionamento(Relacionamento relacionamento);

    @Query(value = """
        SELECT 
            ua.public_id AS publicIdUrl,
            CONCAT(ua.nome, ' & ', ub.nome) AS nomeCasal,
            COALESCE(cpr_a.foto_perfil_url, cpr_b.foto_perfil_url) AS fotoCasal
        FROM stage.seguidores s
        JOIN stage.relacionamentos r ON s.relacionamento_id = r.id
        JOIN stage.usuarios ua ON r.usuario_a_id = ua.id
        JOIN stage.usuarios ub ON r.usuario_b_id = ub.id
        LEFT JOIN stage.casal_page_respostas cpr_a ON cpr_a.relacionamento_id = r.id AND cpr_a.usuario_id = ua.id
        LEFT JOIN stage.casal_page_respostas cpr_b ON cpr_b.relacionamento_id = r.id AND cpr_b.usuario_id = ub.id
        WHERE s.seguidor_id IN (:idA, :idB)
          AND r.relacionamento_ativo = true
        GROUP BY r.id
    """, nativeQuery = true)
    List<CasalSeguidoProjection> findCasaisSeguidos(@Param("idA") Long idA, @Param("idB") Long idB);
}
