package com.draftstd.hydraforge.adapters.out.persistence.sessao.repository;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.CenaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.CenaParticipantesJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.CenaParticipantesKeyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CenaParticipantesRepository extends JpaRepository<CenaParticipantesJpaEntity, CenaParticipantesKeyJpaEntity> {

    @Query("SELECT cp FROM CenaParticipantesJpaEntity cp WHERE cp.cena = :cena")
    List<CenaParticipantesJpaEntity> findByCena(@Param("cena") CenaJpaEntity cena);
}