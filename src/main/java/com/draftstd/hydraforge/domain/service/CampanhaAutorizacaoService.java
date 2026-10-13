package com.draftstd.hydraforge.domain.service;

import com.draftstd.hydraforge.adapters.out.repository.campanha.CampanhaUsuarioRepository;
import com.draftstd.hydraforge.adapters.out.repository.personagem.PersonagemRepository;
import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.exceptions.DeniedAcessException;
import com.draftstd.hydraforge.domain.model.campanha.CampanhaUsuario;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CampanhaAutorizacaoService {

    private final CampanhaUsuarioRepository campanhaUsuarioRepository;
    private final PersonagemRepository personagemRepository;

    public CampanhaAutorizacaoService(CampanhaUsuarioRepository campanhaUsuarioRepository,
                                     PersonagemRepository personagemRepository) {
        this.campanhaUsuarioRepository = campanhaUsuarioRepository;
        this.personagemRepository = personagemRepository;
    }

    private Long uuidParaLong(UUID uuid) {
        if (uuid == null) return null;
        return Math.abs(uuid.getMostSignificantBits());
    }

    public CampanhaUsuario exigirMestre(UUID idCampanha, UUID idUsuario) {
        Long campanhaIdLong = uuidParaLong(idCampanha);
        Long usuarioIdLong = uuidParaLong(idUsuario);

        return campanhaUsuarioRepository
                .findByCampanhaIdAndUsuarioIdAndPapel(campanhaIdLong, usuarioIdLong, PapeisUsuario.MESTRE)
                .orElseThrow(() -> new DeniedAcessException(
                        "Usuário " + idUsuario + " não é mestre da campanha " + idCampanha));
    }

    public CampanhaUsuario exigirMembro(UUID idCampanha, UUID idUsuario) {
        Long campanhaIdLong = uuidParaLong(idCampanha);
        Long usuarioIdLong = uuidParaLong(idUsuario);

        return campanhaUsuarioRepository
                .findByCampanhaIdAndUsuarioIdAndPapel(campanhaIdLong, usuarioIdLong, PapeisUsuario.JOGADOR)
                .orElseThrow(() -> new DeniedAcessException(
                        "Usuário " + idUsuario + " não é membro da campanha " + idCampanha));
    }

    public boolean isMestre(UUID idCampanha, UUID idUsuario) {
        Long campanhaIdLong = uuidParaLong(idCampanha);
        Long usuarioIdLong = uuidParaLong(idUsuario);

        return campanhaUsuarioRepository
                .findByCampanhaIdAndUsuarioId(campanhaIdLong, usuarioIdLong)
                .map(cu -> cu.getPapel() == PapeisUsuario.MESTRE)
                .orElse(false);
    }

    public CampanhaUsuario exigirAcessoPersonagem(Personagem personagem, UUID idUsuario) {
        UUID idCampanhaLong = (personagem.getCampanha() != null) ? personagem.getCampanha().getId() : null;
        
        UUID idCampanhaUuid = idCampanhaLong != null 
                ? UUID.nameUUIDFromBytes(idCampanhaLong.toString().getBytes()) 
                : null;

        UUID usuarioIdEntidade = (personagem.getUsuario() != null) ? personagem.getUsuario().getId() : null;

        if (usuarioIdEntidade != null && usuarioIdEntidade.equals(idUsuario)) {
            return exigirMembro(idCampanhaUuid, idUsuario);
        }
        return exigirMestre(idCampanhaUuid, idUsuario);
    }

    public Personagem exigirPersonagemVivo(UUID idCampanha, UUID idUsuario) {
        return personagemRepository
                .findAtivoByCampanhaIdAndUsuarioId(idCampanha, idUsuario)
                .orElseThrow(() -> new DeniedAcessException(
                        "Nenhum personagem vivo nesta campanha para o usuário " + idUsuario));
    }
}