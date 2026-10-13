package com.draftstd.hydraforge.adapters.out.repository.personagem;

import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonagemRepository extends JpaRepository<Personagem, UUID> {
    List<Personagem> findAllByUsuarioId(UUID usuarioId);
    List<Personagem> findAllByCampanhaId(UUID campanhaId);
    List<Personagem> findByCampanha_Sessoes_Id(UUID sessaoId);
    Optional<Personagem> findAtivoByCampanhaIdAndUsuarioId(UUID idCampanha, UUID idUsuario);
    Optional<Personagem> findByInstanciaIdAndUsuarioId(UUID idInstancia, UUID idUsuario);
}