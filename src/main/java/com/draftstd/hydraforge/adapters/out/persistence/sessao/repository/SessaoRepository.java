package com.draftstd.hydraforge.adapters.out.persistence.sessao.repository;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.SessaoJpaEntity;
import com.draftstd.hydraforge.domain.enums.StatusSessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessaoRepository extends JpaRepository<SessaoJpaEntity, UUID> {

    List<SessaoJpaEntity> findByStatusAndProcedimentoAtivoIdIsNotNull(StatusSessao status);
    
    @Query("SELECT s FROM SessaoJpaEntity s WHERE s.campanha.id = :idCampanha AND s.status = 'ATIVA'")
    Optional<SessaoJpaEntity> findAtivaByCampanhaId(@Param("idCampanha") UUID idCampanha);

    List<SessaoJpaEntity> findByCampanha_Id(UUID idCampanha);

    List<SessaoJpaEntity> findByCampanha_IdOrderByOrdemDesc(UUID campanhaId);

    Optional<SessaoJpaEntity> findFirstByCampanha_IdAndStatusOrderByDataInicioDesc(UUID campanhaId, StatusSessao status);

    int countByCampanha_Id(UUID idCampanha);

    @Query("SELECT COUNT(s) FROM SessaoJpaEntity s WHERE s.campanha.id IN " +
           "(SELECT cu.id.idCampanha FROM CampanhaMembroJpaEntity cu " +
           "WHERE cu.id.idUsuario = :usuarioId) " +
           "AND MONTH(s.dataInicio) = :mes AND YEAR(s.dataInicio) = :ano")
    long countByUsuarioIdAndMesAtual(@Param("usuarioId") UUID usuarioId, @Param("mes") int mes, @Param("ano") int ano);
}