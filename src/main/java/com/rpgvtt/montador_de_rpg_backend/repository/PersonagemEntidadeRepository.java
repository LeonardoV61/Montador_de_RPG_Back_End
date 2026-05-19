package com.rpgvtt.montador_de_rpg_backend.domain.repository;

import com.rpgvtt.montador_de_rpg_backend.domain.model.PersonagemEntidade;
import com.rpgvtt.montador_de_rpg_backend.domain.model.PersonagemEntidadeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonagemEntidadeRepository extends JpaRepository<PersonagemEntidade, PersonagemEntidadeKey> {

}