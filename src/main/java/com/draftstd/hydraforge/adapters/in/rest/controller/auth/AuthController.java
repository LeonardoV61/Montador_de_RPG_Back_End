package com.draftstd.hydraforge.adapters.in.rest.controller.auth;

import com.draftstd.hydraforge.ports.in.auth.AutenticarUsuarioUseCase;
import com.draftstd.hydraforge.ports.in.auth.RegistrarUsuarioUseCase;
import com.draftstd.hydraforge.application.request.auth.LoginCommand;
import com.draftstd.hydraforge.application.request.auth.RegistrarUsuarioCommand;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;

    public AuthController(AutenticarUsuarioUseCase autenticarUsuarioUseCase,
                          RegistrarUsuarioUseCase registrarUsuarioUseCase) {
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
        this.registrarUsuarioUseCase = registrarUsuarioUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        var command = new LoginCommand(request.email(), request.senha());
        String token = autenticarUsuarioUseCase.autenticar(command);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registrar(@Valid @RequestBody RegistroRequest request) {
        var command = new RegistrarUsuarioCommand(request.email(), request.apelido(), request.senha());
        String token = registrarUsuarioUseCase.registrar(command);
        return ResponseEntity.ok(Map.of("token", token));
    }
}

// DTOs de Entrada REST
record LoginRequest(String email, String senha) {}
record RegistroRequest(String email, String apelido, String senha) {}