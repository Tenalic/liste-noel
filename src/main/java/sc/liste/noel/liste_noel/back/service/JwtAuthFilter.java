package sc.liste.noel.liste_noel.back.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

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

        String path = request.getRequestURI();

        // On laisse passer les URLs publiques sans vérifier le JWT
        if (URLS_PUBLIQUES.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        // On cherche le cookie "auth-token" dans la requête
        Optional<String> token = extraireTokenDuCookie(request);

        if (token.isPresent() && jwtService.estValide(token.get())) {
            // Token valide → on identifie l'utilisateur dans le contexte Spring Security
            String email = jwtService.extraireEmail(token.get());

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(email, null, List.of());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        // Token absent ou invalide → SecurityContext reste vide
        // Spring Security refusera l'accès aux endpoints protégés (401)

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