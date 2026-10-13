package com.draftstd.hydraforge.adapters.out.repository.sessao;

import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnotacaoRepository extends JpaRepository<Anotacao, UUID> {
    List<Anotacao> findAllBySessaoId(UUID sessaoId);
    List<Anotacao> findByCampanha_Id(UUID idCampanha);
}