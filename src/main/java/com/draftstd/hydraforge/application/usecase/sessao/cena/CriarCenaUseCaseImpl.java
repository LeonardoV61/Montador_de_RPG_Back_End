package com.draftstd.hydraforge.application.usecase.sessao.cena;

import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.command.sessao.CriarCenaCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.CenaQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.CriarCenaUseCase;
import com.draftstd.hydraforge.ports.out.sessao.SalvarCenaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

        if (command.sessaoId() != null) {
            Sessao sessao = new Sessao();
            cena.setSessao(sessao);
        }

        // Atribuindo diretamente aos campos validados da entidade Cena
        cena.setUrlMapa(command.mapaUrl());
        cena.setTipo(command.ambiente() != null ? command.ambiente() : "Geral");
        cena.setOrdem(1); // Ordem inicial padrão obrigatória

        Cena salva = salvarCenaPort.salvar(cena);

        // Conversão segura do ID Long para UUID para o response
        UUID cenaUuid = salva.getId() != null ? UUID.nameUUIDFromBytes(salva.getId().toString().getBytes()) : UUID.randomUUID();

        return new CenaQueryResponse(cenaUuid, command.sessaoId(), command.titulo(), command.ambiente(), salva.getUrlMapa(), true, LocalDateTime.now());
    }
}