package com.draftstd.hydraforge.ports.in.campanha;

import com.draftstd.hydraforge.application.response.campanha.CampanhaQueryResponse;
import com.draftstd.hydraforge.application.response.campanha.ListarCampanhasPorUsuarioQuery;
import com.draftstd.hydraforge.application.response.campanha.ObterCampanhaQuery;
import java.util.List;

public interface ObterCampanhaUseCase {
    CampanhaQueryResponse obterPorId(ObterCampanhaQuery query);
    List<CampanhaQueryResponse> listarPorUsuario(ListarCampanhasPorUsuarioQuery query);
}