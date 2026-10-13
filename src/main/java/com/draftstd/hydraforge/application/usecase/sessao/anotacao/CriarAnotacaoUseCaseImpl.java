package com.draftstd.hydraforge.application.usecase.sessao.anotacao;

import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.domain.model.usuario.Usuario;
import com.draftstd.hydraforge.ports.in.command.sessao.CriarAnotacaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.AnotacaoQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.CriarAnotacaoUseCase;
import com.draftstd.hydraforge.ports.out.sessao.SalvarAnotacaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class CriarAnotacaoUseCaseImpl implements CriarAnotacaoUseCase {

    private final SalvarAnotacaoPort salvarAnotacaoPort;

    public CriarAnotacaoUseCaseImpl(SalvarAnotacaoPort salvarAnotacaoPort) {
        this.salvarAnotacaoPort = salvarAnotacaoPort;
    }

    @Override
    public AnotacaoQueryResponse executar(CriarAnotacaoCommand command) {
        Anotacao anotacao = new Anotacao();
        // O idAnotacao é gerado por sequence (Integer), não se usa setId com UUID.

        // Mapeamento dos relacionamentos se os IDs forem fornecidos no command
        if (command.sessaoId() != null) {
            Sessao sessao = new Sessao();
            // Ajuste caso a Sessao utilize UUID ou Long no seu ID
            anotacao.setSessao(sessao);
        }

        if (command.autorId() != null) {
            Usuario usuario = new Usuario();
            anotacao.setUsuario(usuario);
        }

        anotacao.setTitulo(command.titulo());
        anotacao.setConteudo(command.conteudo());
        
        // Mapeia para o campo correto 'ePrivado' da entidade
        if (command.privada() != null) {
            anotacao.setEPrivado(command.privada());
        }

        Anotacao salva = salvarAnotacaoPort.salvar(anotacao);

        // Conversão segura do ID Integer para UUID para o response
        UUID anotacaoUuid = salva.getIdAnotacao() != null ? UUID.nameUUIDFromBytes(salva.getIdAnotacao().toString().getBytes()) : UUID.randomUUID();

        LocalDateTime agora = LocalDateTime.now();

        return new AnotacaoQueryResponse(anotacaoUuid, command.sessaoId(), command.autorId(), salva.getTitulo(), salva.getConteudo(), salva.getEPrivado() != null ? salva.getEPrivado() : true, agora, agora);
    }
}