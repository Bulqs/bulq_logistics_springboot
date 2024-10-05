package com.bulq.logistics.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private RSAKey rsaKey;

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (JWKSelector, securityContext) -> JWKSelector.select(jwkSet);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authManager(UserDetailsService userDetailsService) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(authProvider);
    }

    @Bean
    JwtDecoder jwtDecoder() throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwks) {
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // Allow cookies to be included in requests
        config.addAllowedOriginPattern("*"); // Allow all origins
        config.addAllowedHeader("*"); // Allow all headers
        config.addAllowedMethod("*"); // Allow all methods (GET, POST, etc.)

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(
                        csrf -> csrf
                                .ignoringRequestMatchers("/db/console/**"))
                .headers(
                        header -> header.frameOptions().sameOrigin())
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                // .requestMatchers("/**").permitAll()
                                .requestMatchers("/api/v1/auth/token").permitAll()
                                .requestMatchers("/api/v1/auth/users/register").permitAll()
                                .requestMatchers("/api/v1/auth/profile").permitAll()
                                .requestMatchers("/api/v1/booking/pup").authenticated()
                                .requestMatchers("/api/v1/wallet/create-wallet").authenticated()
                                .requestMatchers("/api/v1/wallet/fund-wallet").authenticated()
                                .requestMatchers("/api/v1/wallet/deduct-wallet").authenticated()
                                .requestMatchers("/api/v1/wallet/{walletId}/info")
                                .hasAnyAuthority("SCOPE_USER", "SCOPE_ADMIN")
                                .requestMatchers("/api/v1/link-card").authenticated()
                                .requestMatchers("/api/v1/delete-card").authenticated()
                                .requestMatchers("/api/v1/ratings/rate").authenticated()
                                .requestMatchers("/api/v1/ratings/").hasAnyAuthority("SCOPE_USER", "SCOPE_ADMIN")
                                .requestMatchers("/api/v1/ratings/{ratingId}/view-rating")
                                .hasAnyAuthority("SCOPE_USER", "SCOPE_ADMIN")
                                .requestMatchers("/api/v1/complaints/").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/complaints/create-complaint").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/complaints/update-complaint").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/complaints/{complaintId}/view-complaint")
                                .hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/wallet/{walletId}/info-transactions")
                                .hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/wallet/all-wallets").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/booking/").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/booking/{deliveryCode}").authenticated()
                                .requestMatchers("/api/v1/booking/dp").authenticated()
                                .requestMatchers("/api/v1/booking/bado").authenticated()
                                .requestMatchers("/api/v1/delivery-actions/{deliveryCode}/add-action")
                                .hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/service-catalog/catalogs").permitAll()
                                .requestMatchers("/api/v1/service-catalog/delete-catalog").permitAll()
                                .requestMatchers("/api/v1/service-catalog/update-catalog").permitAll()
                                .requestMatchers("/api/v1/service-catalog/create-service").permitAll()
                                .requestMatchers("/api/v1/hubs/create-hub").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/hubs/all").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/hubs/").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/hubs/update-hub").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/hubs/add-hub-telephone").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/hubs/create-hub-workinghours").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/hubs/delete-hub").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/auth/users").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/auth/profile").authenticated()
                                .requestMatchers("/api/v1/auth/profile/delete").authenticated()
                                .requestMatchers("/api/v1/auth/users/change-password").authenticated()
                                .requestMatchers("/api/v1/auth/users/reset-password").permitAll()
                                .requestMatchers("/api/v1/auth//profile/update-password").authenticated()
                                .requestMatchers("/api/v1/auth/users/{userId}/update-authorities")
                                .hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/v1/auth/users/verification-lnk/**").permitAll()
                                .requestMatchers("/api/v1/auth/user/profile/{userId}/update-profile").permitAll()
                                .requestMatchers("/api/v1/auth/home").authenticated()
                                .requestMatchers("/api/v1/notifications/send-notification")
                                .hasAnyAuthority("SCOPE_USER", "SCOPE_ADMIN")
                                .requestMatchers("/api/v1/notifications/{accountId}/view-notification")
                                .hasAnyAuthority("SCOPE_USER", "SCOPE_ADMIN")
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.csrf(
                csrf -> csrf.disable());
        return http.build();
    }
}
