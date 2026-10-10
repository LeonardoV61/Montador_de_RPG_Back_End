package com.draftstd.hydraforge.domain.service.engine.procedimentos.snapshot;

import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ContextoAccessor;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoContexto.Status;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcedimentoSnapshot {

    private Long idProcedimento;
    private Long idSessao;
    private int etapaAtual;
    private Status status;
    private String contextoRetorno;

    private Map<String, Object> contextoMap = new HashMap<>();

    private String escopoTipo;
    private List<Long> escopoIds;

    @Getter(AccessLevel.NONE)
    private ContextoAccessor contexto;

    public void inicializarAcessor() {
        this.contexto = new ContextoAccessor(contextoMap);
    }

    public ContextoAccessor getContexto() {
        if (contexto == null) contexto = new ContextoAccessor(contextoMap);
        return contexto;
    }

    public void setContextoMap(Map<String, Object> map) {
        this.contextoMap = map;
        this.contexto = new ContextoAccessor(map);
    }

    private List<ResultadoEtapa> historico = new ArrayList<>();
}