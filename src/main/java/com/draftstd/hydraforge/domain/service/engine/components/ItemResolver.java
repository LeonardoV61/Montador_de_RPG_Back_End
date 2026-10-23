package com.draftstd.hydraforge.domain.service.engine.components;

import org.springframework.stereotype.Component;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeRelacao;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeRelacaoOutputPort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class ItemResolver {

    private final EntidadeRelacaoOutputPort relacaoPort;
    private final EntidadeInstanciaOutputPort instanciaPort;

    public ItemResolver(EntidadeRelacaoOutputPort relacaoPort, EntidadeInstanciaOutputPort instanciaPort) {
        this.relacaoPort = relacaoPort;
        this.instanciaPort = instanciaPort;
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

    public Optional<EntidadeInstancia> buscarEquipadoPorSlot(UUID idPersonagem, String slot) {
        return listarEquipados(idPersonagem).stream()
                .map(r -> instanciaPort.buscarPorId(r.getEntidadeFilhaId()).orElse(null))
                .filter(item -> item != null && slot.equals(slotDoItem(item)))
                .findFirst();
    }

    public boolean isEquipado(EntidadeRelacao r) {
        Map<String, Object> custom = r.getCustomizacoes();
        if (custom == null) return false;
        Object val = custom.get("equipado");
        if (val instanceof Boolean b) return b;
        return val != null && Boolean.parseBoolean(val.toString());
    }

    public String slotDoItem(EntidadeInstancia item) {
        if (item == null || item.getCustomizacoes() == null) return null;
        Object slot = item.getCustomizacoes().get("slot");
        return slot != null ? slot.toString() : null;
    }

    public Object atributoDoEquipado(UUID idPersonagem, String slot, String atributo) {
        return buscarEquipadoPorSlot(idPersonagem, slot)
                .map(item -> item.getValoresAtributos() != null ? item.getValoresAtributos().get(atributo) : null)
                .orElse(null);
    }
}