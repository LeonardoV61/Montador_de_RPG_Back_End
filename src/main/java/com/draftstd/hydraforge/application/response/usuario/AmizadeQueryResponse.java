package com.draftstd.hydraforge.application.response.usuario;

import com.draftstd.hydraforge.domain.enums.StatusAmizade;
import java.time.LocalDateTime;
import java.util.UUID;

public record AmizadeQueryResponse(UUID idRemetente, UUID idDestinatario, String apelidoAmigo, String urlImagemAmigo, StatusAmizade status, LocalDateTime criadaEm, LocalDateTime aceitaEm) {

}