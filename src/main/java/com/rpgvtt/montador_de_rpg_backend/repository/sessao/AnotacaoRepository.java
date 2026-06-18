package com.rpgvtt.montador_de_rpg_backend.repository.sessao;

import com.rpgvtt.montador_de_rpg_backend.domain.model.sessao.Anotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnotacaoRepository extends JpaRepository<Anotacao, Integer> {
    List<Anotacao> findBySessao_Id(Long idSessao);
    List<Anotacao> findByCampanha_Id(Long idCampanha);
}
