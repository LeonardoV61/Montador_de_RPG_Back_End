package com.draftstd.hydraforge.adapters.out.repository.sessao;

import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.domain.model.sessao.CenaParticipantes;
import com.draftstd.hydraforge.domain.model.sessao.CenaParticipantesKey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CenaParticipantesRepository extends JpaRepository<CenaParticipantes, CenaParticipantesKey> {

    List<CenaParticipantes> findByCena(Cena cena);
}
