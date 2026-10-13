package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.query.sessao.ObterDetalhesSessaoQuery;
import com.draftstd.hydraforge.ports.in.query.sessao.SessaoQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.ObterDetalhesSessaoUseCase;
import com.draftstd.hydraforge.ports.out.sessao.ObterSessaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ObterDetalhesSessaoUseCaseImpl implements ObterDetalhesSessaoUseCase {

    private final ObterSessaoPort obterSessaoPort;

    public ObterDetalhesSessaoUseCaseImpl(ObterSessaoPort obterSessaoPort) {
        this.obterSessaoPort = obterSessaoPort;
    }

    @Override
    public SessaoQueryResponse obterPorId(ObterDetalhesSessaoQuery query) {
        Sessao sessao = obterSessaoPort.buscarPorId(query.sessaoId()).orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + query.sessaoId()));

        // Conversão segura do ID Long para UUID
        UUID sessaoUuid = sessao.getId() != null ? UUID.nameUUIDFromBytes(sessao.getId().toString().getBytes()) : UUID.randomUUID();

        // Extração segura do ID da campanha a partir da relação JPA
        UUID campanhaId = (sessao.getCampanha() != null && sessao.getCampanha().getId() != null) ? UUID.nameUUIDFromBytes(sessao.getCampanha().getId().toString().getBytes()): null;

        return new SessaoQueryResponse(sessaoUuid,campanhaId,sessao.getStatus(),List.of());
    }
}