package com.rpgvtt.montador_de_rpg_backend.domain.repository;

import com.rpgvtt.montador_de_rpg_backend.domain.model.Primitivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrimitivoRepository extends JpaRepository<Primitivo, Integer> {

}