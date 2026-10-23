package com.draftstd.hydraforge.adapters.in.dto.usuario;

import com.draftstd.hydraforge.domain.enums.StatusAmizade;
import java.time.LocalDateTime;
import java.util.UUID;

public record AmizadeResponseDTO(UUID idRemetente, UUID idDestinatario, String apelidoAmigo, String urlImagemAmigo, StatusAmizade status, LocalDateTime criadaEm, LocalDateTime aceitaEm) {

}