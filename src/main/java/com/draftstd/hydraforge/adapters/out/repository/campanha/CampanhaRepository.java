package com.draftstd.hydraforge.adapters.out.repository.campanha;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID; // Import necessário para UUID

@Repository
public interface CampanhaRepository extends JpaRepository<Campanha, UUID> { // Alterado de Long para UUID

       Optional<Campanha> findBySessoesId(UUID idSessao); // Alterado de Long para UUID

       @Query("SELECT COUNT(c) FROM Campanha c WHERE c.id IN " +
       "(SELECT cu.id.idCampanha FROM CampanhaUsuario cu " +
       "WHERE cu.id.idUsuario = :usuarioId) " +
       "AND c.status = :status")
       long countByUsuarioIdAndStatus(@Param("usuarioId") UUID usuarioId, // Alterado para UUID
                               @Param("status") StatusCampanha status);


       @Query("SELECT COUNT(DISTINCT cu.id.idUsuario) FROM CampanhaUsuario cu " +
              "WHERE cu.id.idCampanha IN (" +
              "  SELECT cu2.id.idCampanha FROM CampanhaUsuario cu2 " +
              "  WHERE cu2.id.idUsuario = :idUsuario" +
              ") " +
              "AND cu.id.idUsuario <> :idUsuario")
       long countJogadoresNasMesmasCampanhas(@Param("idUsuario") UUID idUsuario); // Alterado para UUID

       @Query("SELECT c FROM Campanha c JOIN CampanhaUsuario cu ON cu.id.idCampanha = c.id " +
              "WHERE cu.id.idUsuario = :usuarioId")
       List<Campanha> findByUsuarioId(@Param("usuarioId") UUID usuarioId); // Alterado para UUID

}