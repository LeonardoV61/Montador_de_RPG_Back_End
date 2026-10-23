package com.draftstd.hydraforge.domain.service.engine.procedimentos.snapshot;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EscopoInstancias;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.LoadRequest;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoLoader;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.SessaoContexto;
import com.draftstd.hydraforge.ports.out.persistence.sessao.SessaoPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessaoRehydrator {

    private final SessaoPersistencePort sessaoPort;
    private final SessaoContexto sessaoContexto;
    private final ProcedimentoLoader loader;
    private final JsonMapper mapper;

    @EventListener(ApplicationReadyEvent.class)
    public void rehidratar() {
        List<Sessao> sessoesAtivas = sessaoPort.buscarPorStatusEProcedimentoAtivoNaoNulo(StatusSessao.ATIVA);

        log.info("Retornando {} sessões ativas após reinício", sessoesAtivas.size());

        for (Sessao sessao : sessoesAtivas) {
            try {
                rehidratarSessao(sessao);
            } catch (Exception e) {
                log.error("Falha ao retornar sessão {}: {}",
                        sessao.getId(), e.getMessage());
                if (sessao.getStatus() == StatusSessao.ATIVA) {
                    sessao.pausarSessao();
                }
                sessaoPort.salvar(sessao);
            }
        }
    }

    private void rehidratarSessao(Sessao sessao) throws Exception {
        String jsonSnapshot = sessao.getSessaoSnapshotJson();
        if (jsonSnapshot == null || jsonSnapshot.isBlank()) {
            log.warn("Sessão {} não possui snapshot de execução para reidratação", sessao.getId());
            return;
        }

        SessaoSnapshot snapshot = mapper.readValue(jsonSnapshot, SessaoSnapshot.class);
        List<ProcedimentoSnapshot> procSnapshots = snapshot.getPilha();

        Deque<ProcedimentoContexto> pilha = new ArrayDeque<>();
        ProcedimentoContexto frameAbaixo = null;
        for (int i = procSnapshots.size() - 1; i >= 0; i--) {
            ProcedimentoSnapshot snap = procSnapshots.get(i);
            EscopoInstancias escopo = rehidratarEscopo(snap);

            ProcedimentoContexto ctx;

            if (frameAbaixo == null) {
                // Root frame
                ctx = loader.carregar(LoadRequest.raiz(
                        snap.getIdProcedimento(),
                        snap.getIdSessao(),
                        escopo
                ));
            } else {
                // Child frame — herda participantes e sistema do pai
                ctx = loader.carregar(LoadRequest.filho(
                        snap.getIdProcedimento(),
                        frameAbaixo,
                        escopo,
                        snap.getContextoRetorno(),
                        snap.getContexto().dados() // restaurado do snapshot
                ));
            }

            // Restaura cursor e estado do pipeline
            ctx.setEtapaAtual(snap.getEtapaAtual());
            ctx.setStatus(snap.getStatus());
            ctx.setContextoMap(snap.getContexto().dados());
            ctx.setHistorico(snap.getHistorico());

            pilha.push(ctx);
            frameAbaixo = ctx;
        }

        // Registra a pilha reconstruída na sessão
        Deque<ProcedimentoContexto> pilhaOrdenada = new ArrayDeque<>();
        new ArrayList<>(pilha).forEach(pilhaOrdenada::addLast);

        pilha.forEach(frame ->
                sessaoContexto.empurrarSemPersistir(sessao.getId(), frame));

        log.info("Sessão {} rehydratada com {} frames na pilha",
                sessao.getId(), procSnapshots.size());
    }

    private EscopoInstancias rehidratarEscopo(ProcedimentoSnapshot snap) {
        return switch (snap.getEscopoTipo()) {
            case "NENHUMA"   -> EscopoInstancias.nenhuma();
            case "UNICA"     -> EscopoInstancias.unica(snap.getEscopoIds().get(0));
            case "MULTIPLAS" -> EscopoInstancias.multiplas(snap.getEscopoIds());
            default -> throw new IllegalStateException(
                    "Tipo de escopo desconhecido: " + snap.getEscopoTipo());
        };
    }
}