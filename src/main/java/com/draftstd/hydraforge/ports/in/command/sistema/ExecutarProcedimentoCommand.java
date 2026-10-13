package com.draftstd.hydraforge.ports.in.command.sistema;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ExecutarProcedimentoCommand(UUID procedimentoId, UUID sessaoId, List<UUID> instanciasIds, String acao, Map<String, Object> parametrosEntrada) {
    
}