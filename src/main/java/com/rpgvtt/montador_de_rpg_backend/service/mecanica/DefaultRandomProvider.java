package com.rpgvtt.montador_de_rpg_backend.service.mecanica;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class DefaultRandomProvider implements RandomProvider {
    @Override
    public int rollD20() {
        return ThreadLocalRandom.current().nextInt(1, 21);
    }
}
