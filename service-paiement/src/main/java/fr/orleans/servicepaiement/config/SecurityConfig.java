package fr.orleans.servicepaiement.config;

import fr.orleans.servicepaiement.dtos.ConsulDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)

public class SecurityConfig {
    RSAPublicKey key;

    @Value("${CONSUL_HOST:localhost}")
    private String consulHost;

    @Value("${CONSUL_PORT:8500}")
    private String consulPort;

    @PostConstruct
    public void postConstruct() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ConsulDTO[] results = restTemplate.getForObject("http://" + consulHost + ":" + consulPort + "/v1/kv/config/application/publicKey", ConsulDTO[].class);

        if (results == null || results.length == 0) {
            throw new Exception("Aucune clé trouvée");
        }


        // Décodage Base64
        byte[] decodedKeyBytes = Base64.getDecoder().decode(results[0].Value());

        // Convertir les bytes en une chaîne de caractères
        String decodedKeyString = new String(decodedKeyBytes, StandardCharsets.UTF_8);

        String publicKeyPEM = decodedKeyString
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\n", "");

        // Convertir la chaîne de caractères en un tableau de bytes (byte[])
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

        // Créez une spécification de clé X.509 à partir des données décodées
        X509EncodedKeySpec spec = new X509EncodedKeySpec(encoded);

        // Obtenez une instance de la fabrique de clés RSA
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // Générez la clé publique à partir de la spécification
        PublicKey publicKey = keyFactory.generatePublic(spec);

        // Assurez-vous que la clé est une instance de RSAPublicKey
        if (publicKey instanceof RSAPublicKey) {
            key = (RSAPublicKey) publicKey;
        } else {
            throw new IllegalArgumentException("La clé fournie n'est pas une clé publique RSA valide.");
        }
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET,"/actuator/health").permitAll()
                        .requestMatchers(HttpMethod.GET,"/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers.frameOptions().disable())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oAuth2ResourceServerConfigurer
                        -> oAuth2ResourceServerConfigurer.jwt(Customizer.withDefaults()))
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION,HttpHeaders.CONTENT_TYPE));
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }
    /***
     * la methode jwtDecoder ajoute un préfixe SCOPE celà donne après un role de type SCOPE_ROLE_...
     * Cette methode suprime le préfixe SCOPE pour juste garder le ROLE_...
     * @return
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
