package com.draftstd.hydraforge.domain.service.engine.primitivos;

import org.springframework.stereotype.Component;

import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.domain.service.engine.exceptions.EntityNotFoundException;
import com.draftstd.hydraforge.ports.out.persistence.sistema.ObterSistemaPort;

import java.util.Map;
import java.util.UUID;

@Component
public class VerificadorAtributo {

    private final ObterSistemaPort obterSistemaPort;

    public VerificadorAtributo(ObterSistemaPort obterSistemaPort) {
        this.obterSistemaPort = obterSistemaPort;
    }

    public boolean verificarAtributo(UUID idSistema, String atributo, double valor) {
        Sistema sistema = obterSistemaPort.buscarPorId(idSistema)
                .orElseThrow(() -> new EntityNotFoundException(Sistema.class, idSistema));

        // Obtém o objeto do schema de atributos sem dependência do Jackson
        Object schemaObj = sistema.getSchemaAtributos() != null ? sistema.getSchemaAtributos().get(atributo) : null;

        if (schemaObj instanceof Map<?, ?> schemaMap) {
            Number min = (Number) schemaMap.get("min");
            Number max = (Number) schemaMap.get("max");

            if (min != null && valor < min.doubleValue()) return false;
            if (max != null && valor > max.doubleValue()) return false;
            return true;
        }

        return false;
    }
}