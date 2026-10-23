package com.draftstd.hydraforge.adapters.out.persistence.personagem.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.personagem.entity.PersonagemJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.personagem.mapper.PersonagemMapper;
import com.draftstd.hydraforge.adapters.out.persistence.personagem.repository.PersonagemRepository;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.out.persistence.personagem.DeletarPersonagemPort;
import com.draftstd.hydraforge.ports.out.persistence.personagem.ObterPersonagemPort;
import com.draftstd.hydraforge.ports.out.persistence.personagem.SalvarPersonagemPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PersonagemPersistenceAdapter implements SalvarPersonagemPort, ObterPersonagemPort, DeletarPersonagemPort {

    private final PersonagemRepository personagemRepository;
    private final PersonagemMapper personagemMapper;

    public PersonagemPersistenceAdapter(PersonagemRepository personagemRepository, PersonagemMapper personagemMapper) {
        this.personagemRepository = personagemRepository;
        this.personagemMapper = personagemMapper;
    }

    @Override
    public void salvar(Personagem personagem) {
        PersonagemJpaEntity entity = personagemMapper.toEntity(personagem);
        personagemRepository.save(entity);
    }

    @Override
    public Optional<Personagem> buscarPorId(UUID id) {
        return personagemRepository.findById(id)
                .map(personagemMapper::toDomain);
    }

    @Override
    public List<Personagem> listarPorUsuarioId(UUID usuarioId) {
        return personagemRepository.findAllByUsuarioId(usuarioId).stream()
                .map(personagemMapper::toDomain)
                .toList();
    }

    @Override
    public List<Personagem> listarPorCampanhaId(UUID campanhaId) {
        return personagemRepository.findAllByCampanhaId(campanhaId).stream()
                .map(personagemMapper::toDomain)
                .toList();
    }

    @Override
    public void deletarPorId(UUID id) {
        personagemRepository.deleteById(id);
    }

    @Override
    public Optional<Personagem> buscarAtivoPorCampanhaEUsuario(UUID campanhaId, UUID usuarioId) {
        return personagemRepository.findByCampanhaIdAndUsuarioIdAndAtivo(campanhaId, usuarioId, true)
                .map(personagemMapper::toDomain);
    }
}