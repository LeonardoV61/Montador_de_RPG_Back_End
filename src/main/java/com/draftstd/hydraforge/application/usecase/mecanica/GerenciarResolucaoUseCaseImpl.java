package com.draftstd.hydraforge.application.usecase.mecanica;

import com.draftstd.hydraforge.adapters.in.dto.mecanica.ResolucaoCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.mecanica.ResolucaoResponseDTO;
import com.draftstd.hydraforge.ports.in.command.mecanica.RolarDadosCommand;
import com.draftstd.hydraforge.ports.in.mecanica.RolarDadosUseCase;
import com.draftstd.hydraforge.ports.in.query.mecanica.ResultadoRolagemQueryResponse;
import com.draftstd.hydraforge.ports.out.persistence.mecanica.ResolucaoOutputPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GerenciarResolucaoUseCaseImpl implements RolarDadosUseCase {

    private final ResolucaoOutputPort resolucaoOutputPort;

    public GerenciarResolucaoUseCaseImpl(ResolucaoOutputPort resolucaoOutputPort) {
        this.resolucaoOutputPort = resolucaoOutputPort;
    }

    @Override
    public ResultadoRolagemQueryResponse executar(RolarDadosCommand command) {
        ResolucaoCreateDTO dto = new ResolucaoCreateDTO(null, command.motivo() != null ? command.motivo() : "Rolagem de Dados","ROLAGEM_DADOS",null);

        ResolucaoResponseDTO salvo = resolucaoOutputPort.salvar(dto);

        Long resolucaoId = salvo != null ? salvo.id() : null;
        UUID resolucaoUuid = resolucaoId != null ? UUID.nameUUIDFromBytes(resolucaoId.toString().getBytes()) : UUID.randomUUID();

        return new ResultadoRolagemQueryResponse(resolucaoUuid, command.sessaoId(), command.jogadorId(), command.personagemId(), command.expressaoDados(), List.of(), 0, 0, command.motivo(), LocalDateTime.now());
    }
}