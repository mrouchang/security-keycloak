package org.security.keycloak.springgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

@Configuration
public class OAuth2Config {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.keycloakClientRegistration());
    }

    private ClientRegistration keycloakClientRegistration() {
        return ClientRegistration.withRegistrationId("keycloak")
                .clientId("your-client-id")
                .clientSecret("your-client-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email")
                .authorizationUri("http://localhost:8080/realms/your-realm/protocol/openid-connect/auth")
                .tokenUri("http://localhost:8080/realms/your-realm/protocol/openid-connect/token")
                .userInfoUri("http://localhost:8080/realms/your-realm/protocol/openid-connect/userinfo")
                .userNameAttributeName("preferred_username")
                .jwkSetUri("http://localhost:8080/realms/your-realm/protocol/openid-connect/certs")
                .issuerUri("http://localhost:8080/realms/your-realm")
                .clientName("Keycloak")
                .build();
    }
}
