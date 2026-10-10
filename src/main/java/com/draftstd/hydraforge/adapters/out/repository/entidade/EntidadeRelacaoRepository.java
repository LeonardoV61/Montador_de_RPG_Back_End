package com.draftstd.hydraforge.adapters.out.repository.entidade;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeRelacao;
import com.draftstd.hydraforge.domain.model.entidade.EntidadeRelacaoKey;
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