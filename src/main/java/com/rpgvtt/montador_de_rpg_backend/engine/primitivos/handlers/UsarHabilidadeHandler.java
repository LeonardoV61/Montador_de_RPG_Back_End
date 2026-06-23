package com.rpgvtt.montador_de_rpg_backend.engine.primitivos.handlers;

import com.rpgvtt.montador_de_rpg_backend.domain.model.entidade.EntidadeInstancia;
import com.rpgvtt.montador_de_rpg_backend.domain.model.mecanica.EntidadeProcedimento;
import com.rpgvtt.montador_de_rpg_backend.domain.model.entidade.EntidadeSistema;
import com.rpgvtt.montador_de_rpg_backend.domain.model.sistema.Procedimento;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.*;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.*;
import com.rpgvtt.montador_de_rpg_backend.repository.mecanica.EntidadeProcedimentoRepository;
import com.rpgvtt.montador_de_rpg_backend.repository.entidade.EntidadeInstanciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsarHabilidadeHandler implements EtapaHandler {

    private final EntidadeInstanciaRepository instanciaRepo;
    private final EntidadeProcedimentoRepository entProcRepo;
    private final ChamarProcedimentoHandler chamarProcHandler;
    private final VerificarCondicaoHandler verificarCondicao;
    private final ObjectMapper mapper;

    @Override
    public String tipoEtapa() { return "USAR_HABILIDADE"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        // Parâmetros esperados:
        //   "id_instancia": Long   (instância da entidade habilidade)
        // Ou, se vier de outra forma, pode usar "id_entidade_procedimento"
        JsonNode params = etapa.getParametrosEtapa();
        Long idInstancia = params.get("id_instancia").asLong();

        EntidadeInstancia instanciaHabilidade = instanciaRepo.findById(idInstancia)
                .orElseThrow(() -> new IllegalArgumentException("Instância de habilidade não encontrada: " + idInstancia));

        // Busca a configuração de procedimento vinculada (pode ser pelo tipo da entidade ou por EntidadeProcedimento)
        // Estratégia: procuramos uma EntidadeProcedimento onde:
        //   entidadeInstancia == instanciaHabilidade (ou entidadeSistema == instanciaHabilidade.getEntidadeSistema())
        // Escolha: prefiro buscar por entidadeSistema para abranger todas as instâncias daquela habilidade,
        // mas com condição você pode sobrescrever. Aqui usaremos a mais direta: pela instância.
        Optional<EntidadeProcedimento> optEntProc = entProcRepo
                .findByEntidadeInstancia(instanciaHabilidade);
        // Se não houver, tenta pela entidadeSistema
        if (optEntProc.isEmpty()) {
            EntidadeSistema sistema = instanciaHabilidade.getEntidadeSistema();
            if (sistema != null) {
                // Busca qualquer EntidadeProcedimento que use essa entidadeSistema e que tenha processamento = "PARCIAL" ou "AUTOMATICO"
                optEntProc = entProcRepo.findByEntidadeSistemaAndProcessamentoNot(
                        sistema, "NARRATIVO"); // exemplo
            }
        }

        if (optEntProc.isEmpty()) {
            return ResultadoEtapa.erro("Nenhum procedimento definido para esta habilidade");
        }

        EntidadeProcedimento entProc = optEntProc.get();
        JsonNode condicao = entProc.getCondicao();

        // Verifica condição
        if (condicao != null && !condicao.isNull() && condicao.size() > 0) {
            ResultadoEtapa resultadoCondicao = verificarCondicao.executar(
                    etapa, ctx, condicao); // passamos o JsonNode da condição
            if (!(resultadoCondicao instanceof ResultadoEtapa.Concluida)) {
                return resultadoCondicao; // propaga erro ou recurso indisponível
            }
        }

        // Agora inicia o procedimento filho
        // Vamos construir um mapa de parâmetros no formato esperado pelo ChamarProcedimentoHandler
        Map<String, Object> paramsChamada = Map.of(
                "id_procedimento", entProc.getProcedimento().getId(),
                "salvar_em", "resultado_habilidade",
                "escopo", "HERDAR",
                "passar_contexto", List.of("executor_id", "alvo_id") // ou as chaves relevantes
        );

        // Cria uma etapa fake do tipo CHAMAR_PROCEDIMENTO
        EtapaProcedimento etapaChamada = new EtapaProcedimento();
        etapaChamada.setParametrosEtapa(mapper.valueToTree(paramsChamada));

        // Chama o handler diretamente (agora que ele implementa a interface corretamente)
        return chamarProcHandler.executar(etapaChamada, (ProcedimentoContexto) ctx);
    }
}