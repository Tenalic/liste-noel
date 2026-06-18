package sc.liste.noel.liste_noel.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import sc.liste.noel.liste_noel.back.service.JwtAuthFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Value("${url.front}")
    private String urlFront;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of(
                            urlFront
                    ));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                // Désactive CSRF : on est protégé par le cookie SameSite=Strict + JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Pas de session côté serveur : le JWT suffit
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"messageRetour\":\"Non autorisé\",\"codeRetour\":1}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"messageRetour\":\"Accès refusé\",\"codeRetour\":1}");
                        })
                )
                // Définition des routes publiques et protégées
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/liste/*").permitAll()
                        // Tout le reste de /api/liste/** nécessite d'être connecté
                        .requestMatchers("/api/liste/**").authenticated()
                        // Routes publiques (pas besoin d'être connecté)
                        .requestMatchers(
                                "/api/compte/**",
                                "/api/liste/*",
                                "/error",
                                "/health"
                        ).permitAll()
                        // Toutes les autres routes nécessitent d'être connecté
                        .anyRequest().authenticated()
                )

                // On branche notre filtre JWT avant le filtre d'authentification de Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}