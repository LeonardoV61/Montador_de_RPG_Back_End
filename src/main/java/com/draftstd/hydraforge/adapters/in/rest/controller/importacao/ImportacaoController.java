package com.draftstd.hydraforge.adapters.in.rest.controller.importacao;

import com.draftstd.hydraforge.ports.in.command.importacao.ExportarPersonagemCommand;
import com.draftstd.hydraforge.ports.in.command.importacao.ExportarSistemaCommand;
import com.draftstd.hydraforge.ports.in.command.importacao.ImportarPersonagemCommand;
import com.draftstd.hydraforge.ports.in.command.importacao.ImportarSistemaCommand;
import com.draftstd.hydraforge.ports.in.importacao.ExportarPersonagemUseCase;
import com.draftstd.hydraforge.ports.in.importacao.ExportarSistemaUseCase;
import com.draftstd.hydraforge.ports.in.importacao.ImportarPersonagemUseCase;
import com.draftstd.hydraforge.ports.in.importacao.ImportarSistemaUseCase;
import com.draftstd.hydraforge.ports.in.query.importacao.ArquivoExportadoQueryResponse;
import com.draftstd.hydraforge.ports.in.query.personagem.PersonagemQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sistema.SistemaQueryResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/importacao")
public class ImportacaoController {

    private final ImportarSistemaUseCase importarSistemaUseCase;
    private final ExportarSistemaUseCase exportarSistemaUseCase;
    private final ImportarPersonagemUseCase importarPersonagemUseCase;
    private final ExportarPersonagemUseCase exportarPersonagemUseCase;

    public ImportacaoController(ImportarSistemaUseCase importarSistemaUseCase,
                                ExportarSistemaUseCase exportarSistemaUseCase,
                                ImportarPersonagemUseCase importarPersonagemUseCase,
                                ExportarPersonagemUseCase exportarPersonagemUseCase) {
        this.importarSistemaUseCase = importarSistemaUseCase;
        this.exportarSistemaUseCase = exportarSistemaUseCase;
        this.importarPersonagemUseCase = importarPersonagemUseCase;
        this.exportarPersonagemUseCase = exportarPersonagemUseCase;
    }

    @PostMapping("/sistema")
    public ResponseEntity<SistemaQueryResponse> importarSistema(@RequestParam("file") MultipartFile file,
                                                                 @RequestHeader("X-Usuario-Id") UUID autorId) throws IOException {
        ImportarSistemaCommand command = new ImportarSistemaCommand(
                autorId,
                file.getBytes(),
                file.getContentType()
        );
        return ResponseEntity.ok(importarSistemaUseCase.executar(command));
    }

    @GetMapping("/sistema/{id}/exportar")
    public ResponseEntity<byte[]> exportarSistema(@PathVariable UUID id,
                                                   @RequestParam(defaultValue = "JSON") String formato) {
        ExportarSistemaCommand command = new ExportarSistemaCommand(id, formato);
        ArquivoExportadoQueryResponse arquivo = exportarSistemaUseCase.executar(command);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.nomeArquivo() + "\"")
                .contentType(MediaType.parseMediaType(arquivo.tipoConteudo()))
                .body(arquivo.dados());
    }

    @PostMapping("/personagem")
    public ResponseEntity<PersonagemQueryResponse> importarPersonagem(@RequestParam("file") MultipartFile file,
                                                                       @RequestParam(required = false) UUID campanhaId,
                                                                       @RequestHeader("X-Usuario-Id") UUID usuarioId) throws IOException {
        ImportarPersonagemCommand command = new ImportarPersonagemCommand(
                usuarioId,
                campanhaId,
                file.getBytes(),
                file.getContentType()
        );
        return ResponseEntity.ok(importarPersonagemUseCase.executar(command));
    }

    @GetMapping("/personagem/{id}/exportar")
    public ResponseEntity<byte[]> exportarPersonagem(@PathVariable UUID id,
                                                     @RequestParam(defaultValue = "JSON") String formato) {
        ExportarPersonagemCommand command = new ExportarPersonagemCommand(id, formato);
        ArquivoExportadoQueryResponse arquivo = exportarPersonagemUseCase.executar(command);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.nomeArquivo() + "\"")
                .contentType(MediaType.parseMediaType(arquivo.tipoConteudo()))
                .body(arquivo.dados());
    }
}