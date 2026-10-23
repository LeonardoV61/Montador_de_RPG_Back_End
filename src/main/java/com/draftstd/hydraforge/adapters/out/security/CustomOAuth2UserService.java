package com.draftstd.hydraforge.adapters.out.security;

import com.draftstd.hydraforge.domain.model.usuario.Usuario;
import com.draftstd.hydraforge.ports.out.persistence.usuario.ObterUsuarioPort;
import com.draftstd.hydraforge.ports.out.persistence.usuario.SalvarUsuarioPort;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final ObterUsuarioPort obterUsuarioPort;
    private final SalvarUsuarioPort salvarUsuarioPort;

    public CustomOAuth2UserService(ObterUsuarioPort obterUsuarioPort, SalvarUsuarioPort salvarUsuarioPort) {
        this.obterUsuarioPort = obterUsuarioPort;
        this.salvarUsuarioPort = salvarUsuarioPort;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            return processOAuth2User(registrationId, oAuth2User);
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(String registrationId, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail não encontrado no provedor de autenticação.");
        }

        String apelido = "";
        String providerId = "";

        if ("google".equalsIgnoreCase(registrationId)) {
            apelido = (String) attributes.get("name");
            providerId = (String) attributes.get("sub");
        } else if ("discord".equalsIgnoreCase(registrationId)) {
            apelido = (String) attributes.get("username");
            providerId = (String) attributes.get("id");
        }

        if (apelido == null || apelido.isBlank()) {
            apelido = "Utilizador_RPG";
        }

        Optional<Usuario> usuarioOptional = obterUsuarioPort.buscarPorEmail(email);
        Usuario usuario;

        if (usuarioOptional.isPresent()) {
            usuario = usuarioOptional.get();
            usuario.atualizarPerfil(apelido, usuario.getUrlImagem());
            salvarUsuarioPort.salvar(usuario);
        } else {
            usuario = registerNewUser(email, apelido, registrationId, providerId);
        }

        return oAuth2User;
    }

    private Usuario registerNewUser(String email, String apelido, String provider, String providerId) {
        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                email,
                null,          // senha é nula para logins via OAuth2
                apelido,
                null,          // urlImagem
                false,         // eAdmin
                provider,
                providerId
        );
        
        salvarUsuarioPort.salvar(usuario);
        return usuario;
    }
}