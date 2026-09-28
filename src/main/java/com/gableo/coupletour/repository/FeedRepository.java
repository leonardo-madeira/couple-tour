package com.gableo.coupletour.repository;

import com.gableo.coupletour.dto.FeedProjection;
import com.gableo.coupletour.model.Relacionamento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import java.util.List;

public interface FeedRepository extends Repository<Relacionamento, Long> {

    @Query(value = """
        SELECT 
            r.id AS relacionamentoId,
            l.id AS lugarId,
            l.nome AS lugarNome,
            c.nome AS categoriaNome,
            CASE 
                WHEN ai_a.nota_media IS NOT NULL AND ai_b.nota_media IS NOT NULL 
                     THEN ROUND((ai_a.nota_media + ai_b.nota_media) / 2, 2)
                WHEN ai_a.nota_media IS NOT NULL THEN ai_a.nota_media
                ELSE ai_b.nota_media
            END AS notaMediaCasal,
            ua.public_id AS usuarioAPublicId,
            ua.nome AS usuarioANome,
            cpr_a.foto_perfil_url AS usuarioAFotoUrl,
            ai_a.nota_media AS notaMediaA,
            ai_a.nota_pergunta_1 AS aNota1,
            ai_a.nota_pergunta_2 AS aNota2,
            ai_a.nota_pergunta_3 AS aNota3,
            ai_a.nota_pergunta_4 AS aNota4,
            ai_a.descricao AS descA,
            ub.public_id AS usuarioBPublicId,
            ub.nome AS usuarioBNome,
            cpr_b.foto_perfil_url AS usuarioBFotoUrl,
            ai_b.nota_media AS notaMediaB,
            ai_b.nota_pergunta_1 AS bNota1,
            ai_b.nota_pergunta_2 AS bNota2,
            ai_b.nota_pergunta_3 AS bNota3,
            ai_b.nota_pergunta_4 AS bNota4,
            ai_b.descricao AS descB,
            (SELECT GROUP_CONCAT(f.url SEPARATOR ',') 
             FROM stage.fotos_avaliacao_individual f 
             WHERE f.avaliacao_id IN (ai_a.id, ai_b.id)) AS fotosUrls,
            GREATEST(COALESCE(ai_a.created_at, '1970-01-01'), COALESCE(ai_b.created_at, '1970-01-01')) - INTERVAL 3 HOUR AS dataPostagem
        FROM stage.relacionamentos r
        JOIN stage.usuarios ua ON r.usuario_a_id = ua.id
        JOIN stage.usuarios ub ON r.usuario_b_id = ub.id
        JOIN stage.lugares l 
        JOIN stage.categorias c ON l.categoria_id = c.id
        LEFT JOIN stage.avaliacao_individual ai_a 
            ON ai_a.relacionamento_id = r.id AND ai_a.lugar_id = l.id AND ai_a.usuario_id = ua.id
        LEFT JOIN stage.avaliacao_individual ai_b 
            ON ai_b.relacionamento_id = r.id AND ai_b.lugar_id = l.id AND ai_b.usuario_id = ub.id
        LEFT JOIN stage.casal_page_respostas cpr_a 
            ON cpr_a.relacionamento_id = r.id AND cpr_a.usuario_id = ua.id
        LEFT JOIN stage.casal_page_respostas cpr_b 
            ON cpr_b.relacionamento_id = r.id AND cpr_b.usuario_id = ub.id
        WHERE r.relacionamento_ativo = true 
          AND c.id = :categoriaId
          AND (ai_a.id IS NOT NULL OR ai_b.id IS NOT NULL) 
          AND (COALESCE(ai_a.post_visibility, true) = true AND COALESCE(ai_b.post_visibility, true) = true)
        ORDER BY dataPostagem DESC
    """, nativeQuery = true)
    List<FeedProjection> getFeedPorCategoria(@org.springframework.data.repository.query.Param("categoriaId") Long categoriaId);

