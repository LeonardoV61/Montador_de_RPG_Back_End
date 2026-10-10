package com.draftstd.hydraforge.ports.in.query.campanha;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CampanhaQueryResponse(UUID id, String nome, String descricao, UUID sistemaId, UUID mestreId, StatusCampanha status, List<CampanhaParticipanteQueryResponse> participantes, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {

}