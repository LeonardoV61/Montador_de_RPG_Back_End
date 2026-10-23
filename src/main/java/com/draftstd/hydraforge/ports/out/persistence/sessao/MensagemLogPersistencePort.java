package com.draftstd.hydraforge.ports.out.persistence.sessao;

import com.draftstd.hydraforge.domain.model.sessao.MensagemLog;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MensagemLogPersistencePort {
    MensagemLog salvar(UUID sessaoId, MensagemLog mensagem);
    Optional<MensagemLog> buscarPorId(UUID id);
    List<MensagemLog> listarPorSessaoId(UUID sessaoId);
    List<MensagemLog> listarTodas();
    void deletarPorId(UUID id);
}