package com.draftstd.hydraforge.adapters.in.dto.sessao;

import java.util.UUID;

public record AtributoAlteradoDTO(UUID idInstancia, String atributo, Object valorAnterior, Object novoValor) {

}