package com.draftstd.hydraforge.ports.out.persistence.sistema;

import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import java.util.List;
import java.util.Optional;

public interface ProcedimentoOutputPort {
    Procedimento salvar(Procedimento procedimento);
    Optional<Procedimento> buscarPorId(Long id);
    List<Procedimento> listarPorSistema(Long sistemaId);
    List<Procedimento> listarTodos();
    void deletarPorId(Long id);
    boolean existePorId(Long id);
}