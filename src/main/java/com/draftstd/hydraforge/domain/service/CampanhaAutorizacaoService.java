package com.draftstd.hydraforge.domain.service;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.exceptions.DeniedAcessException;
import com.draftstd.hydraforge.domain.model.campanha.CampanhaMembro;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.out.persistence.campanha.ObterCampanhaMembroPort;
import com.draftstd.hydraforge.ports.out.persistence.personagem.ObterPersonagemPort;

import java.util.UUID;

public class CampanhaAutorizacaoService {

    private final ObterCampanhaMembroPort obterCampanhaMembroPort;
    private final ObterPersonagemPort obterPersonagemPort;

    public CampanhaAutorizacaoService(
            ObterCampanhaMembroPort obterCampanhaMembroPort,
            ObterPersonagemPort obterPersonagemPort
    ) {
        this.obterCampanhaMembroPort = obterCampanhaMembroPort;
        this.obterPersonagemPort = obterPersonagemPort;
    }

    public CampanhaMembro exigirMestre(UUID idCampanha, UUID idUsuario) {
        validarParametros(idCampanha, idUsuario);
        return obterCampanhaMembroPort
                .buscarPorCampanhaEUsuarioEPapel(idCampanha, idUsuario, PapeisUsuario.MESTRE)
                .orElseThrow(() -> new DeniedAcessException(
                        "Usuário " + idUsuario + " não é mestre da campanha " + idCampanha));
    }

    public CampanhaMembro exigirMembro(UUID idCampanha, UUID idUsuario) {
        validarParametros(idCampanha, idUsuario);
        // Mestre ou Jogador são membros válidos da campanha
        return obterCampanhaMembroPort
                .buscarPorCampanhaEUsuario(idCampanha, idUsuario)
                .orElseThrow(() -> new DeniedAcessException(
                        "Usuário " + idUsuario + " não é membro da campanha " + idCampanha));
    }

    public boolean isMestre(UUID idCampanha, UUID idUsuario) {
        if (idCampanha == null || idUsuario == null) return false;
        return obterCampanhaMembroPort
                .buscarPorCampanhaEUsuario(idCampanha, idUsuario)
                .map(membro -> membro.papel() == PapeisUsuario.MESTRE)
                .orElse(false);
    }

    public CampanhaMembro exigirAcessoPersonagem(Personagem personagem, UUID idUsuario) {
        if (personagem == null) {
            throw new DeniedAcessException("Personagem não informado para verificação de acesso.");
        }

        UUID idCampanhaUuid = personagem.getCampanhaId();
        UUID usuarioIdEntidade = personagem.getUsuarioId();

        if (usuarioIdEntidade != null && usuarioIdEntidade.equals(idUsuario)) {
            return exigirMembro(idCampanhaUuid, idUsuario);
        }
        return exigirMestre(idCampanhaUuid, idUsuario);
    }

    public Personagem exigirPersonagemVivo(UUID idCampanha, UUID idUsuario) {
        validarParametros(idCampanha, idUsuario);
        return obterPersonagemPort
                .buscarAtivoPorCampanhaEUsuario(idCampanha, idUsuario)
                .orElseThrow(() -> new DeniedAcessException(
                        "Nenhum personagem vivo nesta campanha para o usuário " + idUsuario));
    }

    private void validarParametros(UUID idCampanha, UUID idUsuario) {
        if (idCampanha == null || idUsuario == null) {
            throw new DeniedAcessException("ID da campanha e ID do usuário são obrigatórios para validação de acesso.");
        }
    }
}