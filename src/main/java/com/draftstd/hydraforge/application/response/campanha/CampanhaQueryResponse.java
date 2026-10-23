package com.draftstd.hydraforge.application.response.campanha;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CampanhaQueryResponse(UUID id, String nome, String descricao, String urlImagem, StatusCampanha status, UUID sistemaId, UUID mestreId, List<CampanhaParticipanteQueryResponse> participantes, List<UUID> sessoesIds, List<UUID> personagensIds, List<UUID> instanciasIds, LocalDateTime criadaEm, LocalDateTime atualizadaEm) {

}