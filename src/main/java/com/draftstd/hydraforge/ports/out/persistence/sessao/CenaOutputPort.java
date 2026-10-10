package com.draftstd.hydraforge.ports.out.persistence.sessao;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.model.sessao.Cena;

import java.util.List;
import java.util.Optional;

public interface CenaOutputPort {
    Cena salvar(Cena cena);
    Optional<Cena> buscarPorId(Long id);
    Optional<Cena> buscarUltimaPorSessaoId(Long idSessao);
    List<Cena> buscarPorSessaoIdOrdenadoPorOrdemDesc(Long idSessao);
    long contarPorCriadorIdEPapel(Long usuarioId, PapeisUsuario papel);
    long contarPorCriadorIdEPapelEComCamposNulos(Long usuarioId, PapeisUsuario papel);
    void deletarPorId(Long id);
}