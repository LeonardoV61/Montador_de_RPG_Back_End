package com.rpgvtt.montador_de_rpg_backend.controller.sessao;

import com.rpgvtt.montador_de_rpg_backend.dto.sessao.*;
import com.rpgvtt.montador_de_rpg_backend.security.UsuarioPrincipal;
import com.rpgvtt.montador_de_rpg_backend.service.sessao.SessaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sessoes")
@RequiredArgsConstructor
public class SessaoController {

    private final SessaoService sessaoService;

    @PostMapping("/campanhas/{idCampanha}/iniciar")
    @ResponseStatus(HttpStatus.CREATED)
    public SessaoDTO iniciar(@PathVariable Long idCampanha,
                             @AuthenticationPrincipal UsuarioPrincipal principal) {
        return sessaoService.iniciarSessao(idCampanha, principal.getId());
    }

    // Nova: agenda uma sessão sem iniciá-la (cria com status AGENDADA e dataInicio)
    @PostMapping("/campanhas/{idCampanha}/agendar")
    public ResponseEntity<SessaoDTO> agendar(
            @PathVariable Long idCampanha,
            @RequestBody AgendarSessaoRequest req,
            @AuthenticationPrincipal UsuarioPrincipal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessaoService.agendarSessao(idCampanha, req.dataInicio(), principal.getId()));
    }

    
    @GetMapping("/campanha/{idCampanha}")
    public ResponseEntity<List<SessaoDTO>> listarPorCampanha(@PathVariable Long idCampanha) {
        return ResponseEntity.ok(sessaoService.listarPorCampanha(idCampanha));
    }

    @PostMapping("/{idSessao}/encerrar")
    public void encerrar(@PathVariable Long idSessao,
                         @AuthenticationPrincipal UsuarioPrincipal principal) {
        sessaoService.encerrarSessao(idSessao, principal.getId());
    }

    @PostMapping("/{idSessao}/entrar")
    public EntradaSessaoDTO entrar(@PathVariable Long idSessao,
                                   @RequestBody(required = false) EntrarRequest req,
                                   @AuthenticationPrincipal UsuarioPrincipal principal) {
        String token = req != null ? req.tokenConvite() : null;
        return sessaoService.entrarNaSessao(idSessao, principal.getId(), token);
    }

    @PostMapping("/{idSessao}/convite")
    public ConviteDTO convidar(@PathVariable Long idSessao,
                               @RequestBody ConviteRequest req,
                               @AuthenticationPrincipal UsuarioPrincipal principal) {
        return sessaoService.gerarConvite(idSessao, req.idUsuarioAlvo(), principal.getId());
    }

    @PatchMapping("/{idSessao}/instancias/{idInstancia}/atributos")
    public AtributoAlteradoDTO alterarAtributoMestre(
            @PathVariable Long idSessao,
            @PathVariable Long idInstancia,
            @RequestBody AlterarAtributoRequest req,
            @AuthenticationPrincipal UsuarioPrincipal principal) {
        return sessaoService.alterarAtributoInstancia(
                idSessao, idInstancia, req.atributo(), req.valor(), principal.getId());
    }

    @PatchMapping("/{idSessao}/meu-personagem/atributos")
    public AtributoAlteradoDTO alterarAtributoJogador(
            @PathVariable Long idSessao,
            @RequestBody AlterarAtributoRequest req,
            @AuthenticationPrincipal UsuarioPrincipal principal) {
        Long idInstancia = sessaoService.resolverInstanciaDoJogador(idSessao, principal.getId());
        return sessaoService.alterarAtributoPersonagem(
                idSessao, idInstancia, req.atributo(), req.valor(), principal.getId());
    }

    public record EntrarRequest(String tokenConvite) {}
    public record ConviteRequest(Long idUsuarioAlvo) {}
    public record AlterarAtributoRequest(String atributo, Object valor) {}
    public record AgendarSessaoRequest(LocalDateTime dataInicio) {}
}