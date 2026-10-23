package com.draftstd.hydraforge.adapters.out.persistence.mecanica.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.mecanica.entity.HistoricoRolagemJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.mecanica.repository.HistoricoRolagemRepository;
import com.draftstd.hydraforge.application.response.mecanica.ResultadoRolagemQueryResponse;
import com.draftstd.hydraforge.ports.out.persistence.mecanica.ObterHistoricoRolagensPort;
import com.draftstd.hydraforge.ports.out.persistence.mecanica.SalvarHistoricoRolagemPort;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class HistoricoRolagemPersistenceAdapter implements SalvarHistoricoRolagemPort, ObterHistoricoRolagensPort {

    private final HistoricoRolagemRepository repository;

    public HistoricoRolagemPersistenceAdapter(HistoricoRolagemRepository repository) {
        this.repository = repository;
    }

    @Override
    public void salvar(ResultadoRolagemQueryResponse resultado) {
        String resultadosCsv = resultado.dadosSorteados() != null
                ? String.join(",", resultado.dadosSorteados().stream().map(String::valueOf).toList())
                : "";

        HistoricoRolagemJpaEntity entity = new HistoricoRolagemJpaEntity(
                resultado.id(),
                resultado.sessaoId(),
                resultado.jogadorId(),
                resultado.personagemId(),
                resultado.expressaoOriginal(),
                resultadosCsv,
                resultado.modificadorTotal(),
                resultado.resultadoFinal(),
                resultado.motivo(),
                resultado.timestamp()
        );

        repository.save(entity);
    }

    @Override
    public List<ResultadoRolagemQueryResponse> buscarPorSessao(UUID sessaoId, UUID personagemId, int limite) {
        PageRequest pageable = PageRequest.of(0, limite);
        List<HistoricoRolagemJpaEntity> entities;

        if (personagemId != null) {
            entities = repository.findBySessaoIdAndPersonagemIdOrderByCriadoEmDesc(sessaoId, personagemId, pageable);
        } else {
            entities = repository.findBySessaoIdOrderByCriadoEmDesc(sessaoId, pageable);
        }

        return entities.stream().map(this::toResponse).toList();
    }

    private ResultadoRolagemQueryResponse toResponse(HistoricoRolagemJpaEntity entity) {
        List<Integer> dadosSorteados = List.of();
        if (entity.getResultados() != null && !entity.getResultados().isBlank()) {
            dadosSorteados = Arrays.stream(entity.getResultados().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .toList();
        }

        return new ResultadoRolagemQueryResponse(
                entity.getId(),
                entity.getSessaoId(),
                entity.getJogadorId(),
                entity.getPersonagemId(),
                entity.getExpressaoDados(),
                dadosSorteados,
                entity.getModificador(),
                entity.getTotal(),
                entity.getMotivo(),
                entity.getCriadoEm()
        );
    }
}