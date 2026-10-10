package com.draftstd.hydraforge.domain.service.engine.primitivos;

import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.domain.service.engine.exceptions.EntityNotFoundException;
import com.draftstd.hydraforge.adapters.out.repository.entidade.EntidadeSistemaRepository;
import com.draftstd.hydraforge.adapters.out.repository.sistema.SistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
@RequiredArgsConstructor
public class VerificadorAtributo {

    private final EntidadeSistemaRepository entidadeSistemaRepo;
    private final SistemaRepository sistemaRepo;

    public boolean verificarAtributo (Long idSistema, String atributo, double valor){
        Sistema sistema = sistemaRepo.findById(idSistema)
                .orElseThrow(() -> new EntityNotFoundException(Sistema.class, idSistema));
        JsonNode schemaAtributo = sistema.getSchemaAtributos().get(atributo);
        int min = schemaAtributo.get("min").asInt();
        int max = schemaAtributo.get("max").asInt();
        return !(valor < min) && !(valor > max);
    }
}
