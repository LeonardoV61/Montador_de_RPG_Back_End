package com.draftstd.hydraforge.ports.out.persistence.campanha;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.model.campanha.CampanhaMembro;

import java.util.Optional;
import java.util.UUID;

public interface ObterCampanhaMembroPort {
    Optional<CampanhaMembro> buscarPorCampanhaEUsuarioEPapel(UUID idCampanha, UUID idUsuario, PapeisUsuario papel);
    Optional<CampanhaMembro> buscarPorCampanhaEUsuario(UUID idCampanha, UUID idUsuario);
}