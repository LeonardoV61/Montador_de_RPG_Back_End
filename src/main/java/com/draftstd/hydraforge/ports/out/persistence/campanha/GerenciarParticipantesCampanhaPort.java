package com.draftstd.hydraforge.ports.out.persistence.campanha;

import com.draftstd.hydraforge.domain.model.campanha.CampanhaMembro;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GerenciarParticipantesCampanhaPort {
    void salvarParticipante(UUID campanhaId, CampanhaMembro membro);
    void removerParticipante(UUID campanhaId, UUID usuarioId);
    Optional<CampanhaMembro> buscarParticipante(UUID campanhaId, UUID usuarioId);
    List<CampanhaMembro> listarParticipantesPorCampanha(UUID campanhaId);
}