package com.draftstd.hydraforge.application.usecase.usuario;

import com.draftstd.hydraforge.application.request.usuario.EnviarSolicitacaoAmizadeCommand;
import com.draftstd.hydraforge.application.request.usuario.ResponderSolicitacaoAmizadeCommand;
import com.draftstd.hydraforge.application.response.usuario.AmizadeQueryResponse;
import com.draftstd.hydraforge.application.response.usuario.ListarAmigosQuery;
import com.draftstd.hydraforge.domain.enums.StatusAmizade;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.usuario.Amizade;
import com.draftstd.hydraforge.domain.model.usuario.Usuario;
import com.draftstd.hydraforge.ports.in.usuario.GerenciarAmizadeUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.usuario.ObterAmizadePort;
import com.draftstd.hydraforge.ports.out.persistence.usuario.ObterUsuarioPort;
import com.draftstd.hydraforge.ports.out.persistence.usuario.SalvarAmizadePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GerenciarAmizadeUseCaseImpl implements GerenciarAmizadeUseCase {

    private final ObterUsuarioPort obterUsuarioPort;
    private final SalvarAmizadePort salvarAmizadePort;
    private final ObterAmizadePort obterAmizadePort;
    private final EventStorePort eventStorePort;

    public GerenciarAmizadeUseCaseImpl(ObterUsuarioPort obterUsuarioPort,
                                       SalvarAmizadePort salvarAmizadePort,
                                       ObterAmizadePort obterAmizadePort,
                                       EventStorePort eventStorePort) {
        this.obterUsuarioPort = obterUsuarioPort;
        this.salvarAmizadePort = salvarAmizadePort;
        this.obterAmizadePort = obterAmizadePort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    @Transactional
    public void enviarSolicitacao(EnviarSolicitacaoAmizadeCommand command) {
        Usuario remetente = obterUsuarioPort.buscarPorId(command.remetenteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Remetente não encontrado com ID: " + command.remetenteId()));

        obterUsuarioPort.buscarPorId(command.destinatarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Destinatário não encontrado com ID: " + command.destinatarioId()));

        long versaoAtual = remetente.getVersion();

        // Aciona o comportamento do Agregado, registrando o SolicitacaoAmizadeEnviadaEvent
        remetente.enviarSolicitacaoAmizade(command.destinatarioId());

        // Grava eventos no Event Store
        eventStorePort.salvarEventos(remetente.getId().toString(), remetente.getUncommittedEvents(), versaoAtual);
        remetente.markEventsAsCommitted();

        // Atualiza a projeção relacional de amizades
        Amizade amizade = new Amizade(
                command.remetenteId(),
                command.destinatarioId(),
                StatusAmizade.PENDENTE,
                LocalDateTime.now(),
                null
        );
        salvarAmizadePort.salvar(amizade);
    }

    @Override
    @Transactional
    public void aceitarSolicitacao(ResponderSolicitacaoAmizadeCommand command) {
        Usuario destinatario = obterUsuarioPort.buscarPorId(command.destinatarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + command.destinatarioId()));

        Amizade amizade = obterAmizadePort.buscarEntreUsuarios(command.remetenteId(), command.destinatarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação de amizade não encontrada"));

        long versaoAtual = destinatario.getVersion();

        destinatario.responderSolicitacaoAmizade(command.remetenteId(), StatusAmizade.ACEITO);

        eventStorePort.salvarEventos(destinatario.getId().toString(), destinatario.getUncommittedEvents(), versaoAtual);
        destinatario.markEventsAsCommitted();

        amizade.aceitar();
        salvarAmizadePort.salvar(amizade);
    }

    @Override
    @Transactional
    public void recusarOuRemover(ResponderSolicitacaoAmizadeCommand command) {
        if (obterAmizadePort.existeEntreUsuarios(command.remetenteId(), command.destinatarioId())) {
            salvarAmizadePort.deletar(command.remetenteId(), command.destinatarioId());
        } else {
            throw new RecursoNaoEncontradoException("Relação de amizade não encontrada");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmizadeQueryResponse> listarAmigos(ListarAmigosQuery query) {
        UUID usuarioId = query.usuarioId();
        List<Amizade> amizades = obterAmizadePort.listarPorUsuarioEStatus(usuarioId, StatusAmizade.ACEITO);

        return amizades.stream().map(amizade -> {
            UUID amigoId = amizade.getIdRemetente().equals(usuarioId) ? amizade.getIdDestinatario() : amizade.getIdRemetente();
            Usuario amigo = obterUsuarioPort.buscarPorId(amigoId).orElse(null);

            String apelidoAmigo = amigo != null ? amigo.getApelido() : "Desconhecido";
            String urlImagemAmigo = amigo != null ? amigo.getUrlImagem() : null;

            return new AmizadeQueryResponse(
                    amizade.getIdRemetente(),
                    amizade.getIdDestinatario(),
                    apelidoAmigo,
                    urlImagemAmigo,
                    amizade.getStatus(),
                    amizade.getCriadaEm(),
                    amizade.getAceitaEm()
            );
        }).toList();
    }
}