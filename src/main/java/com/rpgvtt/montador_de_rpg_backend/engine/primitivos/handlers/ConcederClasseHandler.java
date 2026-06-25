package com.rpgvtt.montador_de_rpg_backend.engine.primitivos.handlers;

import com.rpgvtt.montador_de_rpg_backend.domain.model.entidade.EntidadeInstancia;
import com.rpgvtt.montador_de_rpg_backend.domain.model.entidade.EntidadeRelacao;
import com.rpgvtt.montador_de_rpg_backend.domain.model.entidade.EntidadeRelacaoKey;
import com.rpgvtt.montador_de_rpg_backend.domain.model.entidade.EntidadeSistema;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.InstanciaResolver;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ResultadoEtapa;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.EtapaExecutavel;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.EtapaHandler;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.ExecucaoContexto;
import com.rpgvtt.montador_de_rpg_backend.repository.entidade.EntidadeInstanciaRepository;
import com.rpgvtt.montador_de_rpg_backend.repository.entidade.EntidadeRelacaoRepository;
import com.rpgvtt.montador_de_rpg_backend.repository.entidade.EntidadeSistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ConcederClasseHandler implements EtapaHandler {

    private final EntidadeSistemaRepository entidadeSistemaRepo;
    private final EntidadeInstanciaRepository instanciaRepo;
    private final EntidadeRelacaoRepository relacaoRepo;
    private final InstanciaResolver instanciaResolver;

    @Override
    public String tipoEtapa() { return "CONCEDER_CLASSE"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        JsonNode params = etapa.getParametrosEtapa();

        Long idCavaleiro = resolverIdCavaleiro(params, ctx);

        EntidadeSistema cavaleiro = entidadeSistemaRepo.findById(idCavaleiro)
                .orElseThrow(() -> new IllegalArgumentException("Cavaleiro não encontrado: " + idCavaleiro));

        EntidadeInstancia personagem = instanciaResolver.retornarAtiva(ctx);
        JsonNode propriedades = cavaleiro.getPropriedades();

        // 1. Vincula o próprio cavaleiro como classe (referência ao template, não cria instância)
        vincularRelacao(personagem, cavaleiro, "CLASSE");

        // 2. Instancia e vincula os itens iniciais
        List<Long> itensGranted = new ArrayList<>();
        if (propriedades != null && propriedades.has("itens_iniciais")) {
            for (JsonNode idNode : propriedades.get("itens_iniciais")) {
                Long idTemplate = idNode.asLong();
                EntidadeInstancia instanciaItem = instanciarDeTemplate(idTemplate, personagem);
                vincularRelacao(personagem, instanciaItem, "ITEM");
                itensGranted.add(instanciaItem.getId());
            }
        }

        // 3. Instancia e vincula as habilidades
        List<Long> habsGranted = new ArrayList<>();
        if (propriedades != null && propriedades.has("habilidades")) {
            for (JsonNode idNode : propriedades.get("habilidades")) {
                Long idTemplate = idNode.asLong();
                EntidadeInstancia instanciaHab = instanciarDeTemplate(idTemplate, personagem);
                vincularRelacao(personagem, instanciaHab, "HABILIDADE");
                habsGranted.add(instanciaHab.getId());
            }
        }

        return ResultadoEtapa.concluida(Map.of(
                "cavaleiro_concedido", idCavaleiro,
                "itens_concedidos", itensGranted,
                "habilidades_concedidas", habsGranted
        ));
    }

    // ── Helpers ──────────────────────────────────────────────────

    /**
     * Cria uma EntidadeInstancia a partir de um EntidadeSistema (template).
     * Os atributosAtuais são copiados dos atributos do template,
     * permitindo que a instância evolua independentemente.
     */
    private EntidadeInstancia instanciarDeTemplate(Long idTemplate, EntidadeInstancia personagem) {
        EntidadeSistema template = entidadeSistemaRepo.findById(idTemplate)
                .orElseThrow(() -> new IllegalArgumentException("Template não encontrado: " + idTemplate));

        EntidadeInstancia instancia = new EntidadeInstancia();
        instancia.setEntidadeSistema(template);                          // era setSistema()
        instancia.setTipo(template.getTipo());
        instancia.setNome(template.getNome());
        instancia.setAtributosAtuais(template.getAtributos());

        return instanciaRepo.save(instancia);
    }

    private void vincularRelacao(EntidadeInstancia personagem, EntidadeInstancia filha, String tipo) {
        EntidadeRelacaoKey key = new EntidadeRelacaoKey(personagem.getId(), filha.getId());

        EntidadeRelacao rel = new EntidadeRelacao();
        rel.setId(key);
        rel.setEntidadePai(personagem);
        rel.setEntidadeFilha(filha);
        rel.setOrigem(tipo);                                             // era setTipo()
        rel.setQuantidade(1);
        relacaoRepo.save(rel);
    }

    private void vincularRelacao(EntidadeInstancia personagem, EntidadeSistema template, String tipo) {
        EntidadeInstancia instanciaClasse = new EntidadeInstancia();
        instanciaClasse.setEntidadeSistema(template);                    // era setSistema()
        instanciaClasse.setTipo(template.getTipo());
        instanciaClasse.setNome(template.getNome());
        instanciaClasse.setAtributosAtuais(template.getAtributos());
        instanciaClasse = instanciaRepo.save(instanciaClasse);

        EntidadeRelacaoKey key = new EntidadeRelacaoKey(personagem.getId(), instanciaClasse.getId());

        EntidadeRelacao rel = new EntidadeRelacao();
        rel.setId(key);
        rel.setEntidadePai(personagem);
        rel.setEntidadeFilha(instanciaClasse);
        rel.setEntidadeSistemaFilha(template);                           // aproveita o campo que já existe
        rel.setOrigem(tipo);                                             // era setTipo()
        rel.setQuantidade(1);
        relacaoRepo.save(rel);
    }

    private Long resolverIdCavaleiro(JsonNode params, ExecucaoContexto ctx) {
        String chaveEscolhido = params.path("chave_escolhido").asString("id_cavaleiro_escolhido");
        String chaveSorteado  = params.path("chave_sorteado").asString("id_cavaleiro_sorteado");

        return ctx.getContexto().getLong(chaveEscolhido)
                .or(() -> ctx.getContexto().getLong(chaveSorteado))
                .orElseThrow(() -> new IllegalStateException(
                        "Nenhum cavaleiro foi escolhido ou sorteado no contexto"));
    }
}
