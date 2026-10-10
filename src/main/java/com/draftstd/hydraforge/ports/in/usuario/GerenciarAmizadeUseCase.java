package com.draftstd.hydraforge.ports.in.usuario;

import com.draftstd.hydraforge.adapters.in.dto.usuario.AmizadeResponseDTO;
import java.util.List;
import java.util.UUID;

public interface GerenciarAmizadeUseCase {
    void enviarSolicitacao(UUID remetenteId, UUID destinatarioId);
    void aceitarSolicitacao(UUID usuarioId, UUID amigoId);
    void recusarOuRemover(UUID usuarioId, UUID amigoId);
    List<AmizadeResponseDTO> listarAmigos(UUID usuarioId);
}