package com.draftstd.hydraforge.ports.out.persistence.mecanica;

import com.draftstd.hydraforge.adapters.in.dto.mecanica.ResolucaoCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.mecanica.ResolucaoExecucaoDTO;
import com.draftstd.hydraforge.adapters.in.dto.mecanica.ResolucaoResponseDTO;
import com.draftstd.hydraforge.adapters.in.dto.mecanica.ResolucaoUpdateDTO;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.Optional;

public interface ResolucaoOutputPort {
    ResolucaoResponseDTO salvar(ResolucaoCreateDTO dto);
    Optional<ResolucaoResponseDTO> buscarPorId(Long id);
    List<ResolucaoResponseDTO> listarPorSistema(Long sistemaId);
    ResolucaoResponseDTO atualizar(Long id, ResolucaoUpdateDTO dto);
    void deletar(Long id);
    ResolucaoExecucaoDTO executar(Long id, JsonNode contexto);
}