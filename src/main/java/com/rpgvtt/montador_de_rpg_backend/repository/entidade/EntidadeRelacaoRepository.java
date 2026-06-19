package com.rpgvtt.montador_de_rpg_backend.repository.entidade;

import com.rpgvtt.montador_de_rpg_backend.domain.model.entidade.EntidadeRelacao;
import com.rpgvtt.montador_de_rpg_backend.domain.model.entidade.EntidadeRelacaoKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntidadeRelacaoRepository extends JpaRepository<EntidadeRelacao, EntidadeRelacaoKey> {

    List<EntidadeRelacao> findByEntidadePaiId(Long idPai);
    Optional<EntidadeRelacao> findByEntidadePaiIdAndEntidadeFilhaId(Long idPai, Long idFilho);
    List<EntidadeRelacao> findAllByEntidadePaiId(long idPai);

}