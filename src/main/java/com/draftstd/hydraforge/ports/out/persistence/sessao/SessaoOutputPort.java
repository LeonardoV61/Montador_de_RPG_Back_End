package com.draftstd.hydraforge.ports.out.persistence.sessao;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;

import java.util.List;
import java.util.Optional;

public interface SessaoOutputPort {
    Sessao salvar(Sessao sessao);
    Optional<Sessao> buscarPorId(Long id);
    List<Sessao> buscarPorStatusEProcedimentoAtivoNaoNulo(StatusSessao status);
    Optional<Sessao> buscarAtivaPorCampanhaId(Long idCampanha);
    List<Sessao> buscarPorCampanhaId(Long idCampanha);
    List<Sessao> buscarPorCampanhaIdOrdenadoPorOrdemDesc(Long campanhaId);
    Optional<Sessao> buscarPrimeiraPorCampanhaEStatusOrdenadoPorDataInicioDesc(Long campanhaId, StatusSessao status);
    int contarPorCampanhaId(Long idCampanha);
    long contarPorUsuarioIdEMesAtual(Long usuarioId, int mes, int ano);
    void deletarPorId(Long id);
}