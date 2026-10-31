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
                .clientId("minwei-keycloak-confidential")
                .clientSecret("sqHRMwCWIX6m9Fazo5rUsFIZDkiufgwjd0M1phJkZBuuLzHKnMy799JiQusc8pOJANs6AqynbPNmTmoOn4ibK3")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email")
                .authorizationUri("http://localhost:8080/realms/minwei_keycloak/protocol/openid-connect/auth")
                .tokenUri("http://localhost:8080/realms/minwei_keycloak/protocol/openid-connect/token")
                .userInfoUri("http://localhost:8080/realms/minwei_keycloak/protocol/openid-connect/userinfo")
                .userNameAttributeName("preferred_username")
                .jwkSetUri("http://localhost:8080/realms/minwei_keycloak/protocol/openid-connect/certs")
                .issuerUri("http://localhost:8080/realms/minwei_keycloak")
                .clientName("minwei-keycloak")
                .build();
    }
}
