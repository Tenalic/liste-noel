package sc.liste.noel.liste_noel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sc.liste.noel.liste_noel.Utile.PasswordUtils;
import sc.liste.noel.liste_noel.Utile.mapper.CompteMapper;
import sc.liste.noel.liste_noel.dao.entity.CompteDao;
import sc.liste.noel.liste_noel.dao.repo.CompteRepo;
import sc.liste.noel.liste_noel.dto.CompteDto;
import sc.liste.noel.liste_noel.dto.TokenDto;
import sc.liste.noel.liste_noel.exception.CompteNotFoundException;
import sc.liste.noel.liste_noel.exception.TokenExpiredException;
import sc.liste.noel.liste_noel.service.CompteServiceInterface;
import sc.liste.noel.liste_noel.service.JwtTokenInterface;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CompteServiceImpl implements CompteServiceInterface {

    private static final int DURABILITE_TOKEN = 24;

    @Autowired
    private CompteRepo compteRepo;

    @Autowired
    private JwtTokenInterface jwtToken;

    @Value("${salt}")
    private String salt;

    private final Map<String, TokenDto> tokenValideMap = new HashMap<>();

    @Override
    public boolean compteExiste(String cossy) {
        return Optional.ofNullable(compteRepo.findByEmail(cossy)).isPresent();
    }

    @Override
    public boolean pseudoExiste(String pseudo) {
        return Optional.ofNullable(compteRepo.findByPseudo(pseudo)).isPresent();
    }

    @Override
    public CompteDto connexion(String email, String password) {
        CompteDao compte = compteRepo.findByEmailAndPassword(email, PasswordUtils.generateSecurePassword(password, salt));
        if (compte != null) {
            compte.setNbConnexion(compte.getNbConnexion() + 1);
            compte.setDateDerniereConnexion(LocalDateTime.now());
            compteRepo.save(compte);
            return CompteMapper.DaoToDto(compte);
        } else {
            return null;
        }
    }

    @Override
    public boolean deconexion(String cossy) {
        CompteDao compte = compteRepo.findByEmail(cossy);
        if (compte != null) {
            compte.setNbDeconnexion(compte.getNbDeconnexion() + 1);
            compte.setDateDerniereDeconnexion(LocalDateTime.now());
            compteRepo.save(compte);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean creationCompte(String cossy, String password, boolean cguAccepted, String pseudo) {
        compteRepo.save(new CompteDao(cossy, PasswordUtils.generateSecurePassword(password, salt), cguAccepted, pseudo));
        return true;
    }

    @Override
    public boolean supprimerCompte(String cossy) {
        if (compteRepo.findByEmail(cossy) != null) {
            compteRepo.deleteById(cossy);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean updatePassword(String email, String oldPassword, String newPassword) {
        CompteDao compteDao = compteRepo.findByEmailAndPassword(email,
                PasswordUtils.generateSecurePassword(oldPassword, salt));
        if (compteDao != null) {
            compteDao.setPassword(PasswordUtils.generateSecurePassword(newPassword, salt));
            compteDao.setNbModificationMdp(compteDao.getNbModificationMdp() + 1);
            compteDao.setDateDerniereModificationMdp(LocalDateTime.now());
            compteRepo.save(compteDao);
            return true;
        }
        return false;
    }

    @Override
    public TokenDto getTokenDtoByEmail(String cossy) {

        TokenDto tokenDto = tokenValideMap.get(cossy);

        if (tokenDto != null && jwtToken.validateToken(tokenDto.getToken())) {
            return tokenDto;
        } else {
            String newToken = jwtToken.generateToken(cossy);
            tokenDto = new TokenDto();
            tokenDto.setCossy(cossy);
            tokenDto.setTokenExpireDate(LocalDateTime.now().plusHours(DURABILITE_TOKEN));
            tokenDto.setToken(newToken);
            tokenValideMap.put(cossy, tokenDto);
            return tokenDto;
        }
    }

    @Override
    public TokenDto getTokenDtoByToken(String token) throws CompteNotFoundException, TokenExpiredException {

        String cossy = jwtToken.getUsernameFromToken(token);

        TokenDto tokenDto = tokenValideMap.get(cossy);

        if (tokenDto != null && jwtToken.validateToken(tokenDto.getToken())) {
            return tokenDto;
        } else {
            if (tokenDto == null) {
                throw new CompteNotFoundException("Aucun compte ne correspond a ce token");
            }
            throw new TokenExpiredException("Le token n'est plus valide");
        }
    }
}
