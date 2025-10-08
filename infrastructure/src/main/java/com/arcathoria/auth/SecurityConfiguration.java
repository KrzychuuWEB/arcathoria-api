package com.arcathoria.auth;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity http,
            final AccountJwtAuthenticationConverter accountJwtAuthenticationConverter
    ) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/authenticate", "/accounts/register", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(accountJwtAuthenticationConverter))
                )
                .build();
    }

    @Bean
    AccountJwtAuthenticationConverter accountJwtAuthenticationConverter() {
        return new AccountJwtAuthenticationConverter();
    }

    @Bean
    JwtDecoder jwtDecoder(final JwtConfigurationProperties jwtConfigurationProperties) {
        byte[] keyBytes = Base64.getDecoder().decode(jwtConfigurationProperties.getSecret());
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    JwtEncoder jwtEncoder(final JwtConfigurationProperties properties) {
        SecretKey secretKey = secretKey(properties);
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }

    private SecretKey secretKey(final JwtConfigurationProperties properties) {
        byte[] keyBytes = Base64.getDecoder().decode(properties.getSecret());
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }
}
