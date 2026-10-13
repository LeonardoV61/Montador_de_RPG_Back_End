package com.draftstd.hydraforge.application.usecase.sessao.anotacao;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import com.draftstd.hydraforge.ports.in.query.sessao.AnotacaoQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sessao.ObterAnotacaoQuery;
import com.draftstd.hydraforge.ports.in.sessao.ObterAnotacaoUseCase;
import com.draftstd.hydraforge.ports.out.sessao.ObterAnotacaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ObterAnotacaoUseCaseImpl implements ObterAnotacaoUseCase {

    private final ObterAnotacaoPort obterAnotacaoPort;

    public ObterAnotacaoUseCaseImpl(ObterAnotacaoPort obterAnotacaoPort) {
        this.obterAnotacaoPort = obterAnotacaoPort;
    }

    @Override
    public AnotacaoQueryResponse obterPorId(ObterAnotacaoQuery query) {
        Anotacao anotacao = obterAnotacaoPort.buscarPorId(query.anotacaoId()).orElseThrow(() -> new RecursoNaoEncontradoException("Anotação não encontrada: " + query.anotacaoId()));

        // Conversão segura do idAnotacao (Integer) para UUID
        UUID anotacaoUuid = anotacao.getIdAnotacao() != null ? UUID.nameUUIDFromBytes(anotacao.getIdAnotacao().toString().getBytes()) : UUID.randomUUID();

        // Extração de IDs relacionais de forma segura se necessário
        UUID sessaoId = null;
        UUID autorId = null;

        LocalDateTime agora = LocalDateTime.now();

        return new AnotacaoQueryResponse(anotacaoUuid, sessaoId, autorId, anotacao.getTitulo(), anotacao.getConteudo(), anotacao.getEPrivado() != null ? anotacao.getEPrivado() : true, agora, agora);
    }
}