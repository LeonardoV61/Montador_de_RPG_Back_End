package com.rpgvtt.montador_de_rpg_backend.service.importacao;

import com.rpgvtt.montador_de_rpg_backend.dto.entidade.EntidadeSistemaCreateDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.entidade.EntidadeSistemaResponseDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.importacao.DefinicaoDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.importacao.ImportacaoRequestDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.mecanica.ResolucaoCreateDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.sistema.EtapaProcedimentoCreateDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.sistema.ProcedimentoCreateDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.sistema.ProcedimentoResponseDTO;
import com.rpgvtt.montador_de_rpg_backend.service.entidade.EntidadeSistemaService;
import com.rpgvtt.montador_de_rpg_backend.service.mecanica.ResolucaoService;
import com.rpgvtt.montador_de_rpg_backend.service.sistema.ProcedimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
// import tools.jackson.databind.node.TextNode;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImportacaoService {

    private final EntidadeSistemaService entidadeService;
    private final ProcedimentoService procedimentoService;
    private final ResolucaoService resolucaoService;
    private final JsonMapper mapper;

    @Transactional
    public Map<String, Long> processar(ImportacaoRequestDTO request) {
        Map<String, Long> aliasParaId = new HashMap<>();

        // Fase 1: entidades SEM aliases (itens, montarias)
        for (DefinicaoDTO def : request.definicoes()) {
            if ("entidade".equals(def.tipo()) && !contemAliases(def.dados())) {
                aliasParaId.put(def.alias(), criarEntidade(def.dados(), aliasParaId));
            }
        }

        // Fase 2: procedimentos SEM aliases (sub-procs, proc_conceder_classe)
        for (DefinicaoDTO def : request.definicoes()) {
            if ("procedimento".equals(def.tipo()) && !contemAliases(def.dados())) {
                aliasParaId.put(def.alias(), criarProcedimento(def.dados(), aliasParaId));
            }
        }

        // Fase 3: resoluções SEM aliases (resolucao_moeda)
        for (DefinicaoDTO def : request.definicoes()) {
            if ("resolucao".equals(def.tipo()) && !contemAliases(def.dados())) {
                aliasParaId.put(def.alias(), criarResolucao(def.dados(), aliasParaId));
            }
        }

        // Fase 4: entidades COM aliases (habilidades, cavaleiros)
        for (DefinicaoDTO def : request.definicoes()) {
            if ("entidade".equals(def.tipo()) && contemAliases(def.dados())) {
                aliasParaId.put(def.alias(), criarEntidade(def.dados(), aliasParaId));
            }
        }

        // Fase 5: resoluções COM aliases (resolucao_cavaleiros)
        for (DefinicaoDTO def : request.definicoes()) {
            if ("resolucao".equals(def.tipo()) && contemAliases(def.dados())) {
                aliasParaId.put(def.alias(), criarResolucao(def.dados(), aliasParaId));
            }
        }

        // Fase 6: procedimentos COM aliases (proc_criacao_personagem)
        for (DefinicaoDTO def : request.definicoes()) {
            if ("procedimento".equals(def.tipo()) && contemAliases(def.dados())) {
                aliasParaId.put(def.alias(), criarProcedimento(def.dados(), aliasParaId));
            }
        }

        return aliasParaId;
    }

    // ── Métodos de criação ─────────────────────────────────────────

    private Long criarEntidade(JsonNode dados, Map<String, Long> aliases) {
        JsonNode resolvido = resolverAliases(dados, aliases);
        resolvido = garantirAtributos(resolvido);
        resolvido = garantirPropriedades(resolvido);
        EntidadeSistemaCreateDTO dto = mapper.convertValue(resolvido, EntidadeSistemaCreateDTO.class);
        EntidadeSistemaResponseDTO criado = entidadeService.criar(dto);
        return criado.id();
    }

    private Long criarProcedimento(JsonNode dados, Map<String, Long> aliases) {
        JsonNode resolvido = resolverAliases(dados, aliases);
        resolvido = garantirConfigsGeral(resolvido);
        ProcedimentoCreateDTO dto = mapper.convertValue(resolvido, ProcedimentoCreateDTO.class);
        ProcedimentoResponseDTO criado = procedimentoService.criar(dto);

        // Salva as etapas, se existirem
        JsonNode etapasNode = resolvido.get("etapas");
        if (etapasNode != null && etapasNode.isArray()) {
            for (JsonNode etapaNode : etapasNode) {
                EtapaProcedimentoCreateDTO etapaDto = mapper.convertValue(etapaNode, EtapaProcedimentoCreateDTO.class);
                procedimentoService.adicionarEtapa(criado.id(), etapaDto);
            }
        }

        return criado.id();
    }

    private Long criarResolucao(JsonNode dados, Map<String, Long> aliases) {
        JsonNode resolvido = resolverAliases(dados, aliases);
        ResolucaoCreateDTO dto = mapper.convertValue(resolvido, ResolucaoCreateDTO.class);
        return resolucaoService.criar(dto).id();
    }

    // ── Resolução de aliases ──────────────────────────────────────

    /**
     * Varre recursivamente o JsonNode e substitui qualquer string que seja um alias
     * (começa com "@") pelo ID correspondente (como Long). Se o alias não estiver
     * resolvido, mantém a string original.
     */
    private JsonNode resolverAliases(JsonNode node, Map<String, Long> aliases) {
        if (node.isString()) {
            String text = node.asString();
            if (text.startsWith("@") && aliases.containsKey(text)) {
                return mapper.getNodeFactory().numberNode(aliases.get(text));
            }
            return node;
        }

        if (node.isArray()) {
            ArrayNode array = mapper.createArrayNode();
            for (JsonNode item : node) {
                array.add(resolverAliases(item, aliases));
            }
            return array;
        }

        if (node.isObject()) {
            ObjectNode obj = mapper.createObjectNode();
            for (Map.Entry<String, JsonNode> field : node.properties()) {
                obj.set(field.getKey(), resolverAliases(field.getValue(), aliases));
            }
            return obj;
        }

        // Números, booleanos, nulos → mantém como está
        return node;
    }

    // ── Detecção de dependências & Privados ──────────────────────────────────

    /**
     * Retorna true se o JsonNode contiver alguma string que comece com "@",
     * indicando que a definição depende de outra.
     */
    private boolean contemAliases(JsonNode node) {
        if (node.isString()) {
            return node.asString().startsWith("@");
        }
        if (node.isArray() || node.isObject()) {
            for (JsonNode child : node) {
                if (contemAliases(child)) return true;
            }
        }
        return false;
    }

    private JsonNode garantirAtributos(JsonNode dados) {
        if (dados.has("atributos") && !dados.get("atributos").isNull()) {
            return dados;
        }
        // Cria um objeto vazio para "atributos"
        ObjectNode comAtributos = (ObjectNode) dados.deepCopy();
        comAtributos.set("atributos", mapper.createObjectNode());
        return comAtributos;
    }

    private JsonNode garantirPropriedades(JsonNode dados) {
        if (dados.has("propriedades") && !dados.get("propriedades").isNull()) {
            return dados;
        }
        ObjectNode comPropriedades = (ObjectNode) dados.deepCopy();
        comPropriedades.set("propriedades", mapper.createObjectNode());
        return comPropriedades;
    }

    private JsonNode garantirConfigsGeral(JsonNode dados) {
        if (dados.has("configsGeral") && !dados.get("configsGeral").isNull()) {
            return dados;
        }
        ObjectNode comConfig = (ObjectNode) dados.deepCopy();
        comConfig.set("configsGeral", mapper.createObjectNode());
        return comConfig;
    }


}