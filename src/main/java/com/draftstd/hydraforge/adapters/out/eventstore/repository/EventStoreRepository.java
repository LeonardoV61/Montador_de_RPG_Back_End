package com.draftstd.hydraforge.adapters.out.eventstore.repository;

import com.draftstd.hydraforge.adapters.out.eventstore.entity.EventStoreJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventStoreRepository extends JpaRepository<EventStoreJpaEntity, UUID> {
    List<EventStoreJpaEntity> findByAggregateIdOrderByVersaoAsc(String aggregateId);
    List<EventStoreJpaEntity> findBySessaoIdOrderByVersaoAsc(UUID sessaoId);
    @Query("SELECT MAX(e.versao) FROM EventStoreJpaEntity e WHERE e.aggregateId = :aggregateId")
    Optional<Long> findMaxVersionByAggregateId(@Param("aggregateId") String aggregateId);
}