package com.draftstd.hydraforge.adapters.out.repository.sessao;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, UUID> {
    List<Sessao> findByStatusAndProcedimentoAtivoIsNotNull(StatusSessao status);
    Optional<Sessao> findAtivaByCampanhaId(UUID idCampanha);
    List<Sessao> findByCampanhaId(UUID idCampanha);
    List<Sessao> findByCampanhaIdOrderByOrdemDesc(UUID campanhaId);
    Optional<Sessao> findFirstByCampanhaIdAndStatusOrderByDataInicioDesc(UUID campanhaId, StatusSessao status);
    int countByCampanhaId(UUID idCampanha);

    @Query("SELECT COUNT(s) FROM Sessao s WHERE s.campanha.id IN " +
           "(SELECT cu.id.idCampanha FROM CampanhaUsuario cu " +
           "WHERE cu.id.idUsuario = :usuarioId) " +
           "AND MONTH(s.dataInicio) = :mes AND YEAR(s.dataInicio) = :ano")
    long countByUsuarioIdAndMesAtual(@Param("usuarioId") UUID usuarioId, @Param("mes") int mes, @Param("ano") int ano);
}