package com.draftstd.hydraforge.adapters.out.persistence.sessao.repository;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.EfeitoAtivoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EfeitoAtivoRepository extends JpaRepository<EfeitoAtivoJpaEntity, UUID> {

    List<EfeitoAtivoJpaEntity> findByEntidadeInstanciaIdAndAtivo(UUID entidadeInstanciaId, Boolean ativo);

    List<EfeitoAtivoJpaEntity> findBySessao_Id(UUID sessaoId);

    List<EfeitoAtivoJpaEntity> findBySessao_IdAndMomento(UUID sessaoId, String momento);
}