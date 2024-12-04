package sc.liste.noel.liste_noel.service;

public interface JwtTokenInterface {


    String generateToken(String username);

    String getUsernameFromToken(String token);

    boolean validateToken(String token);
}

