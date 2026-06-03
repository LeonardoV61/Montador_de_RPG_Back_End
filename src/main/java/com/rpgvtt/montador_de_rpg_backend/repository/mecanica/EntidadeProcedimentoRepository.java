package com.rpgvtt.montador_de_rpg_backend.repository.mecanica;

import com.rpgvtt.montador_de_rpg_backend.domain.model.mecanica.EntidadeProcedimento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntidadeProcedimentoRepository extends JpaRepository<EntidadeProcedimento, Long> {

    List<EntidadeProcedimento> findByEntidadeSistemaId(Long entidadeId);
}
