package com.draftstd.hydraforge.ports.in.campanha;

import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaQueryResponse;
import com.draftstd.hydraforge.ports.in.query.campanha.ListarCampanhasPorUsuarioQuery;
import com.draftstd.hydraforge.ports.in.query.campanha.ObterCampanhaQuery;
import java.util.List;

public interface ObterCampanhaUseCase {
    CampanhaQueryResponse obterPorId(ObterCampanhaQuery query);
    List<CampanhaQueryResponse> listarPorUsuario(ListarCampanhasPorUsuarioQuery query);
}