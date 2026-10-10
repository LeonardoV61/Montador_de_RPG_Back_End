package com.draftstd.hydraforge.adapters.out.projections.persistence.sessao;

import com.draftstd.hydraforge.ports.out.persistence.sessao.MensagemLogOutputPort;
import com.draftstd.hydraforge.domain.model.sessao.MensagemLog;
import com.draftstd.hydraforge.adapters.out.repository.sessao.MensagemLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MensagemLogPersistenceAdapter implements MensagemLogOutputPort {

    private final MensagemLogRepository mensagemLogRepository;

    @Override
    public MensagemLog salvar(MensagemLog mensagem) {
        return mensagemLogRepository.save(mensagem);
    }

    @Override
    public Optional<MensagemLog> buscarPorId(Long id) {
        return mensagemLogRepository.findById(id);
    }

    @Override
    public List<MensagemLog> listarTodas() {
        return mensagemLogRepository.findAll();
    }

    @Override
    public void deletarPorId(Long id) {
        mensagemLogRepository.deleteById(id);
    }
}