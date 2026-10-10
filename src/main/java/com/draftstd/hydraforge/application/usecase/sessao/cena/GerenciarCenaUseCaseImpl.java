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
        Cena cena = obterCenaPort.buscarPorId(command.cenaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cena não encontrada: " + command.cenaId()));

        cena.setTitulo(command.titulo());
        cena.setAmbiente(command.ambiente());
        cena.setMapaUrl(command.mapaUrl());
        cena.setAtiva(command.ativa());

        Cena atualizada = salvarCenaPort.salvar(cena);

        return new CenaQueryResponse(
                atualizada.getId(),
                atualizada.getSessaoId(),
                atualizada.getTitulo(),
                atualizada.getAmbiente(),
                atualizada.getMapaUrl(),
                atualizada.isAtiva()
        );
    }
}