package com.draftstd.hydraforge.adapters.out.persistence.campanha.repository;

import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaJpaEntity;
import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CampanhaRepository extends JpaRepository<CampanhaJpaEntity, UUID> {

    @Query("SELECT COUNT(c) FROM CampanhaJpaEntity c JOIN c.membros m " +
           "WHERE m.id.idUsuario = :usuarioId AND c.status = :status")
    long countByUsuarioIdAndStatus(@Param("usuarioId") UUID usuarioId,
                                   @Param("status") StatusCampanha status);

    @Query("SELECT COUNT(DISTINCT m.id.idUsuario) FROM CampanhaMembroJpaEntity m " +
           "WHERE m.id.idCampanha IN (" +
           "  SELECT m2.id.idCampanha FROM CampanhaMembroJpaEntity m2 " +
           "  WHERE m2.id.idUsuario = :idUsuario" +
           ") " +
           "AND m.id.idUsuario <> :idUsuario")
    long countJogadoresNasMesmasCampanhas(@Param("idUsuario") UUID idUsuario);

    @Query("SELECT c FROM CampanhaJpaEntity c JOIN c.membros m " +
           "WHERE m.id.idUsuario = :usuarioId")
    List<CampanhaJpaEntity> findByUsuarioId(@Param("usuarioId") UUID usuarioId);
}