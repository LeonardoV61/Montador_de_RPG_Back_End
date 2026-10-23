package com.draftstd.hydraforge.ports.out.persistence.sessao;

import com.draftstd.hydraforge.domain.model.sessao.CenaParticipante;

import java.util.List;
import java.util.UUID;

public interface CenaParticipanteOutputPort {
    void salvar(UUID cenaId, CenaParticipante participante);
    
    void salvarTodos(UUID cenaId, List<CenaParticipante> participantes);
    
    List<CenaParticipante> buscarPorCenaId(UUID cenaId);
    
    void deletarPorCenaId(UUID cenaId);
}