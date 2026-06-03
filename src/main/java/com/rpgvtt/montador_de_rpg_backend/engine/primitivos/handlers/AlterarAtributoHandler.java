package com.rpgvtt.montador_de_rpg_backend.engine.primitivos.handlers;

import com.rpgvtt.montador_de_rpg_backend.domain.model.entidade.EntidadeInstancia;
import com.rpgvtt.montador_de_rpg_backend.domain.model.sistema.EtapaProcedimento;
import com.rpgvtt.montador_de_rpg_backend.engine.exceptions.EntityNotFoundException;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.EtapaHandler;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.InstanciaResolver;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ProcedimentoContexto;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ResultadoEtapa;
import com.rpgvtt.montador_de_rpg_backend.repository.entidade.EntidadeInstanciaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlterarAtributoHandler implements EtapaHandler {

    private JsonMapper mapper;
    private InstanciaResolver instanciaResolver;
    private EntidadeInstanciaRepository instanciaRepo;

    @Override
    public String tipoEtapa() {
        return "ALTERAR_ATRIBUTO";
    }

    @Override
    public ResultadoEtapa executar(EtapaProcedimento etapa, ProcedimentoContexto ctx) {

        Map<String, Object> params = mapper.convertValue(etapa.getParametros_etapa(), new TypeReference<>() {});

        String atributo = params.get("atributo").toString();
        Integer qtd = (Integer) params.get("quantidade");
        Long idEntidade = (Long) params.get("id_entidade"); // opcional
        String op = params.getOrDefault("operacao", "").toString(); // soma, sub, div, mult (opcional)

        EntidadeInstancia inst;

        if (idEntidade == null) {
            inst = instanciaResolver.retornarAtiva(ctx);
        } else {
            inst = instanciaRepo.findById(idEntidade)
                    .orElseThrow(() -> new EntityNotFoundException(EntidadeInstancia.class, idEntidade));
        }



        inst.getAtributosAtuais();


        if (!op.isEmpty()) {
            double result = switch (op) {
                case "soma" -> atributo + qtd;
                case "sub" ->
            }
        }

        return null;
    }
}


