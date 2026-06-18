package sc.liste.noel.liste_noel.back.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sc.liste.noel.liste_noel.back.ressource.ListeRessource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LogManager.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;

    // URLs qui ne nécessitent PAS d'être connecté
    private static final List<String> URLS_PUBLIQUES = List.of(
            "/api/compte/connexion",
            "/api/compte/inscription",
            "/api/compte/mot-de-passe-oublie"
    );

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        LOGGER.info("JwtAuthFilter - URI: {}", request.getRequestURI());

        Optional<String> token = extraireTokenDuCookie(request);

        LOGGER.info("JwtAuthFilter - Cookie présent: {}", token.isPresent());

        if (token.isPresent()) {
            LOGGER.info("JwtAuthFilter - Token valide: {}", jwtService.estValide(token.get()));
        }

        if (token.isPresent() && jwtService.estValide(token.get())) {
            String email = jwtService.extraireEmail(token.get());
            LOGGER.info("JwtAuthFilter - Email extrait: {}", email);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(email, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    // Cherche le cookie "auth-token" parmi tous les cookies de la requête
    private Optional<String> extraireTokenDuCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "auth-token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}