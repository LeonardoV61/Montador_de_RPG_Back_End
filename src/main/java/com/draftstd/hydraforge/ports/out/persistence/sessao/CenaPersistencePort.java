package com.draftstd.hydraforge.ports.out.persistence.sessao;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.model.sessao.Cena;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CenaPersistencePort {
    void salvar(Cena cena);
    Optional<Cena> buscarPorId(UUID id);
    Optional<Cena> buscarUltimaPorSessaoId(UUID sessaoId);
    List<Cena> buscarPorSessaoIdOrdenadoPorOrdemDesc(UUID sessaoId);
    long contarPorCriadorIdEPapel(UUID usuarioId, PapeisUsuario papel);
    long contarPorCriadorIdEPapelEComCamposNulos(UUID usuarioId, PapeisUsuario papel);
    void deletarPorId(UUID id);
}