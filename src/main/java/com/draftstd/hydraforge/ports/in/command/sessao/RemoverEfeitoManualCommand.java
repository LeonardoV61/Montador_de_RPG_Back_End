package com.draftstd.hydraforge.ports.in.command.sessao;

import java.util.UUID;

public record RemoverEfeitoManualCommand(UUID sessaoId, UUID efeitoId) {

}