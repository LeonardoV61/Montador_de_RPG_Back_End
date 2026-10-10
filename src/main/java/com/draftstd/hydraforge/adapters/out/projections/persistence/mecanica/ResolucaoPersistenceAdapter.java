package com.draftstd.hydraforge.adapters.out.projections.persistence.mecanica;

import com.draftstd.hydraforge.adapters.out.repository.mecanica.ResolucaoRepository;
import com.draftstd.hydraforge.ports.in.query.mecanica.ResultadoRolagemQueryResponse;
import com.draftstd.hydraforge.ports.out.mecanica.SalvarHistoricoRolagemPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ResolucaoPersistenceAdapter implements SalvarHistoricoRolagemPort {

    private final ResolucaoRepository resolucaoRepository;

    public ResolucaoPersistenceAdapter(ResolucaoRepository resolucaoRepository) {
        this.resolucaoRepository = resolucaoRepository;
    }

    @Override
    public void salvar(ResultadoRolagemQueryResponse resultadoRolagem) {
        // Mapeamento do DTO/QueryResponse para a entidade JPA Resolucao/HistoricoRolagem
        // e persistência através do repository
    }

    @Override
    public List<ResultadoRolagemQueryResponse> buscarPorSessaoId(UUID sessaoId, Integer limite) {
        // Consulta no repositório por histórico de rolagens na sessão limitada por N registros
        return List.of();
    }
}