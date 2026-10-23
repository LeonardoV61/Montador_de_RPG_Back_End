package com.draftstd.hydraforge.adapters.in.dto.campanha;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.enums.StatusMembroCampanha;
import jakarta.validation.constraints.NotNull;

public record CampanhaPapelDTO(@NotNull(message = "O novo papel é obrigatório") PapeisUsuario novoPapel, StatusMembroCampanha novoStatus) {

}