package com.draftstd.hydraforge.application.usecase.sessao.cena;

import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.ports.in.command.sessao.CriarCenaCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.CenaQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.CriarCenaUseCase;
import com.draftstd.hydraforge.ports.out.sessao.SalvarCenaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CriarCenaUseCaseImpl implements CriarCenaUseCase {

    private final SalvarCenaPort salvarCenaPort;

    public CriarCenaUseCaseImpl(SalvarCenaPort salvarCenaPort) {
        this.salvarCenaPort = salvarCenaPort;
    }

    @Override
    @Transactional
    public CenaQueryResponse executar(CriarCenaCommand command) {
        Cena cena = new Cena();
        cena.setId(UUID.randomUUID());
        cena.setSessaoId(command.sessaoId());
        cena.setTitulo(command.titulo());
        cena.setAmbiente(command.ambiente());
        cena.setMapaUrl(command.mapaUrl());
        cena.setAtiva(true);

        Cena salva = salvarCenaPort.salvar(cena);

        return new CenaQueryResponse(
                salva.getId(),
                salva.getSessaoId(),
                salva.getTitulo(),
                salva.getAmbiente(),
                salva.getMapaUrl(),
                salva.isAtiva()
        );
    }
}