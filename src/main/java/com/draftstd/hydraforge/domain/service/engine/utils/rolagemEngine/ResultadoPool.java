package com.draftstd.hydraforge.domain.service.engine.utils.rolagemEngine;

import java.util.List;

public record ResultadoPool(int tamanhoPool, List<Integer> rolos, int sucessosLiquidos, int falhas, boolean critico, boolean botch, Integer dificuldade) {

}