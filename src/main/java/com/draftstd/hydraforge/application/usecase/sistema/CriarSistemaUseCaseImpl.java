package com.draftstd.hydraforge.application.usecase.sistema;

import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.ports.in.command.sistema.CriarSistemaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.SistemaQueryResponse;
import com.draftstd.hydraforge.ports.in.sistema.CriarSistemaUseCase;
import com.draftstd.hydraforge.ports.out.sistema.SalvarSistemaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CriarSistemaUseCaseImpl implements CriarSistemaUseCase {

    private final SalvarSistemaPort salvarSistemaPort;

    public CriarSistemaUseCaseImpl(SalvarSistemaPort salvarSistemaPort) {
        this.salvarSistemaPort = salvarSistemaPort;
    }

    @Override
    public SistemaQueryResponse executar(CriarSistemaCommand command) {
        Sistema s = new Sistema();
        // O ID Long é gerado por sequence, não chamamos setId com UUID
        s.setNome(command.nome());
        s.setDescricao(command.descricao());
        
        if (command.versao() != null) {
            s.setVersaoSchemas(Integer.valueOf(command.versao()));
        }

        Sistema salvo = salvarSistemaPort.salvar(s);

        // Conversão segura do ID Long da entidade salva para UUID
        UUID sistemaUuid = salvo.getId() != null 
                ? UUID.nameUUIDFromBytes(salvo.getId().toString().getBytes()) 
                : UUID.randomUUID();

        UUID autorId = (salvo.getUsuario() != null && salvo.getUsuario().getId() != null)
                ? UUID.nameUUIDFromBytes(salvo.getUsuario().getId().toString().getBytes())
                : null;

        String versaoStr = salvo.getVersaoSchemas() != null 
                ? String.valueOf(salvo.getVersaoSchemas()) 
                : "1";

        return new SistemaQueryResponse(
                sistemaUuid, 
                salvo.getNome(), 
                salvo.getDescricao(), 
                versaoStr,
                autorId,
                null,
                List.of(),
                salvo.getCriadoEm() != null ? salvo.getCriadoEm() : LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}