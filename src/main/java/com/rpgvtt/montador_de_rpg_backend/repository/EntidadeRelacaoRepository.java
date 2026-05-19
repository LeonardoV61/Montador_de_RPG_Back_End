package com.rpgvtt.montador_de_rpg_backend.domain.repository;

import com.rpgvtt.montador_de_rpg_backend.domain.model.EntidadeRelacao;
import com.rpgvtt.montador_de_rpg_backend.domain.model.EntidadeRelacionamentoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntidadeRelacaoRepository extends JpaRepository<EntidadeRelacao, EntidadeRelacionamentoId> {

}