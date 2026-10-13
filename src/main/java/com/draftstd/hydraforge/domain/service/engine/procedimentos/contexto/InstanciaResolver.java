package com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.domain.model.sessao.CenaParticipantes;
import com.draftstd.hydraforge.domain.service.engine.exceptions.EntityNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.adapters.out.repository.entidade.EntidadeInstanciaRepository;
import com.draftstd.hydraforge.adapters.out.repository.sessao.CenaParticipantesRepository;
import com.draftstd.hydraforge.adapters.out.repository.sessao.CenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InstanciaResolver {

    private final EntidadeInstanciaRepository instanciaRepo;
    private final CenaRepository cenaRepo;
    private final CenaParticipantesRepository cenaParticipantesRepo;

    public List<EntidadeInstancia> retornarTodas(ExecucaoContexto ctx) {
        List<UUID> ids = ctx.idsInstancias();
        if (ids == null || ids.isEmpty()) return List.of();

        List<EntidadeInstancia> instancias = instanciaRepo.findAllById(ids);
        Map<UUID, EntidadeInstancia> porId = instancias.stream()
                .collect(Collectors.toMap(EntidadeInstancia::getId, i -> i));

        return ids.stream().map(porId::get).filter(Objects::nonNull).toList();
    }

    public EntidadeInstancia buscarPorId(UUID id) {
        return instanciaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntidadeInstancia.class, id));
    }

    public EntidadeInstancia retornarAtiva(ExecucaoContexto ctx) {
        UUID idAtiva = ctx.idInstanciaAtiva();
        return instanciaRepo.findById(idAtiva)
                .orElseThrow(() -> new IllegalStateException(
                        "Instância ativa não encontrada: " + idAtiva));
    }

    public EntidadeInstancia retornarDeContexto(ExecucaoContexto ctx, String chave) {
        // Lê a representação em String ou Objeto do UUID no contexto e converte com segurança
        String idStr = ctx.getContexto().getStringOrThrow(chave);
        UUID id = UUID.fromString(idStr);
        return instanciaRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Instância não encontrada: " + id));
    }

    public void salvar(EntidadeInstancia instancia) {
        instanciaRepo.save(instancia);
    }

    public List<EntidadeInstancia> resolverDeFonte(String fonte,
                                                   ExecucaoContexto ctx) {
        if (fonte.startsWith("cena.")) {
            return resolverDeCena(fonte, ctx);
        }
        throw new IllegalArgumentException("Fonte desconhecida: " + fonte);
    }

    private List<EntidadeInstancia> resolverDeCena(String fonte,
                                                   ExecucaoContexto ctx) {
        // Lê o ID da cena como String do contexto e converte para UUID
        String idCenaStr = ctx.getContexto().getStringOrThrow("id_cena");
        UUID idCena = UUID.fromString(idCenaStr);

        Cena cena = cenaRepo.findById(idCena)
                .orElseThrow(() -> new IllegalStateException(
                        "Cena não encontrada: " + idCena));

        String sub = fonte.substring("cena.".length());

        List<CenaParticipantes> todosParticipantes = cenaParticipantesRepo.findByCena(cena);

        List<CenaParticipantes> selecionados = switch (sub) {
            case "todos" -> todosParticipantes;

            case "aliados" -> {
                int ladoAtivo = resolverLadoAtivo(cena, ctx, todosParticipantes);
                yield todosParticipantes.stream()
                        .filter(p -> p.getLado() != null && p.getLado() == ladoAtivo)
                        .toList();
            }

            case "inimigos" -> {
                int ladoAtivo = resolverLadoAtivo(cena, ctx, todosParticipantes);
                yield todosParticipantes.stream()
                        .filter(p -> p.getLado() != null && p.getLado() != ladoAtivo)
                        .toList();
            }

            default -> {
                if (sub.startsWith("time.")) {
                    int timeIdx = Integer.parseInt(sub.substring("time.".length()));
                    yield todosParticipantes.stream()
                        .filter(p -> p.getLado() != null && p.getLado() == timeIdx)
                        .toList();
                }
                throw new IllegalArgumentException("Sub-fonte de cena desconhecida: " + sub);
            }
        };

        List<UUID> ids = selecionados.stream()
                .map(p -> p.getEntidadeInstancia().getId())
                .toList();

        return preservarOrdem(ids, instanciaRepo.findAllById(ids));
    }

    private int resolverLadoAtivo(Cena cena, ExecucaoContexto ctx,
                                  List<CenaParticipantes> todosParticipantes) {
        UUID idAtiva = ctx.idInstanciaAtiva();
        return todosParticipantes.stream()
                .filter(p -> p.getEntidadeInstancia() != null && p.getEntidadeInstancia().getId().equals(idAtiva))
                .map(CenaParticipantes::getLado)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Instância ativa não encontrada na cena"));
    }

    private List<EntidadeInstancia> preservarOrdem(List<UUID> ids,
                                                   List<EntidadeInstancia> instancias) {
        Map<UUID, EntidadeInstancia> porId = instancias.stream()
                .collect(Collectors.toMap(EntidadeInstancia::getId, i -> i));
        return ids.stream().map(porId::get).filter(Objects::nonNull).toList();
    }
}