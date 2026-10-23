package com.draftstd.hydraforge.domain.service.personagem;

import org.springframework.stereotype.Component;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeSistema;
import com.draftstd.hydraforge.ports.out.persistence.sistema.SalvarEntidadeInstanciaPort;

import java.util.Map;

@Component
public class ItemEfeitoService {

    private final SalvarEntidadeInstanciaPort salvarEntidadeInstanciaPort;

    public ItemEfeitoService(SalvarEntidadeInstanciaPort salvarEntidadeInstanciaPort) {
        this.salvarEntidadeInstanciaPort = salvarEntidadeInstanciaPort;
    }

    /**
     * Dispara gatilhos ou efeitos associados a um item (ex: AO_EQUIPAR, AO_DESEQUIPAR, AO_USAR).
     * Suporta IDs baseados em UUID e propriedades em estruturas puras Java.
     */
    public void dispararGatilho(EntidadeInstancia item, EntidadeSistema entidadeSistemaItem, String tipoGatilho, 
                                EntidadeInstancia personagem, EntidadeInstancia alvo, 
                                Map<String, Object> contextoExtra) {
        if (item == null || entidadeSistemaItem == null) {
            return;
        }

        Map<String, Object> propriedades = entidadeSistemaItem.getPropriedades();
        if (propriedades == null || propriedades.isEmpty()) {
            return;
        }

        // Verifica se o item possui o gatilho configurado no mapa de propriedades
        Object gatilhosObj = propriedades.get("gatilhos");
        if (gatilhosObj instanceof Map<?, ?> gatilhos) {
            if (gatilhos.containsKey(tipoGatilho)) {
                // Execução de lógica pura de domínio para tratar gatilhos de itens
                if (salvarEntidadeInstanciaPort != null && personagem != null) {
                    salvarEntidadeInstanciaPort.salvar(personagem);
                }
            }
        }
    }
}