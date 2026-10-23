package com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto;

import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.domain.service.engine.exceptions.EntityNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoContexto.Status;
import com.draftstd.hydraforge.ports.out.persistence.personagem.ObterPersonagemPort;
import com.draftstd.hydraforge.ports.out.persistence.sessao.SessaoPersistencePort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EtapaProcedimentoOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.ProcedimentoOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.ObterSistemaPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcedimentoLoader {

    private final ProcedimentoOutputPort procedimentoPort;
    private final EtapaProcedimentoOutputPort etapaPort;
    private final ObterPersonagemPort personagemPort;
    private final SessaoPersistencePort sessaoPort;
    private final ObterSistemaPort sistemaPort;

    public ProcedimentoContexto carregar(LoadRequest req) {

        Procedimento proc = procedimentoPort
                .buscarPorId(req.idProcedimento())
                .orElseThrow(() -> new EntityNotFoundException(
                        Procedimento.class, req.idProcedimento()
                ));

        List<EtapaProcedimento> etapas = etapaPort
                .buscarPorProcedimentoIdOrdenado(req.idProcedimento());

        etapas.sort(Comparator.comparing(EtapaProcedimento::getOrdem));
        
        // Obtém o Sistema a partir do ID contido em Procedimento
        Sistema sistema = sistemaPort.buscarPorId(proc.getSistemaId())
                .orElseGet(() -> Sistema.reconstituirChave(proc.getSistemaId()));

        // Participantes: reutiliza a lista do pai se for um frame filho;
        // caso contrário, obtém a campanha através da sessão para listar os personagens.
        List<Personagem> participantes;
        if (req.participantesHerdados() != null) {
            participantes = req.participantesHerdados();
        } else if (req.idSessao() != null) {
            Sessao sessao = sessaoPort.buscarPorId(req.idSessao())
                    .orElseThrow(() -> new EntityNotFoundException(Sessao.class, req.idSessao()));
            participantes = personagemPort.listarPorCampanhaId(sessao.getCampanhaId());
        } else {
            participantes = List.of();
        }

        ProcedimentoContexto ctx = new ProcedimentoContexto();
        ctx.setProcedimento(proc);
        ctx.setEtapas(etapas);
        ctx.setSistema(sistema);
        ctx.setParticipantes(participantes);
        ctx.setEscopo(req.escopo());
        ctx.setIdSessao(req.idSessao());
        ctx.setRetornoContexto(req.retornoContexto());
        ctx.setEtapaAtual(0);
        ctx.setStatus(Status.EM_ANDAMENTO);
        ctx.setContextoMap(new HashMap<>(req.contextoInicial()));

        return ctx;
    }
}