package com.draftstd.hydraforge.application.usecase.sessao.cena;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.ports.in.command.sessao.AtualizarCenaCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.CenaQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarCenaUseCase;
import com.draftstd.hydraforge.ports.out.sessao.ObterCenaPort;
import com.draftstd.hydraforge.ports.out.sessao.SalvarCenaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class GerenciarCenaUseCaseImpl implements GerenciarCenaUseCase {

    private final ObterCenaPort obterCenaPort;
    private final SalvarCenaPort salvarCenaPort;

    public GerenciarCenaUseCaseImpl(ObterCenaPort obterCenaPort, SalvarCenaPort salvarCenaPort) {
        this.obterCenaPort = obterCenaPort;
        this.salvarCenaPort = salvarCenaPort;
    }

    @Override
    public CenaQueryResponse atualizar(AtualizarCenaCommand command) {
        Cena cena = obterCenaPort.buscarPorId(command.cenaId()).orElseThrow(() -> new RecursoNaoEncontradoException("Cena não encontrada: " + command.cenaId()));

        if (command.mapaUrl() != null) {
            cena.setUrlMapa(command.mapaUrl());
        }
        if (command.ambiente() != null) {
            cena.setTipo(command.ambiente());
        }

        Cena atualizada = salvarCenaPort.salvar(cena);

        UUID cenaUuid = atualizada.getId() != null ? UUID.nameUUIDFromBytes(atualizada.getId().toString().getBytes()) : UUID.randomUUID();

        // Extração corrigida utilizando .getId() em vez de .getIdSessao()
        UUID sessaoId = (atualizada.getSessao() != null && atualizada.getSessao().getId() != null) ? UUID.nameUUIDFromBytes(atualizada.getSessao().getId().toString().getBytes()) : null;

        return new CenaQueryResponse(cenaUuid, sessaoId, command.titulo(), command.ambiente(), atualizada.getUrlMapa(), true, LocalDateTime.now());
    }
}