package com.draftstd.hydraforge.adapters.out.persistence.campanha.repository;

import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaMembroJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaMembroKeyJpaEntity;
import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CampanhaMembroRepository extends JpaRepository<CampanhaMembroJpaEntity, CampanhaMembroKeyJpaEntity> {

    @Query("SELECT cm FROM CampanhaMembroJpaEntity cm WHERE cm.id.idCampanha = :campanhaId AND cm.id.idUsuario = :usuarioId AND cm.papel = :papel")
    Optional<CampanhaMembroJpaEntity> findByCampanhaIdAndUsuarioIdAndPapel(
            @Param("campanhaId") UUID campanhaId,
            @Param("usuarioId") UUID usuarioId,
            @Param("papel") PapeisUsuario papel
    );

    @Query("SELECT cm FROM CampanhaMembroJpaEntity cm WHERE cm.id.idCampanha = :campanhaId AND cm.id.idUsuario = :usuarioId")
    Optional<CampanhaMembroJpaEntity> findByCampanhaIdAndUsuarioId(
            @Param("campanhaId") UUID campanhaId,
            @Param("usuarioId") UUID usuarioId
    );

    @Query("SELECT cm FROM CampanhaMembroJpaEntity cm WHERE cm.id.idCampanha = :campanhaId")
    List<CampanhaMembroJpaEntity> findByCampanhaId(@Param("campanhaId") UUID campanhaId);
}