package com.storefront.gatewayserver.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import com.storefront.gatewayserver.config.JwtProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtDecoderConfig {

    private final RsaKeyLoader rsaKeyLoader;
    private final JwtProperties jwtProperties;
    private final GatewayJwtValidator gatewayJwtValidator;

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
    	NimbusReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder.withPublicKey(rsaKeyLoader.getPublicKey()).build();
    	OAuth2TokenValidator<Jwt> defaultValidator = JwtValidators.createDefaultWithIssuer(jwtProperties.getIssuer());
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(defaultValidator, gatewayJwtValidator);
        decoder.setJwtValidator(validator);
    	return decoder;
    }

}