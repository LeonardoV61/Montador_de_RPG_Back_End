package com.draftstd.hydraforge.adapters.out.repository.sessao;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.model.sessao.Cena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CenaRepository extends JpaRepository<Cena, UUID> {

    Optional<Cena> findTopBySessao_IdOrderByOrdemDesc(UUID idSessao);

    List<Cena> findBySessao_IdOrderByOrdemDesc(UUID idSessao);

    List<Cena> findAllBySessaoId(UUID sessaoId);

    @Query("SELECT COUNT(c) FROM Cena c WHERE c.sessao.campanha.id IN " +
           "(SELECT cu.id.idCampanha FROM CampanhaUsuario cu " +
           "WHERE cu.id.idUsuario = :usuarioId AND cu.papel = :papel)")
    long countByCriadorId(@Param("usuarioId") UUID usuarioId, 
                          @Param("papel") PapeisUsuario papel);

    @Query("SELECT COUNT(c) FROM Cena c WHERE c.sessao.campanha.id IN " +
           "(SELECT cu.id.idCampanha FROM CampanhaUsuario cu " +
           "WHERE cu.id.idUsuario = :usuarioId AND cu.papel = :papel) " +
           "AND (c.mapaJson IS NULL OR c.urlMapa IS NULL)")
    long countByCriadorIdAndCamposNulos(@Param("usuarioId") UUID usuarioId, 
                                        @Param("papel") PapeisUsuario papel);
}