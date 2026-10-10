package com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto;

import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.domain.service.engine.exceptions.EntityNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoContexto.Status;
import com.draftstd.hydraforge.adapters.out.repository.personagem.PersonagemRepository;
import com.draftstd.hydraforge.adapters.out.repository.sistema.EtapaProcedimentoRepository;
import com.draftstd.hydraforge.adapters.out.repository.sistema.ProcedimentoRepository;
import com.draftstd.hydraforge.adapters.out.repository.sistema.SistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ProcedimentoLoader {

    private final ProcedimentoRepository procedimentoRepo;
    private final EtapaProcedimentoRepository etapaRepo;
    private final SistemaRepository sistemaRepo;
    private final PersonagemRepository personagemRepo;

    /**
     * Builds a root ProcedimentoContexto from a LoadRequest.
     * Called by ProcedimentoEngine.iniciar() and by ChamarProcedimentoHandler
     * when pushing a child frame.
     */
    public ProcedimentoContexto carregar(LoadRequest req) {

        Procedimento proc = procedimentoRepo
                .findById(req.idProcedimento())
                .orElseThrow(() -> new EntityNotFoundException(
                        Procedimento.class, req.idProcedimento()
                ));

        List<EtapaProcedimento> etapas = etapaRepo
                .findByProcedimentoIdOrderByOrdem(req.idProcedimento());

        etapas.sort(Comparator.comparing(EtapaProcedimento::getOrdem));
        
        Sistema sistema = proc.getSistema();

        // Participants: reuse parent's list if this is a child frame,
        // query DB only for root procedures
        List<Personagem> participantes = req.participantesHerdados() != null
                ? req.participantesHerdados()
                : personagemRepo.findByCampanha_Sessoes_Id(req.idSessao());

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
        ctx.setContextoMap(new HashMap<>(req.contextoInicial())); // copy, not ref

        return ctx;
    }
}