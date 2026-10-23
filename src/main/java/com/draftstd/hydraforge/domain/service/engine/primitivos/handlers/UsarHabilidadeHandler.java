package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeProcedimento;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeProcedimentoOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.ProcedimentoOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UsarHabilidadeHandler implements EtapaHandler {

    private final EntidadeInstanciaOutputPort instanciaPort;
    private final EntidadeProcedimentoOutputPort entProcPort;
    private final ProcedimentoOutputPort procedimentoPort;
    private final ChamarProcedimentoHandler chamarProcHandler;
    private final VerificarCondicaoHandler verificarCondicao;
    private final JsonMapper mapper;

    @Override
    public String tipoEtapa() { return "USAR_HABILIDADE"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();
        
        Object rawId = params.get("id_instancia");
        UUID idInstancia = rawId instanceof UUID u ? u : UUID.fromString(rawId.toString());

        EntidadeInstancia instanciaHabilidade = instanciaPort.buscarPorId(idInstancia)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Instância de habilidade não encontrada: " + idInstancia));

        // Busca a EntidadeProcedimento associada à instância através do seu ID na porta de saída
        EntidadeProcedimento entProc = entProcPort.buscarPorEntidadeInstanciaId(instanciaHabilidade.getId())
                .orElse(null);

        if (entProc == null || entProc.getProcedimentoId() == null) {
            return ResultadoEtapa.erro("Nenhum procedimento definido para esta habilidade");
        }

        // Valida existência do procedimento no sistema
        UUID procedimentoId = entProc.getProcedimentoId();
        procedimentoPort.buscarPorId(procedimentoId)
                .orElseThrow(() -> new IllegalArgumentException("Procedimento não encontrado: " + procedimentoId));

        Map<String, Object> condicaoMap = entProc.getCondicao();
        JsonNode condicao = condicaoMap != null && !condicaoMap.isEmpty() ? mapper.valueToTree(condicaoMap) : null;
        boolean temCondicao = condicao != null && !condicao.isNull() && !condicao.isEmpty();
        
        if (temCondicao) {
            ResultadoEtapa resultadoCondicao = verificarCondicao.verificar(condicao, ctx);
            
            if (resultadoCondicao.tipo() != ResultadoEtapa.Tipo.CONCLUIDA) {
                return resultadoCondicao;
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> dados = (Map<String, Object>) resultadoCondicao.dados();
            String status = dados != null ? (String) dados.get("status") : null;
            if (!"condicao_ok".equals(status) && !"sem_condicao".equals(status)) {
                return resultadoCondicao;
            }
        }

        Map<String, Object> paramsChamada = Map.of(
                "id_procedimento", procedimentoId,
                "salvar_em", "resultado_habilidade",
                "escopo", "HERDAR",
                "passar_contexto", List.of("executor_id", "alvo_id")
        );

        EtapaProcedimento etapaChamada = new EtapaProcedimento(
                UUID.randomUUID(), 
                null, 
                1, 
                "SubEtapaChamarProcedimentoHabilidade", 
                "CHAMAR_PROCEDIMENTO", 
                paramsChamada, 
                true
        );

        if (!(ctx instanceof ProcedimentoContexto procedimentoCtx)) {
            return ResultadoEtapa.erro("USAR_HABILIDADE requer um procedimento ativo");
        }

        return chamarProcHandler.executar(etapaChamada, procedimentoCtx);
    }
}