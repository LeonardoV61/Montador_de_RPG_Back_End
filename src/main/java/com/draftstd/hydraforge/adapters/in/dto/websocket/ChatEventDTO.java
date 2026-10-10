package com.draftstd.hydraforge.adapters.in.dto.websocket;

import java.time.Instant;

public record ChatEventDTO(String sender, String content, String type, Instant timestamp) {
}
