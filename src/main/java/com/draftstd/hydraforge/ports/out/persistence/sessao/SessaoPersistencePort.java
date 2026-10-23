package com.draftstd.hydraforge.ports.out.persistence.sessao;

import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.domain.enums.StatusSessao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessaoPersistencePort {
    void salvar(Sessao sessao);
    Optional<Sessao> buscarPorId(UUID id);
    List<Sessao> buscarPorStatusEProcedimentoAtivoNaoNulo(StatusSessao status);
    Optional<Sessao> buscarAtivaPorCampanhaId(UUID campanhaId);
    List<Sessao> buscarPorCampanhaId(UUID campanhaId);
    List<Sessao> buscarPorCampanhaIdOrdenadoPorOrdemDesc(UUID campanhaId);
    Optional<Sessao> buscarPrimeiraPorCampanhaEStatusOrdenadoPorDataInicioDesc(UUID campanhaId, StatusSessao status);
    int contarPorCampanhaId(UUID campanhaId);
    long contarPorUsuarioIdEMesAtual(UUID usuarioId, int mes, int ano);
    void deletarPorId(UUID id);
}