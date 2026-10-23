package com.draftstd.hydraforge.adapters.out.eventstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.domain.Persistable;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_store", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"aggregate_id", "versao"})
})
public class EventStoreJpaEntity implements Persistable<UUID> {

    @Id
    @Column(name = "id_evento", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID idEvento;

    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId;

    @Column(name = "sessao_id", columnDefinition = "UUID")
    private UUID sessaoId;

    @Column(name = "tipo_evento", nullable = false)
    private String tipoEvento;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload_json", nullable = false, columnDefinition = "jsonb")
    private String payloadJson;

    @Column(name = "versao", nullable = false)
    private Long versao;

    @Column(name = "data_hora", nullable = false, updatable = false)
    private Instant dataHora;

    @Transient
    private boolean transientNew = true;

    @Override
    public UUID getId() {
        return idEvento;
    }

    @Override
    public boolean isNew() {
        return transientNew;
    }

    @PostPersist
    @PostLoad
    public void markNotNew() {
        this.transientNew = false;
    }
}