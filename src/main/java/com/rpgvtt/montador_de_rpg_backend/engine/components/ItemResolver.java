package com.rpgvtt.montador_de_rpg_backend.engine.components;

import com.rpgvtt.montador_de_rpg_backend.domain.model.entidade.EntidadeInstancia;
import com.rpgvtt.montador_de_rpg_backend.domain.model.entidade.EntidadeRelacao;
import com.rpgvtt.montador_de_rpg_backend.repository.entidade.EntidadeRelacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ItemResolver {

    private final EntidadeRelacaoRepository relacaoRepo;

    public List<EntidadeRelacao> listarItens(Long idPersonagem) {
        return relacaoRepo.findByEntidadePaiId(idPersonagem);
    }

    public Optional<EntidadeRelacao> buscarRelacao(Long idPersonagem, Long idItem) {
        return relacaoRepo.findByEntidadePaiIdAndEntidadeFilhaId(idPersonagem, idItem);
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

    /**
     * Reads an attribute from whatever item is currently equipped in a slot.
     * Returns null if nothing is equipped there or the item lacks the attribute.
     * This is what combat handlers call instead of reading weapon stats
     * off the character's own atributos_atuais.
     */
    public JsonNode atributoDoEquipado(Long idPersonagem, String slot, String atributo) {
        return buscarEquipadoPorSlot(idPersonagem, slot)
                .map(EntidadeRelacao::getEntidadeFilha)
                .map(item -> item.getAtributosAtuais().get(atributo))
                .orElse(null);
    }
}
