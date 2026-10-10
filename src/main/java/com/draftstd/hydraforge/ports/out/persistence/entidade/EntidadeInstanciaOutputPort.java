package com.draftstd.hydraforge.ports.out.persistence.entidade;

import com.draftstd.hydraforge.adapters.in.dto.entidade.EntidadeInstanciaCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.entidade.EntidadeInstanciaResponseDTO;
import com.draftstd.hydraforge.adapters.in.dto.entidade.EntidadeInstanciaUpadteDTO;
import com.draftstd.hydraforge.adapters.in.dto.entidade.EntidadeRelacaoCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.entidade.EntidadeRelacaoResponseDTO;

import java.util.List;
import java.util.Optional;

public interface EntidadeInstanciaOutputPort {
    EntidadeInstanciaResponseDTO salvar(EntidadeInstanciaCreateDTO dto);
    Optional<EntidadeInstanciaResponseDTO> buscarPorId(Long id);
    List<EntidadeInstanciaResponseDTO> listarPorCampanha(Long campanhaId);
    EntidadeInstanciaResponseDTO atualizar(Long id, EntidadeInstanciaUpadteDTO dto);
    void deletar(Long id);
    
    List<EntidadeRelacaoResponseDTO> listarRelacoesPorPai(Long idPai);
    EntidadeRelacaoResponseDTO adicionarRelacao(EntidadeRelacaoCreateDTO dto);
    void removerRelacao(Long idPai, Long idFilha);
    EntidadeInstanciaResponseDTO concederClasse(Long idInstancia, Long entidadeSistemaId);
}