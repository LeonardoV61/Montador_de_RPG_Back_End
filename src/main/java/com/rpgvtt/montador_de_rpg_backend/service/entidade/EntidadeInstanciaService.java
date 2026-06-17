package com.rpgvtt.montador_de_rpg_backend.service.entidade;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.rpgvtt.montador_de_rpg_backend.domain.validation.SchemaValidator;
import com.rpgvtt.montador_de_rpg_backend.repository.entidade.EntidadeInstanciaRepository;
import com.rpgvtt.montador_de_rpg_backend.repository.entidade.EntidadeRelacaoRepository;
import com.rpgvtt.montador_de_rpg_backend.repository.entidade.EntidadeSistemaRepository;
import com.rpgvtt.montador_de_rpg_backend.repository.mecanica.EntidadeProcedimentoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EntidadeInstanciaService {

    private final EntidadeInstanciaRepository       instanciaRepository;
    private final EntidadeSistemaRepository         entidadeSistemaRepository;
    private final EntidadeRelacaoRepository         entidadeRelacaoRepository;
    private final EntidadeProcedimentoRepository    entidadeProcedimentoRepository;
    private final SchemaValidator                   schemaValidator;
    
}
