package com.draftstd.hydraforge.adapters.out.repository.sessao;

import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnotacaoRepository extends JpaRepository<Anotacao, Integer> {
    List<Anotacao> findBySessao_Id(Long idSessao);
    List<Anotacao> findByCampanha_Id(Long idCampanha);
}
