package com.draftstd.hydraforge.adapters.out.eventstore.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataEventStoreRepository extends JpaRepository<EventStoreEntity, UUID> {

    // Busca todos os eventos de um agregado ordenados pela versão para permitir a reidratação
    List<EventStoreEntity> findByAggregateIdOrderByVersionAsc(String aggregateId);

    // Opcional: útil para verificar a última versão gravada do agregado
    EventStoreEntity findTopByAggregateIdOrderByVersionDesc(String aggregateId);
}