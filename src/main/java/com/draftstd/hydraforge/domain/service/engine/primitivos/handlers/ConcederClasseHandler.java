package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeRelacao;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeSistema;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeRelacaoOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeSistemaOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConcederClasseHandler implements EtapaHandler {

    private final EntidadeSistemaOutputPort entidadeSistemaPort;
    private final EntidadeInstanciaOutputPort instanciaPort;
    private final EntidadeRelacaoOutputPort relacaoPort;
    private final InstanciaResolver instanciaResolver;

    @Override
    public String tipoEtapa() { return "CONCEDER_CLASSE"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();

        UUID idCavaleiro = resolverIdCavaleiro(params, ctx);

        EntidadeSistema cavaleiro = entidadeSistemaPort.buscarPorId(idCavaleiro)
                .orElseThrow(() -> new IllegalArgumentException("Cavaleiro não encontrado: " + idCavaleiro));

        EntidadeInstancia personagem = instanciaResolver.retornarAtiva(ctx);
        Map<String, Object> propriedades = cavaleiro.getPropriedades();

        // 1. Vincula o próprio cavaleiro como classe (cria instância de classe a partir do template)
        vincularRelacao(personagem, cavaleiro, "CLASSE");

        // 2. Instancia e vincula os itens iniciais
        List<UUID> itensGranted = new ArrayList<>();
        if (propriedades != null && propriedades.containsKey("itens_iniciais")) {
            List<?> itensIniciais = (List<?>) propriedades.get("itens_iniciais");
            if (itensIniciais != null) {
                for (Object itemObj : itensIniciais) {
                    UUID idTemplate = converterParaUuid(itemObj);
                    EntidadeInstancia instanciaItem = instanciarDeTemplate(idTemplate, personagem);
                    vincularRelacao(personagem, instanciaItem, "ITEM");
                    itensGranted.add(instanciaItem.getId());
                }
            }
        }

        // 3. Instancia e vincula as habilidades
        List<UUID> habsGranted = new ArrayList<>();
        if (propriedades != null && propriedades.containsKey("habilidades")) {
            List<?> habilidades = (List<?>) propriedades.get("habilidades");
            if (habilidades != null) {
                for (Object habObj : habilidades) {
                    UUID idTemplate = converterParaUuid(habObj);
                    EntidadeInstancia instanciaHab = instanciarDeTemplate(idTemplate, personagem);
                    vincularRelacao(personagem, instanciaHab, "HABILIDADE");
                    habsGranted.add(instanciaHab.getId());
                }
            }
        }

        return ResultadoEtapa.concluida(Map.of(
                "cavaleiro_concedido", idCavaleiro,
                "itens_concedidos", itensGranted,
                "habilidades_concedidas", habsGranted
        ));
    }

    // ── Helpers ──────────────────────────────────────────────────

    private EntidadeInstancia instanciarDeTemplate(UUID idTemplate, EntidadeInstancia personagem) {
        EntidadeSistema template = entidadeSistemaPort.buscarPorId(idTemplate)
                .orElseThrow(() -> new IllegalArgumentException("Template não encontrado: " + idTemplate));

        EntidadeInstancia instancia = new EntidadeInstancia();
        instancia.setId(UUID.randomUUID());
        instancia.setEntidadeSistemaId(template.getId());
        instancia.setCampanhaId(personagem.getCampanhaId());
        instancia.setDonoId(personagem.getDonoId());
        instancia.setTipo(template.getTipo());
        instancia.setNome(template.getNome());
        instancia.setDescricao(template.getDescricao());
        instancia.setValoresAtributos(template.getAtributos() != null ? new HashMap<>(template.getAtributos()) : new HashMap<>());

        instanciaPort.salvar(instancia);
        return instancia;
    }

    private void vincularRelacao(EntidadeInstancia personagem, EntidadeInstancia filha, String tipo) {
        EntidadeRelacao rel = new EntidadeRelacao();
        rel.setId(UUID.randomUUID());
        rel.setEntidadePaiId(personagem.getId());
        rel.setEntidadeFilhaId(filha.getId());
        rel.setOrigem(tipo);
        rel.setQuantidade(1);
        relacaoPort.salvar(rel);
    }

    private void vincularRelacao(EntidadeInstancia personagem, EntidadeSistema template, String tipo) {
        EntidadeInstancia instanciaClasse = new EntidadeInstancia();
        instanciaClasse.setId(UUID.randomUUID());
        instanciaClasse.setEntidadeSistemaId(template.getId());
        instanciaClasse.setCampanhaId(personagem.getCampanhaId());
        instanciaClasse.setDonoId(personagem.getDonoId());
        instanciaClasse.setTipo(template.getTipo());
        instanciaClasse.setNome(template.getNome());
        instanciaClasse.setDescricao(template.getDescricao());
        instanciaClasse.setValoresAtributos(template.getAtributos() != null ? new HashMap<>(template.getAtributos()) : new HashMap<>());
        
        instanciaPort.salvar(instanciaClasse);

        EntidadeRelacao rel = new EntidadeRelacao();
        rel.setId(UUID.randomUUID());
        rel.setEntidadePaiId(personagem.getId());
        rel.setEntidadeFilhaId(instanciaClasse.getId());
        rel.setEntidadeSistemaFilhaId(template.getId());
        rel.setOrigem(tipo);
        rel.setQuantidade(1);
        relacaoPort.salvar(rel);
    }

    private UUID resolverIdCavaleiro(Map<String, Object> params, ExecucaoContexto ctx) {
        String chaveEscolhido = (String) params.getOrDefault("chave_escolhido", "id_cavaleiro_escolhido");
        String chaveSorteado  = (String) params.getOrDefault("chave_sorteado", "id_cavaleiro_sorteado");

        Object rawId = ctx.getContexto().get(chaveEscolhido, Object.class)
                .or(() -> ctx.getContexto().get(chaveSorteado, Object.class))
                .orElseThrow(() -> new IllegalStateException(
                        "Nenhum cavaleiro foi escolhido ou sorteado no contexto"));

        return rawId instanceof UUID u ? u : UUID.fromString(rawId.toString());
    }

    private UUID converterParaUuid(Object obj) {
        if (obj == null) return null;
        if (obj instanceof UUID u) return u;
        return UUID.fromString(obj.toString());
    }
}