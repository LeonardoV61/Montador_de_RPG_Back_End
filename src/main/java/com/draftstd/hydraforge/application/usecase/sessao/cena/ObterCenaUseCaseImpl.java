package com.draftstd.hydraforge.application.usecase.sessao.cena;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.ports.in.query.sessao.CenaQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sessao.ObterCenaQuery;
import com.draftstd.hydraforge.ports.in.sessao.ObterCenaUseCase;
import com.draftstd.hydraforge.ports.out.sessao.ObterCenaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ObterCenaUseCaseImpl implements ObterCenaUseCase {

    private final ObterCenaPort obterCenaPort;

    public ObterCenaUseCaseImpl(ObterCenaPort obterCenaPort) {
        this.obterCenaPort = obterCenaPort;
    }

    @Override
    public CenaQueryResponse obterPorId(ObterCenaQuery query) {
        Cena cena = obterCenaPort.buscarPorId(query.cenaId()).orElseThrow(() -> new RecursoNaoEncontradoException("Cena não encontrada: " + query.cenaId()));

        // Conversão segura do ID Long para UUID
        UUID cenaUuid = cena.getId() != null ? UUID.nameUUIDFromBytes(cena.getId().toString().getBytes()) : UUID.randomUUID();

        // Extração segura do ID da sessão a partir da relação JPA
        UUID sessaoId = (cena.getSessao() != null && cena.getSessao().getId() != null) ? UUID.nameUUIDFromBytes(cena.getSessao().getId().toString().getBytes()) : null;

        return new CenaQueryResponse(cenaUuid, sessaoId, null, cena.getTipo(), cena.getUrlMapa(), true, LocalDateTime.now());
    }
}