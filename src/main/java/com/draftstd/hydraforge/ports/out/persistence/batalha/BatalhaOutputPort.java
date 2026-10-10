package com.draftstd.hydraforge.ports.out.persistence.batalha;

import com.draftstd.hydraforge.domain.enums.BatalhaStatus;
import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import com.draftstd.hydraforge.domain.model.batalha.BatalhaParticipantes;

import java.util.List;
import java.util.Optional;

public interface BatalhaOutputPort {
    Batalha salvar(Batalha batalha);
    Optional<Batalha> buscarPorId(Long id);
    List<Batalha> buscarPorSessaoId(Long sessaoId);
    Optional<Batalha> buscarBatalhaAtivaPorSessaoId(Long sessaoId);
    void deletarPorId(Long id);

    // Métodos para gestão de participantes da batalha
    BatalhaParticipantes salvarParticipante(BatalhaParticipantes participante);
    List<BatalhaParticipantes> buscarParticipantesPorBatalhaId(Long batalhaId);
    void removerParticipante(Long batalhaId, Long entidadeInstanciaId);
}