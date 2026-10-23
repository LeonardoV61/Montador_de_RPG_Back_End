package com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces;


import java.util.List;
import java.util.UUID;

// The three possible scopes as a sealed hierarchy
public sealed interface EscopoInstancias
        permits EscopoInstancias.Nenhuma,
        EscopoInstancias.Unica,
        EscopoInstancias.Multiplas {

    record Nenhuma() implements EscopoInstancias {}

    record Unica(UUID idInstancia) implements EscopoInstancias {
        public UUID id() { return idInstancia; }
    }

    record Multiplas(List<UUID> ids) implements EscopoInstancias {
        // ordered — matters for sequential area resolution
        public Multiplas(List<UUID> ids) {
            this.ids = List.copyOf(ids);
        }
    }

    // Factory methods for call sites
    static EscopoInstancias nenhuma() { return new Nenhuma(); }
    static EscopoInstancias unica(UUID id) { return new Unica(id); }
    static EscopoInstancias multiplas(List<UUID> ids) { return new Multiplas(ids); }
}