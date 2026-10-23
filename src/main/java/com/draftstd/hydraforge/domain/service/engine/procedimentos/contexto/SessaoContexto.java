package com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto;

import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EscopoInstancias;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.snapshot.ProcedimentoSnapshot;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.snapshot.SessaoSnapshot;
import com.draftstd.hydraforge.ports.out.persistence.sessao.SessaoPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessaoContexto {

    private final ConcurrentHashMap<UUID, Deque<ProcedimentoContexto>> sessoes
            = new ConcurrentHashMap<>();

    private final SessaoPersistencePort sessaoPort;
    private final JsonMapper mapper;

    public void iniciarSessao(UUID idSessao) {
        sessoes.put(idSessao, new ArrayDeque<>());
    }

    public void empurrar(UUID idSessao, ProcedimentoContexto ctx) {
        Deque<ProcedimentoContexto> pilha = sessoes.computeIfAbsent(idSessao, _ -> new ArrayDeque<>());
        pilha.push(ctx);
        persistirSnapshot(idSessao, pilha);
    }

    public ProcedimentoContexto concluirFrame(UUID idSessao) {
        Deque<ProcedimentoContexto> pilha = exigirPilha(idSessao);
        pilha.pop();

        if (pilha.isEmpty()) {
            limparSnapshot(idSessao);
            return null;
        }

        persistirSnapshot(idSessao, pilha);
        return pilha.peek();
    }

    public ProcedimentoContexto frameAtivo(UUID idSessao) {
        Deque<ProcedimentoContexto> pilha = sessoes.get(idSessao);
        if (pilha == null || pilha.isEmpty())
            throw new IllegalStateException("Nenhum procedimento ativo na sessão " + idSessao);
        return pilha.peek();
    }

    public boolean temProcedimentoAtivo(UUID idSessao) {
        Deque<ProcedimentoContexto> pilha = sessoes.get(idSessao);
        return pilha != null && !pilha.isEmpty();
    }

    public int profundidade(UUID idSessao) {
        Deque<ProcedimentoContexto> pilha = sessoes.get(idSessao);
        return pilha == null ? 0 : pilha.size();
    }

    public void persistirSnapshot(UUID idSessao, Deque<ProcedimentoContexto> pilha) {
        try {
            SessaoSnapshot snapshot = new SessaoSnapshot();

            snapshot.setPilha(
                    pilha.stream()
                            .map(this::toSnapshot)
                            .collect(Collectors.toList())
            );

            Sessao sessao = sessaoPort.buscarPorId(idSessao).orElseThrow();
            String snapshotJson = mapper.writeValueAsString(snapshot);
            
            // Grava o snapshot JSON no agregado do domínio
            sessao.encerrarSessao(snapshotJson); 
            sessaoPort.salvar(sessao);

        } catch (Exception e) {
            log.error("Falha ao persistir snapshot da sessão {}: {}", idSessao, e.getMessage());
        }
    }

    private void limparSnapshot(UUID idSessao) {
        sessaoPort.buscarPorId(idSessao).ifPresent(s -> {
            sessaoPort.salvar(s);
        });
    }

    private ProcedimentoSnapshot toSnapshot(ProcedimentoContexto ctx) {
        ProcedimentoSnapshot snap = new ProcedimentoSnapshot();
        snap.setIdProcedimento(ctx.getProcedimento().getId());
        snap.setIdSessao(ctx.getIdSessao());
        snap.setEtapaAtual(ctx.getEtapaAtual());
        snap.setStatus(ctx.getStatus());
        snap.setContextoRetorno(ctx.getRetornoContexto());
        snap.setContextoMap(ctx.getContexto().dados());
        snap.setHistorico(ctx.getHistorico());

        switch (ctx.getEscopo()) {
            case EscopoInstancias.Nenhuma n -> {
                snap.setEscopoTipo("NENHUMA");
                snap.setEscopoIds(List.of());
            }
            case EscopoInstancias.Unica u -> {
                snap.setEscopoTipo("UNICA");
                snap.setEscopoIds(List.of(u.id()));
            }
            case EscopoInstancias.Multiplas m -> {
                snap.setEscopoTipo("MULTIPLAS");
                snap.setEscopoIds(m.ids());
            }
        }

        return snap;
    }

    private Deque<ProcedimentoContexto> exigirPilha(UUID idSessao) {
        Deque<ProcedimentoContexto> pilha = sessoes.get(idSessao);
        if (pilha == null || pilha.isEmpty())
            throw new IllegalStateException("Nenhum procedimento ativo na sessão " + idSessao);
        return pilha;
    }

    public void empurrarSemPersistir(UUID idSessao, ProcedimentoContexto ctx) {
        sessoes.computeIfAbsent(idSessao, id -> new ArrayDeque<>()).push(ctx);
    }
}