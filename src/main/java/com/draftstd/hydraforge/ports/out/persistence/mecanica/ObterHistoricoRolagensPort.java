package com.draftstd.hydraforge.ports.out.persistence.mecanica;

import com.draftstd.hydraforge.application.response.mecanica.ResultadoRolagemQueryResponse;

import java.util.List;
import java.util.UUID;

public interface ObterHistoricoRolagensPort {
    List<ResultadoRolagemQueryResponse> buscarPorSessao(UUID sessaoId, UUID personagemId, int limite);
}