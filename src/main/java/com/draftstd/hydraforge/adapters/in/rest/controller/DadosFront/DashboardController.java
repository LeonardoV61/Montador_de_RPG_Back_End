package com.draftstd.hydraforge.adapters.in.rest.controller.DadosFront;

import java.time.LocalDate;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import com.draftstd.hydraforge.adapters.out.repository.campanha.CampanhaRepository;
import com.draftstd.hydraforge.adapters.out.repository.sessao.CenaRepository;
import com.draftstd.hydraforge.adapters.out.repository.sessao.SessaoRepository;
import com.draftstd.hydraforge.security.UsuarioPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final CampanhaRepository campanhaRepo;
    private final CenaRepository cenaRepo;
    private final SessaoRepository sessaoRepo;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getIndicadores() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long idUsuario = ((UsuarioPrincipal) auth.getPrincipal()).getId();

        long campanhasAtivas = campanhaRepo.countByUsuarioIdAndStatus(idUsuario, StatusCampanha.ATIVA);
        LocalDate hoje = LocalDate.now();
        long sessoesEsteMes = sessaoRepo.countByUsuarioIdAndMesAtual(idUsuario, hoje.getMonthValue(), hoje.getYear());
        long jogadores = campanhaRepo.countJogadoresNasMesmasCampanhas(idUsuario);

        long cenasCriadas = cenaRepo.countByCriadorId(idUsuario, PapeisUsuario.MESTRE);
        long cenasParaPreparar = cenaRepo.countByCriadorIdAndCamposNulos(idUsuario, PapeisUsuario.MESTRE);

        Map<String, Object> indicadores = new LinkedHashMap<>();
        indicadores.put("campanhasAtivas", campanhasAtivas);
        indicadores.put("jogadores", jogadores);
        indicadores.put("cenasCriadas", cenasCriadas);
        indicadores.put("sessoesEsteMes", sessoesEsteMes);
        indicadores.put("cenasParaPreparar", cenasParaPreparar);

        return ResponseEntity.ok(indicadores);
    }
}