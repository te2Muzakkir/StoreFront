package com.storefront.gatewayserver.security.config;

import static com.storefront.gatewayserver.config.GatewayConstants.USER_ROLE_CUSTOMER;

import java.util.Collection;

import static com.storefront.gatewayserver.config.GatewayConstants.USER_ROLE_ADMIN;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.storefront.gatewayserver.config.GatewayConstants;
import com.storefront.gatewayserver.security.AccessDeniedHandlerImpl;
import com.storefront.gatewayserver.security.AuthenticationEntryPointImpl;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Configuration
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final ReactiveJwtDecoder jwtDecoder;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AccessDeniedHandlerImpl accessDeniedHandler;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    	JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
    	authoritiesConverter.setAuthoritiesClaimName("roles");
    	authoritiesConverter.setAuthorityPrefix("ROLE_");
    	ReactiveJwtAuthenticationConverter authenticationConverter = new ReactiveJwtAuthenticationConverter();
    	authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {

    	    Collection<GrantedAuthority> authorities = authoritiesConverter.convert(jwt);

    	    System.out.println("JWT Claims : " + jwt.getClaims());
    	    System.out.println("Authorities : " + authorities);

    	    return Flux.fromIterable(authorities);
    	});
    	return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
    			.formLogin(ServerHttpSecurity.FormLoginSpec::disable)
    			.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
    			.logout(ServerHttpSecurity.LogoutSpec::disable)
    			.authorizeExchange(exchange -> exchange
    					.pathMatchers(GatewayConstants.WHITELISTED_URL).permitAll()
    					.pathMatchers(HttpMethod.OPTIONS).permitAll()
    					.pathMatchers("/actuator/**").hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers("/storefront/user/api/users/**").hasAnyRole(USER_ROLE_CUSTOMER, USER_ROLE_ADMIN)
    				    .pathMatchers("/storefront/product/api/category/**").hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers(HttpMethod.GET, "/storefront/product/api/product/**").hasAnyRole(USER_ROLE_CUSTOMER, USER_ROLE_ADMIN)
    				    .pathMatchers(HttpMethod.POST, "/storefront/product/api/product/**").hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers(HttpMethod.PUT, "/storefront/product/api/product/**").hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers(HttpMethod.DELETE, "/storefront/product/api/product/**").hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers("/storefront/inventory/api/inventory/**").hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers("/storefront/inventory/api/order/dlq/**") .hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers("/storefront/inventory/admin/outbox/**").hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers("/storefront/payment/api/payment/**").hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers("/storefront/payment/api/order/dlq/**") .hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers("/storefront/payment/admin/outbox/**").hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers("/storefront/order/api/order/customer/**").hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers("/storefront/order/api/order/dlq/**") .hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers("/storefront/order/admin/outbox/**").hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers("/storefront/order/admin/saga/**").hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers(HttpMethod.PUT, "/storefront/order/api/order/**").hasRole(USER_ROLE_ADMIN)
    				    .pathMatchers("/storefront/order/api/order/**").hasAnyRole(USER_ROLE_CUSTOMER, USER_ROLE_ADMIN)
    				    .pathMatchers("/fallback/**").hasRole(USER_ROLE_ADMIN)
    					.anyExchange().authenticated())
    			.oauth2ResourceServer(oauth2 -> oauth2
    					.authenticationEntryPoint(authenticationEntryPoint)
    				    .accessDeniedHandler(accessDeniedHandler)
    					.jwt(jwt -> jwt
    							.jwtDecoder(jwtDecoder)
    							.jwtAuthenticationConverter(authenticationConverter)))
    			.build();
    }

}