package com.draftstd.hydraforge.adapters.out.persistence.sessao.repository;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.CenaJpaEntity;
import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CenaRepository extends JpaRepository<CenaJpaEntity, UUID> {

    Optional<CenaJpaEntity> findTopBySessao_IdOrderByOrdemDesc(UUID sessaoId);

    List<CenaJpaEntity> findBySessao_IdOrderByOrdemDesc(UUID sessaoId);

    List<CenaJpaEntity> findBySessao_Id(UUID sessaoId);

    @Query("SELECT COUNT(c) FROM CenaJpaEntity c WHERE c.sessao.campanha.id IN " +
           "(SELECT cu.id.idCampanha FROM CampanhaMembroJpaEntity cu " +
           "WHERE cu.id.idUsuario = :usuarioId AND cu.papel = :papel)")
    long countByCriadorId(@Param("usuarioId") UUID usuarioId, 
                          @Param("papel") PapeisUsuario papel);

    @Query("SELECT COUNT(c) FROM CenaJpaEntity c WHERE c.sessao.campanha.id IN " +
           "(SELECT cu.id.idCampanha FROM CampanhaMembroJpaEntity cu " +
           "WHERE cu.id.idUsuario = :usuarioId AND cu.papel = :papel) " +
           "AND (c.mapaJson IS NULL OR c.urlMapa IS NULL)")
    long countByCriadorIdAndCamposNulos(@Param("usuarioId") UUID usuarioId, 
                                        @Param("papel") PapeisUsuario papel);
}