package com.draftstd.hydraforge.config.security;

import com.draftstd.hydraforge.ports.out.security.GerarTokenPort;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final GerarTokenPort gerarTokenPort;

    @Value("${app.oauth2.authorizedRedirectUri}")
    private String authorizedRedirectUri;

    public OAuth2AuthenticationSuccessHandler(GerarTokenPort gerarTokenPort) {
        this.gerarTokenPort = gerarTokenPort;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        Authentication authentication) throws IOException, ServletException {
        
        if (response.isCommitted()) {
            logger.debug("A resposta já foi enviada. Não é possível redirecionar para " + authorizedRedirectUri);
            return;
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail não encontrado no provedor de autenticação.");
        }

        String token = gerarTokenPort.gerarTokenPorEmail(email);

        String targetUrl = UriComponentsBuilder.fromUriString(authorizedRedirectUri)
                .queryParam("token", token)
                .build().toUriString();

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}