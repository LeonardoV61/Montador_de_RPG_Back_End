package com.draftstd.hydraforge.ports.out.persistence.personagem;

import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObterPersonagemPort {
    Optional<Personagem> buscarPorId(UUID id);
    List<Personagem> listarPorUsuarioId(UUID usuarioId);
    List<Personagem> listarPorCampanhaId(UUID campanhaId);
    Optional<Personagem> buscarAtivoPorCampanhaEUsuario(UUID campanhaId, UUID usuarioId);
}