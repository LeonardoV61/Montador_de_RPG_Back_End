package com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto;

import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EscopoInstancias;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Contexto mínimo para rodar UM handler fora de qualquer procedimento real —
 * usado por PrimitivoExecutor ao disparar gatilhos de item, efeitos manuais
 * do mestre, ou qualquer "primitivo avulso".
 *
 * Convenção: a "instância ativa" é sempre o EXECUTOR (quem realiza a ação).
 * O alvo nunca é inferido daqui — PrimitivoExecutor injeta "id_entidade"
 * explicitamente nos parâmetros antes de chamar o handler, seguindo o mesmo
 * padrão de override que AlterarAtributoHandler e CalcularAtributoHandler
 * já usam para procedimentos reais.
 */

public class ContextoAvulso implements ExecucaoContexto {

    private final Sistema sistema;
    private final ContextoAccessor contexto;
    private EscopoInstancias escopo;

    public ContextoAvulso(Sistema sistema, EntidadeInstancia executor, EntidadeInstancia alvo) {
        this.sistema = sistema;

        Map<String, Object> seed = new HashMap<>();
        if (executor != null && executor.getId() != null) {
            seed.put("executor_id", executor.getId());
        }
        if (alvo != null && alvo.getId() != null) {
            seed.put("alvo_id", alvo.getId());
        }
        this.contexto = new ContextoAccessor(seed);

        UUID executorId = (executor != null) ? executor.getId() : null;
        this.escopo = EscopoInstancias.unica(executorId);
    }

    @Override public ContextoAccessor getContexto()  { return contexto; }
    @Override public Sistema getSistema()           { return sistema; }
    @Override public UUID getIdSistema()            { return sistema.getId(); }
    @Override public EscopoInstancias getEscopo()    { return escopo; }
    @Override public void setEscopo(EscopoInstancias escopo) { this.escopo = escopo; }
    @Override public boolean semInstancias()        { return false; }
    @Override public boolean temInstanciaUnica()    { return true; }
    @Override public Procedimento getProcedimento() { return null; }
    
    @Override
    public UUID idInstanciaAtiva() {
        if (escopo instanceof EscopoInstancias.Unica unica) {
            return unica.id();
        }
        return null;
    }

    @Override
    public List<UUID> idsInstancias() {
        UUID idAtivo = idInstanciaAtiva();
        return idAtivo != null ? List.of(idAtivo) : List.of();
    }
}