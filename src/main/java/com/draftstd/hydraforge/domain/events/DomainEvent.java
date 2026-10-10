package com.draftstd.hydraforge.domain.events;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    UUID getEventId();
    String getAggregateId();
    Instant getOccurredOn();
    long getVersion();
}