package com.draftstd.hydraforge.domain.service.engine.procedimentos;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import com.draftstd.hydraforge.domain.service.engine.primitivos.HandlerRegistry;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.*;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EscopoInstancias;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcedimentoEngine {

    private final SessaoContexto sessaoContexto;
    private final ProcedimentoLoader loader;
    private final HandlerRegistry registry;
    private final InstanciaResolver instanciaResolver;

    public ProcedimentoContexto getContextoAtivo(UUID idSessao) {
        return null;
    }

    public ProcedimentoContexto iniciarComInstancia(UUID idProcedimento, UUID idSessao, UUID idInstancia) {
        ProcedimentoContexto raiz = loader.carregar(
                LoadRequest.raiz(idProcedimento, idSessao, EscopoInstancias.unica(idInstancia)));
        sessaoContexto.empurrar(idSessao, raiz);
        return avancar(idSessao);
    }

    public ProcedimentoContexto iniciarComMultiplos(UUID idProcedimento, UUID idSessao, List<UUID> ids) {
        return iniciarComMultiplos(idProcedimento, idSessao, ids, Map.of());
    }

    public ProcedimentoContexto iniciarComMultiplos(UUID idProcedimento, UUID idSessao,
                                                    List<UUID> ids, Map<String, Object> contextoInicial) {
        ProcedimentoContexto raiz = loader.carregar(
                LoadRequest.raiz(idProcedimento, idSessao, EscopoInstancias.multiplas(ids), contextoInicial));
        sessaoContexto.empurrar(idSessao, raiz);
        return avancar(idSessao);
    }

    public ProcedimentoContexto iniciarSemInstancia(UUID idProcedimento, UUID idSessao) {
        return iniciarSemInstancia(idProcedimento, idSessao, Map.of());
    }

    public ProcedimentoContexto iniciarSemInstancia(UUID idProcedimento, UUID idSessao,
                                                    Map<String, Object> contextoInicial) {
        ProcedimentoContexto raiz = loader.carregar(
                LoadRequest.raiz(idProcedimento, idSessao, EscopoInstancias.nenhuma(), contextoInicial));
        sessaoContexto.empurrar(idSessao, raiz);
        return avancar(idSessao);
    }

    public ProcedimentoContexto responder(UUID idSessao, Object valor) {
        ProcedimentoContexto frame = sessaoContexto.frameAtivo(idSessao);
        EtapaProcedimento etapaPendente = frame.getEtapaPendente();
        if (etapaPendente == null) {
            throw new IllegalStateException("Nenhuma etapa aguardando input");
        }

        JsonNode params = etapaPendente.getParametrosEtapa();
        String chave = params.path("salvar_em").asString();

        // Armazena o valor como veio (String, Integer etc.)
        frame.getContexto().put(chave, valor);
        frame.setAguardandoInput(false);
        frame.setEtapaPendente(null);

        // NÃO avança manualmente – o avancar() fará isso ao reprocessar a etapa
        return avancar(idSessao);
    }

    private ProcedimentoContexto avancar(UUID idSessao) {
        while (sessaoContexto.temProcedimentoAtivo(idSessao)) {
            ProcedimentoContexto frame = sessaoContexto.frameAtivo(idSessao);

            if (frame.etapasConcluidas()) {
                frame.setStatus(ProcedimentoContexto.Status.CONCLUIDO);
                ProcedimentoContexto pai = sessaoContexto.concluirFrame(idSessao);
                if (pai == null) return frame;

                if (frame.getRetornoContexto() != null) {
                    pai.getContexto().put(frame.getRetornoContexto(), frame.getHistorico());
                }
                pai.avancarEtapa();
                continue;
            }

            EtapaProcedimento etapa = frame.etapaCorrente();

            if (!etapa.isObrigatorio() && naoDisponivel(etapa, frame)) {
                frame.pularEtapa(etapa.getNome() + " — not available");
                continue;
            }

            EtapaHandler handler = registry.get(etapa.getTipoEtapa());
            if (handler == null) {
                frame.pularEtapa("handler not found: " + etapa.getTipoEtapa());
                continue;
            }

            ResultadoEtapa resultado = handler.executar(etapa, frame);
            frame.getHistorico().add(resultado);

            switch (resultado.tipo()) {

                case CONCLUIDA -> {
                    frame.avancarEtapa();
                }

                case AGUARDANDO_INPUT, AGUARDANDO_INPUT_MULTIPLO -> {
                    frame.setStatus(ProcedimentoContexto.Status.AGUARDANDO_INPUT_MULTIPLO);
                    frame.setAguardandoInput(true);
                    frame.setEtapaPendente(etapa);
                    return frame;
                }

                case SUB_PROCEDIMENTO_INICIADO -> {
                    // Child frame was already pushed by ChamarProcedimentoHandler.
                }

                case REPETINDO -> {
                    // etapaAtual already rewritten.
                }

                case PULAR_ETAPAS -> {
                    int n = resultado.pularQuantidade() != null ? resultado.pularQuantidade() : 0;
                    for (int i = 0; i < n && !frame.etapasConcluidas(); i++) {
                        frame.pularEtapa("skipped by previous etapa");
                    }
                }

                case ERRO -> {
                    frame.setStatus(ProcedimentoContexto.Status.ERRO);
                    return frame;
                }
            }
        }
        return null;
    }

    private boolean naoDisponivel(EtapaProcedimento etapa, ProcedimentoContexto frame) {
        JsonNode params = etapa.getParametrosEtapa();
        String requerRecurso = params.path("requer_recurso").asString(null);
        if (requerRecurso == null) return false;
        if (frame.semInstancias()) return true;

        EntidadeInstancia instancia = instanciaResolver.retornarAtiva(frame);
        JsonNode val = instancia.getAtributosAtuais().path(requerRecurso);
        if (val.isBoolean()) return !val.asBoolean();
        if (val.isNumber())  return val.asLong() <= 0;
        return true;
    }
}