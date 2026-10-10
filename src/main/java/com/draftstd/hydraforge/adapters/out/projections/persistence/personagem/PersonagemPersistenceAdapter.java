package com.draftstd.hydraforge.adapters.out.projections.persistence.personagem;

import com.draftstd.hydraforge.adapters.out.repository.personagem.PersonagemRepository;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.out.personagem.DeletarPersonagemPort;
import com.draftstd.hydraforge.ports.out.personagem.ObterPersonagemPort;
import com.draftstd.hydraforge.ports.out.personagem.SalvarPersonagemPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PersonagemPersistenceAdapter implements SalvarPersonagemPort, ObterPersonagemPort, DeletarPersonagemPort {

    private final PersonagemRepository personagemRepository;

    public PersonagemPersistenceAdapter(PersonagemRepository personagemRepository) {
        this.personagemRepository = personagemRepository;
    }

    @Override
    public Personagem salvar(Personagem personagem) {
        return personagemRepository.save(personagem);
    }

    @Override
    public Optional<Personagem> buscarPorId(UUID id) {
        return personagemRepository.findById(id);
    }

    @Override
    public List<Personagem> listarPorUsuarioId(UUID usuarioId) {
        return personagemRepository.findAllByUsuarioId(usuarioId);
    }

    @Override
    public List<Personagem> listarPorCampanhaId(UUID campanhaId) {
        return personagemRepository.findAllByCampanhaId(campanhaId);
    }

    @Override
    public void deletarPorId(UUID id) {
        personagemRepository.deleteById(id);
    }
}