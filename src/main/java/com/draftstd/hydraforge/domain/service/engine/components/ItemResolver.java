package com.draftstd.hydraforge.domain.service.engine.components;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.model.entidade.EntidadeRelacao;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeRelacaoPort;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ItemResolver {

    private final EntidadeRelacaoPort relacaoPort;

    public ItemResolver(EntidadeRelacaoPort relacaoPort) {
        this.relacaoPort = relacaoPort;
    }

    public List<EntidadeRelacao> listarItens(UUID idPersonagem) {
        return relacaoPort.buscarPorEntidadePaiId(idPersonagem);
    }

    public Optional<EntidadeRelacao> buscarRelacao(UUID idPersonagem, UUID idItem) {
        return relacaoPort.buscarPorPaiEFilhaId(idPersonagem, idItem);
    }

    public List<EntidadeRelacao> listarEquipados(UUID idPersonagem) {
        return listarItens(idPersonagem).stream()
                .filter(this::isEquipado)
                .toList();
    }

    public Optional<EntidadeRelacao> buscarEquipadoPorSlot(UUID idPersonagem, String slot) {
        return listarEquipados(idPersonagem).stream()
                .filter(r -> slot.equals(slotDoItem(r.getEntidadeFilha())))
                .findFirst();
    }

    public boolean isEquipado(EntidadeRelacao r) {
        JsonNode custom = r.getCustomizacoes();
        return custom != null && custom.path("equipado").asBoolean(false);
    }

    public String slotDoItem(EntidadeInstancia item) {
        return item.getEntidadeSistema().getPropriedades().path("slot").asString(null);
    }

    public JsonNode atributoDoEquipado(UUID idPersonagem, String slot, String atributo) {
        return buscarEquipadoPorSlot(idPersonagem, slot)
                .map(EntidadeRelacao::getEntidadeFilha)
                .map(item -> item.getAtributosAtuais().get(atributo))
                .orElse(null);
    }
}