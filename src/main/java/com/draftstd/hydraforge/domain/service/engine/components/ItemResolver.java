package com.draftstd.hydraforge.domain.service.engine.components;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.model.entidade.EntidadeRelacao;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeRelacaoPort;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Optional;

public class ItemResolver {

    private final EntidadeRelacaoPort relacaoPort;

    public ItemResolver(EntidadeRelacaoPort relacaoPort) {
        this.relacaoPort = relacaoPort;
    }

    public List<EntidadeRelacao> listarItens(Long idPersonagem) {
        return relacaoPort.buscarPorEntidadePaiId(idPersonagem);
    }

    public Optional<EntidadeRelacao> buscarRelacao(Long idPersonagem, Long idItem) {
        return relacaoPort.buscarPorPaiEFilhaId(idPersonagem, idItem);
    }

    public List<EntidadeRelacao> listarEquipados(Long idPersonagem) {
        return listarItens(idPersonagem).stream()
                .filter(this::isEquipado)
                .toList();
    }

    public Optional<EntidadeRelacao> buscarEquipadoPorSlot(Long idPersonagem, String slot) {
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

    public JsonNode atributoDoEquipado(Long idPersonagem, String slot, String atributo) {
        return buscarEquipadoPorSlot(idPersonagem, slot)
                .map(EntidadeRelacao::getEntidadeFilha)
                .map(item -> item.getAtributosAtuais().get(atributo))
                .orElse(null);
    }
}