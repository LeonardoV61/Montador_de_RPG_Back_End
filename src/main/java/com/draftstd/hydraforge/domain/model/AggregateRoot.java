package com.draftstd.hydraforge.domain.model;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot<ID> {
    private ID id;
    private long version = -1;
    private final List<DomainEvent> uncommittedEvents = new ArrayList<>();

    public ID getId() {
        return id;
    }

    protected void setId(ID id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public List<DomainEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(uncommittedEvents);
    }

    public void markEventsAsCommitted() {
        this.uncommittedEvents.clear();
    }

    protected void raiseEvent(DomainEvent event) {
        this.uncommittedEvents.add(event);
        apply(event);
    }

    // Método abstrato para reconstruir o estado a partir de um evento
    protected abstract void apply(DomainEvent event);

    // Reconstitui o agregado a partir de um histórico de eventos
    public void loadFromHistory(List<DomainEvent> history) {
        for (DomainEvent event : history) {
            apply(event);
            this.version = event.getVersion();
        }
    }
}