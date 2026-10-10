package com.draftstd.hydraforge.adapters.out.repository.campanha;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.model.campanha.CampanhaUsuario;
import com.draftstd.hydraforge.domain.model.campanha.CampanhaUsuarioKey;
import com.draftstd.hydraforge.adapters.in.dto.campanha.CampanhaParticipanteResponseDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampanhaUsuarioRepository extends JpaRepository<CampanhaUsuario, CampanhaUsuarioKey> {

    Optional<CampanhaUsuario> findByCampanhaIdAndUsuarioIdAndPapel(Long idCampanha, Long idUsuario, PapeisUsuario papel);

    List<CampanhaUsuario> findByCampanhaId(Long campanhaId);

    Optional<CampanhaUsuario> findByCampanhaIdAndUsuarioId(Long idCampanha, Long idUsuario);
}