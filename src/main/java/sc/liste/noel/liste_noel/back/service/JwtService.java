package sc.liste.noel.liste_noel.back.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    // Injectés depuis application.properties → jamais en dur dans le code
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationEnSecondes;

    // --- Génération de la clé de signature ---
    // On la recalcule à partir du secret à chaque fois (stateless)
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // --- Générer un token JWT ---
    // Appelé après une connexion réussie
    // Le "subject" = identifiant de l'utilisateur (ici l'email)
    public String genererToken(String email) {
        Date maintenant = new Date();
        Date expiration = new Date(maintenant.getTime() + expirationEnSecondes * 1000);

        return Jwts.builder()
                .subject(email)                     // qui est l'utilisateur
                .issuedAt(maintenant)               // quand le token a été créé
                .expiration(expiration)             // quand il expire
                .signWith(getSigningKey())          // signature avec notre clé secrète
                .compact();
    }

    // --- Extraire l'email depuis un token ---
    // Utilisé dans les endpoints protégés pour savoir qui fait la requête
    public String extraireEmail(String token) {
        return extraireClaims(token).getSubject();
    }

    // --- Vérifier si un token est valide ---
    // Retourne true si le token est bien signé ET non expiré
    public boolean estValide(String token) {
        try {
            Claims claims = extraireClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // Token mal formé, signature invalide, ou expiré
            return false;
        }
    }

    // --- Extraire toutes les données du token ---
    // Méthode privée utilisée par les deux méthodes ci-dessus
    private Claims extraireClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())         // vérifie la signature
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}