package com.draftstd.hydraforge.ports.in.command.campanha;

import java.util.UUID;

public record CriarCampanhaCommand(String nome, String descricao, UUID sistemaId, UUID mestreId ) {

}