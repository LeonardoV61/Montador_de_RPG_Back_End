package com.draftstd.hydraforge.ports.out.persistence.sessao;

import com.draftstd.hydraforge.domain.model.sessao.MensagemLog;

import java.util.List;
import java.util.Optional;

public interface MensagemLogOutputPort {
    MensagemLog salvar(MensagemLog mensagem);
    Optional<MensagemLog> buscarPorId(Long id);
    List<MensagemLog> listarTodas();
    void deletarPorId(Long id);
}