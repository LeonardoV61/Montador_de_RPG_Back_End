package com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto;

import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.domain.model.sessao.CenaParticipante;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.exceptions.EntityNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaParticipanteOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaPersistencePort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;
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

    private final EntidadeInstanciaOutputPort instanciaPort;
    private final CenaPersistencePort cenaPort;
    private final CenaParticipanteOutputPort cenaParticipantesPort;

    public List<EntidadeInstancia> retornarTodas(ExecucaoContexto ctx) {
        List<UUID> ids = ctx.idsInstancias();
        if (ids == null || ids.isEmpty()) return List.of();

        List<EntidadeInstancia> instancias = instanciaPort.buscarPorIds(ids);
        Map<UUID, EntidadeInstancia> porId = instancias.stream()
                .collect(Collectors.toMap(EntidadeInstancia::getId, i -> i));

        return ids.stream().map(porId::get).filter(Objects::nonNull).toList();
    }

    public EntidadeInstancia buscarPorId(UUID id) {
        return instanciaPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException(EntidadeInstancia.class, id));
    }

    public EntidadeInstancia retornarAtiva(ExecucaoContexto ctx) {
        UUID idAtiva = ctx.idInstanciaAtiva();
        return instanciaPort.buscarPorId(idAtiva)
                .orElseThrow(() -> new IllegalStateException(
                        "Instância ativa não encontrada: " + idAtiva));
    }

    public EntidadeInstancia retornarDeContexto(ExecucaoContexto ctx, String chave) {
        String idStr = ctx.getContexto().getStringOrThrow(chave);
        UUID id = UUID.fromString(idStr);
        return instanciaPort.buscarPorId(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Instância não encontrada: " + id));
    }

    public void salvar(EntidadeInstancia instancia) {
        instanciaPort.salvar(instancia);
    }

    public List<EntidadeInstancia> resolverDeFonte(String fonte, ExecucaoContexto ctx) {
        if (fonte.startsWith("cena.")) {
            return resolverDeCena(fonte, ctx);
        }
        throw new IllegalArgumentException("Fonte desconhecida: " + fonte);
    }

    private List<EntidadeInstancia> resolverDeCena(String fonte, ExecucaoContexto ctx) {
        String idCenaStr = ctx.getContexto().getStringOrThrow("id_cena");
        UUID idCena = UUID.fromString(idCenaStr);

        Cena cena = cenaPort.buscarPorId(idCena)
                .orElseThrow(() -> new IllegalStateException("Cena não encontrada: " + idCena));

        String sub = fonte.substring("cena.".length());
        List<CenaParticipante> todosParticipantes = cenaParticipantesPort.buscarPorCenaId(cena.getId());

        List<CenaParticipante> selecionados = switch (sub) {
            case "todos" -> todosParticipantes;

            case "aliados" -> {
                int ladoAtivo = resolverLadoAtivo(cena, ctx, todosParticipantes);
                yield todosParticipantes.stream()
                        .filter(p -> p.lado() != null && p.lado() == ladoAtivo)
                        .toList();
            }

            case "inimigos" -> {
                int ladoAtivo = resolverLadoAtivo(cena, ctx, todosParticipantes);
                yield todosParticipantes.stream()
                        .filter(p -> p.lado() != null && p.lado() != ladoAtivo)
                        .toList();
            }

            default -> {
                if (sub.startsWith("time.")) {
                    int timeIdx = Integer.parseInt(sub.substring("time.".length()));
                    yield todosParticipantes.stream()
                        .filter(p -> p.lado() != null && p.lado() == timeIdx)
                        .toList();
                }
                throw new IllegalArgumentException("Sub-fonte de cena desconhecida: " + sub);
            }
        };

        List<UUID> ids = selecionados.stream()
                .map(CenaParticipante::instanciaId)
                .toList();

        return preservarOrdem(ids, instanciaPort.buscarPorIds(ids));
    }

    private int resolverLadoAtivo(Cena cena, ExecucaoContexto ctx, List<CenaParticipante> todosParticipantes) {
        UUID idAtiva = ctx.idInstanciaAtiva();
        return todosParticipantes.stream()
                .filter(p -> p.instanciaId() != null && p.instanciaId().equals(idAtiva))
                .map(CenaParticipante::lado)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Instância ativa não encontrada na cena"));
    }

    private List<EntidadeInstancia> preservarOrdem(List<UUID> ids, List<EntidadeInstancia> instancias) {
        Map<UUID, EntidadeInstancia> porId = instancias.stream()
                .collect(Collectors.toMap(EntidadeInstancia::getId, i -> i));
        return ids.stream().map(porId::get).filter(Objects::nonNull).toList();
    }
}