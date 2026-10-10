package com.draftstd.hydraforge.ports.in.command.sistema;

import java.util.Map;
import java.util.UUID;

public record ExecutarProcedimentoCommand(UUID procedimentoId, UUID sessaoId, UUID atorId, UUID alvoId, Map<String, Object> parametrosEntrada) {

}