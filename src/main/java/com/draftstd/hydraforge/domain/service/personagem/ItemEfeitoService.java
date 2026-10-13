package com.draftstd.hydraforge.domain.service.personagem;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.adapters.out.repository.entidade.EntidadeInstanciaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemEfeitoService {

    private final EntidadeInstanciaRepository entidadeInstanciaRepository;

    /**
     * Dispara gatilhos ou efeitos associados a um item (ex: AO_EQUIPAR, AO_DESEQUIPAR, AO_USAR).
     * Totalmente adaptado para suportar IDs baseados em UUID e propriedades em JSON.
     */
    public void dispararGatilho(EntidadeInstancia item, String tipoGatilho, 
                                EntidadeInstancia personagem, EntidadeInstancia alvo, 
                                Map<String, Object> contextoExtra) {
        if (item == null || item.getEntidadeSistema() == null) {
            return;
        }

        JsonNode propriedades = item.getEntidadeSistema().getPropriedades();
        if (propriedades == null || propriedades.isMissingNode()) {
            return;
        }

        // Exemplo de integração: verifica se o item possui o gatilho configurado nas propriedades
        JsonNode gatilhos = propriedades.path("gatilhos");
        if (gatilhos != null && gatilhos.has(tipoGatilho)) {
            log.info("Disparando gatilho '{}' para o item {} na entidade {}", 
                    tipoGatilho, item.getId(), personagem.getId());
            
            // Aqui você pode estender a lógica para interpretar efeitos dinâmicos,
            // alterar atributos ou invocar procedimentos associados ao efeito do item.
        }
    }
}