package com.draftstd.hydraforge.adapters.out.repository.personagem;

import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonagemRepository extends JpaRepository<Personagem, Long> {

    List<Personagem> findByCampanha_Sessoes_Id(Long sessaoId);

    Optional<Personagem> findAtivoByCampanhaIdAndUsuarioId(Long idCampanha, Long idUsuario);

    Optional<Personagem> findByInstanciaIdAndUsuarioId(Long idInstancia, Long idUsuario);

    Optional<Personagem> findByUsuarioId(Long idSessao, Long idUsuario);
}