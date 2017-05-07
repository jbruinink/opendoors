package com.jdriven.opendoors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.Arrays;

@Configuration
@EnableOAuth2Client
public class GoogleOpenIdConnectConfig {
    private final String clientId;
    private final String clientSecret;
    private final String accessTokenUri;
    private final String userAuthorizationUri;
    private final String redirectUri;

    public GoogleOpenIdConnectConfig(@Value("${google.clientId}") String clientId,
                                     @Value("${google.clientSecret}") String clientSecret,
                                     @Value("${google.accessTokenUri}")String accessTokenUri,
                                     @Value("${google.userAuthorizationUri}")String userAuthorizationUri,
                                     @Value("${google.redirectUri}") String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUri = accessTokenUri;
        this.userAuthorizationUri = userAuthorizationUri;
        this.redirectUri = redirectUri;
    }

    @Bean
    public OAuth2ProtectedResourceDetails googleOpenId() {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri(accessTokenUri);
        details.setUserAuthorizationUri(userAuthorizationUri);
        details.setScope(Arrays.asList("openid", "email"));
        details.setPreEstablishedRedirectUri(redirectUri);
        details.setUseCurrentUri(false);
        return details;
    }

    @Bean
    public OAuth2RestTemplate googleOpenIdTemplate(OAuth2ClientContext clientContext) {
        return new OAuth2RestTemplate(googleOpenId(), clientContext);
    }

}
