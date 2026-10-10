package com.draftstd.hydraforge.adapters.out.repository.mecanica;

import com.draftstd.hydraforge.domain.model.mecanica.Resolucao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResolucaoRepository extends JpaRepository<Resolucao, Long> {

    List<Resolucao> findBySistemaId(Long sistemaId);
}