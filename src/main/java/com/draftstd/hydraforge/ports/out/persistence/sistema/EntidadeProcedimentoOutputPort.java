package com.draftstd.hydraforge.ports.out.persistence.sistema;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeProcedimento;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntidadeProcedimentoOutputPort {
    void salvar(EntidadeProcedimento entidadeProcedimento);
    Optional<EntidadeProcedimento> buscarPorId(UUID id);
    Optional<EntidadeProcedimento> buscarPorEntidadeInstanciaId(UUID entidadeInstanciaId);
    List<EntidadeProcedimento> listarPorEntidadeInstanciaId(UUID entidadeInstanciaId);
    List<EntidadeProcedimento> listarPorSistemaId(UUID sistemaId);
    void deletarPorId(UUID id);
}