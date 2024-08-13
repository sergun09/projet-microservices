package fr.orleans.serviceauthentification.config;


import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import fr.orleans.serviceauthentification.entities.Utilisateur;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class AuthentificationConfig {

    RSAPublicKey key;
    RSAPrivateKey priv;

    @Value("${CONSUL_HOST:localhost}")
    private String consulHost;

    @Value("${CONSUL_PORT:8500}")
    private int consulPort;

    /**
     * Géneration de la clé public et privée puis envoi de la clé public à consul
     */
    @PostConstruct
    public void postConstruct() {
        try {
            // Génération d'une paire de clés RSA de 2048 bits
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Récupération de la clé privée
            priv = (RSAPrivateKey) keyPair.getPrivate();

            // Récupération de la clé publique
            PublicKey publicKey = keyPair.getPublic();
            key = (RSAPublicKey) keyPair.getPublic();

            // Conversion de la clé publique en format PEM
            String publicKeyPEM = convertToPEM(publicKey);

            // Envoi de la clé publique à Consul
            sendPublicKeyToConsul(publicKeyPEM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private String convertToPEM(Key key) {
        byte[] keyBytes = key.getEncoded();
        return "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getEncoder().encodeToString(keyBytes) +
                "\n-----END PUBLIC KEY-----";
    }

    private void sendPublicKeyToConsul(String publicKeyPEM) {
        String consulUrl = "http://" + consulHost + ":" + consulPort + "/v1/kv/config/application/publicKey";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(consulUrl, publicKeyPEM);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/authentification/inscriptions").permitAll()
                        .requestMatchers(HttpMethod.POST, "/authentification/inscriptions").permitAll()
                        .requestMatchers(HttpMethod.POST,"/authentification/login").permitAll()
                        .requestMatchers(HttpMethod.GET,"/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/v3/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/actuator/health").permitAll()
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

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public PasswordEncoder delegatingPasswordEncoder() {
        String idForEncode = "bcrypt";
        ;
        PasswordEncoder defaultEncoder = new BCryptPasswordEncoder();
        Map<String, PasswordEncoder> encoders = Map.of(
                idForEncode, defaultEncoder,
                "noop", NoOpPasswordEncoder.getInstance(),
                "scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v4_1(),
                "sha256", new StandardPasswordEncoder()
        );

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }


    @Bean
    Function<Utilisateur, String> genereTokenFunction(JwtEncoder jwtEncoder) {
        return utilisateur -> {
            Instant now = Instant.now();
            long expiry = 36000L;

            List<String> roleNames = utilisateur.getRoles().stream()
                    .map(Enum::name)
                    .toList();

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(utilisateur.getEmail())
                    .claim("roles",  roleNames)
                    .claim("idUtilisateur",utilisateur.getIdUtilisateur())
                    .claim("prenom",utilisateur.getPrenom())
                    .claim("nom",utilisateur.getNom())
                    .build();

            return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        };
    }

    /***
     * la methode jwtDecoder ajoute un préfixe SCOPE au role et celà donne SCOPE_ROLE_X
     * Cette methode supprime le préfixe pour avoir ROLE_X
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