    @Query(value = """
        SELECT 
            r.id AS relacionamentoId,
            l.id AS lugarId,
            l.nome AS lugarNome,
            c.nome AS categoriaNome,
            CASE 
                WHEN ai_a.nota_media IS NOT NULL AND ai_b.nota_media IS NOT NULL 
                     THEN ROUND((ai_a.nota_media + ai_b.nota_media) / 2, 2)
                WHEN ai_a.nota_media IS NOT NULL THEN ai_a.nota_media
                ELSE ai_b.nota_media
            END AS notaMediaCasal,
            ua.public_id AS usuarioAPublicId,
            ua.nome AS usuarioANome,
            cpr_a.foto_perfil_url AS usuarioAFotoUrl,
            ai_a.nota_media AS notaMediaA,
            ai_a.nota_pergunta_1 AS aNota1,
            ai_a.nota_pergunta_2 AS aNota2,
            ai_a.nota_pergunta_3 AS aNota3,
            ai_a.nota_pergunta_4 AS aNota4,
            ai_a.descricao AS descA,
            ub.public_id AS usuarioBPublicId,
            ub.nome AS usuarioBNome,
            cpr_b.foto_perfil_url AS usuarioBFotoUrl,
            ai_b.nota_media AS notaMediaB,
            ai_b.nota_pergunta_1 AS bNota1,
            ai_b.nota_pergunta_2 AS bNota2,
            ai_b.nota_pergunta_3 AS bNota3,
            ai_b.nota_pergunta_4 AS bNota4,
            ai_b.descricao AS descB,
            (SELECT GROUP_CONCAT(f.url SEPARATOR ',') 
             FROM stage.fotos_avaliacao_individual f 
             WHERE f.avaliacao_id IN (ai_a.id, ai_b.id)) AS fotosUrls,
            GREATEST(COALESCE(ai_a.created_at, '1970-01-01'), COALESCE(ai_b.created_at, '1970-01-01')) - INTERVAL 3 HOUR AS dataPostagem
        FROM stage.relacionamentos r
        JOIN stage.usuarios ua ON r.usuario_a_id = ua.id
        JOIN stage.usuarios ub ON r.usuario_b_id = ub.id
        JOIN stage.lugares l 
        JOIN stage.categorias c ON l.categoria_id = c.id
        LEFT JOIN stage.avaliacao_individual ai_a 
            ON ai_a.relacionamento_id = r.id AND ai_a.lugar_id = l.id AND ai_a.usuario_id = ua.id
        LEFT JOIN stage.avaliacao_individual ai_b 
            ON ai_b.relacionamento_id = r.id AND ai_b.lugar_id = l.id AND ai_b.usuario_id = ub.id
        LEFT JOIN stage.casal_page_respostas cpr_a 
            ON cpr_a.relacionamento_id = r.id AND cpr_a.usuario_id = ua.id
        LEFT JOIN stage.casal_page_respostas cpr_b 
            ON cpr_b.relacionamento_id = r.id AND cpr_b.usuario_id = ub.id
        WHERE r.relacionamento_ativo = true AND (ai_a.id IS NOT NULL OR ai_b.id IS NOT NULL) 
          AND (COALESCE(ai_a.post_visibility, true) = true AND COALESCE(ai_b.post_visibility, true) = true)
        ORDER BY dataPostagem DESC
    """, nativeQuery = true)
    List<FeedProjection> getFeedGeral();

    @Query(value = """
        SELECT 
            r.id AS relacionamentoId,
            l.id AS lugarId,
            l.nome AS lugarNome,
            c.nome AS categoriaNome,
            CASE 
                WHEN ai_a.nota_media IS NOT NULL AND ai_b.nota_media IS NOT NULL 
                     THEN ROUND((ai_a.nota_media + ai_b.nota_media) / 2, 2)
                WHEN ai_a.nota_media IS NOT NULL THEN ai_a.nota_media
                ELSE ai_b.nota_media
            END AS notaMediaCasal,
            ua.public_id AS usuarioAPublicId,
            ua.nome AS usuarioANome,
            cpr_a.foto_perfil_url AS usuarioAFotoUrl,
            ai_a.nota_media AS notaMediaA,
            ai_a.nota_pergunta_1 AS aNota1,
            ai_a.nota_pergunta_2 AS aNota2,
            ai_a.nota_pergunta_3 AS aNota3,
            ai_a.nota_pergunta_4 AS aNota4,
            ai_a.descricao AS descA,
            ub.public_id AS usuarioBPublicId,
            ub.nome AS usuarioBNome,
            cpr_b.foto_perfil_url AS usuarioBFotoUrl,
            ai_b.nota_media AS notaMediaB,
            ai_b.nota_pergunta_1 AS bNota1,
            ai_b.nota_pergunta_2 AS bNota2,
            ai_b.nota_pergunta_3 AS bNota3,
            ai_b.nota_pergunta_4 AS bNota4,
            ai_b.descricao AS descB,
            (SELECT GROUP_CONCAT(f.url SEPARATOR ',') 
             FROM stage.fotos_avaliacao_individual f 
             WHERE f.avaliacao_id IN (ai_a.id, ai_b.id)) AS fotosUrls,
            GREATEST(COALESCE(ai_a.created_at, '1970-01-01'), COALESCE(ai_b.created_at, '1970-01-01')) - INTERVAL 3 HOUR AS dataPostagem
        FROM stage.relacionamentos r
        JOIN stage.usuarios ua ON r.usuario_a_id = ua.id
        JOIN stage.usuarios ub ON r.usuario_b_id = ub.id
        JOIN stage.lugares l 
        JOIN stage.categorias c ON l.categoria_id = c.id
        LEFT JOIN stage.avaliacao_individual ai_a 
            ON ai_a.relacionamento_id = r.id AND ai_a.lugar_id = l.id AND ai_a.usuario_id = ua.id
        LEFT JOIN stage.avaliacao_individual ai_b 
            ON ai_b.relacionamento_id = r.id AND ai_b.lugar_id = l.id AND ai_b.usuario_id = ub.id
        LEFT JOIN stage.casal_page_respostas cpr_a 
            ON cpr_a.relacionamento_id = r.id AND cpr_a.usuario_id = ua.id
        LEFT JOIN stage.casal_page_respostas cpr_b 
            ON cpr_b.relacionamento_id = r.id AND cpr_b.usuario_id = ub.id
        WHERE r.relacionamento_ativo = true AND r.id = :relacionamentoId
          AND (ai_a.id IS NOT NULL OR ai_b.id IS NOT NULL) 
        ORDER BY dataPostagem DESC
    """, nativeQuery = true)
    List<FeedProjection> getFeedDoCasal(@org.springframework.data.repository.query.Param("relacionamentoId") Long relacionamentoId);
}
