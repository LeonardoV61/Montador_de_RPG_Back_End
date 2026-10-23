package com.draftstd.hydraforge.adapters.out.persistence.usuario.repository;

import com.draftstd.hydraforge.adapters.out.persistence.usuario.entity.AmizadeJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.usuario.entity.AmizadeKeyEmbeddable;
import com.draftstd.hydraforge.domain.enums.StatusAmizade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AmizadeRepository extends JpaRepository<AmizadeJpaEntity, AmizadeKeyEmbeddable> {

    @Query("""
        SELECT a FROM AmizadeJpaEntity a
        WHERE (a.id.idRemetente = :idA AND a.id.idDestinatario = :idB)
           OR (a.id.idRemetente = :idB AND a.id.idDestinatario = :idA)
    """)
    Optional<AmizadeJpaEntity> findEntreUsuarios(@Param("idA") UUID idA, @Param("idB") UUID idB);

    @Query("""
        SELECT a FROM AmizadeJpaEntity a
        WHERE (a.id.idRemetente = :idUsuario OR a.id.idDestinatario = :idUsuario)
          AND a.status = :status
    """)
    List<AmizadeJpaEntity> findByUsuarioAndStatus(@Param("idUsuario") UUID idUsuario,
                                                  @Param("status") StatusAmizade status);

    List<AmizadeJpaEntity> findById_IdDestinatarioAndStatus(UUID idDestinatario, StatusAmizade status);
}