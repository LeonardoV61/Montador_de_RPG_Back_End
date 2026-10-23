package com.draftstd.hydraforge.domain.service.mecanica;

import java.util.Map;

public record ResolutionOutcome(int roll, int targetValue, boolean success, String motivo, Map<String, Object> detalhes) {}