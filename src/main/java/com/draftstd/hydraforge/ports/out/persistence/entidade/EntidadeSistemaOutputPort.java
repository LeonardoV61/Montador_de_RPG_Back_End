package com.draftstd.hydraforge.ports.out.persistence.entidade;

import com.draftstd.hydraforge.adapters.in.dto.entidade.EntidadeSistemaCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.entidade.EntidadeSistemaResponseDTO;
import com.draftstd.hydraforge.adapters.in.dto.entidade.EntidadeSistemaUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface EntidadeSistemaOutputPort {
    EntidadeSistemaResponseDTO salvar(EntidadeSistemaCreateDTO dto);
    Optional<EntidadeSistemaResponseDTO> buscarPorId(Long id);
    List<EntidadeSistemaResponseDTO> listarPorSistema(Long sistemaId);
    EntidadeSistemaResponseDTO atualizar(Long id, EntidadeSistemaUpdateDTO dto);
    void deletar(Long id);
}