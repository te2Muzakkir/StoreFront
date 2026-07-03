package com.storefront.user.security.config;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
	
    @NotBlank(message = "JWT issuer must not be blank")
    private String issuer;
    @NotBlank(message = "JWT algorithm must not be blank")
    private String algorithm;
    @Min(value = 60, message = "Access token validity must be at least 60 seconds")
    private long accessTokenValidity;
    private Duration clockSkew = Duration.ofSeconds(60);
    @NotEmpty(message = "JWT audience must not be empty")
    private List<String> audience;
    @NotBlank(message = "Private key location must not be blank")
    private String privateKeyLocation;
    @NotBlank(message = "Public key location must not be blank")
    private String publicKeyLocation;

}