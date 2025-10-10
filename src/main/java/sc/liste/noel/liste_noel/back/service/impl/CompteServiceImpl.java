package sc.liste.noel.liste_noel.back.service.impl;

import com.fasterxml.uuid.Generators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sc.liste.noel.liste_noel.front.Utile.PasswordUtils;
import sc.liste.noel.liste_noel.front.Utile.Utils;
import sc.liste.noel.liste_noel.front.Utile.mapper.CompteMapper;
import sc.liste.noel.liste_noel.back.db.entity.CompteEntity;
import sc.liste.noel.liste_noel.back.db.repo.CompteRepo;
import sc.liste.noel.liste_noel.front.dto.CompteDto;
import sc.liste.noel.liste_noel.front.dto.TokenDto;
import sc.liste.noel.liste_noel.front.exception.CompteNotFoundException;
import sc.liste.noel.liste_noel.front.exception.TokenExpiredException;
import sc.liste.noel.liste_noel.back.service.CompteServiceInterface;
import sc.liste.noel.liste_noel.front.service.JwtTokenInterface;
import sc.liste.noel.liste_noel.front.service.impl.MailService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CompteServiceImpl implements CompteServiceInterface {

    @Value("${base_url}")
    private String baseUrl;

    private static final int DURABILITE_TOKEN = 24;

    @Autowired
    private CompteRepo compteRepo;

    @Autowired
    private JwtTokenInterface jwtToken;

    @Value("${salt}")
    private String salt;

    @Autowired
    private MailService mailService;

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
        CompteEntity compte = compteRepo.findByEmailAndPassword(email, PasswordUtils.generateSecurePassword(password, salt));
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
        CompteEntity compte = compteRepo.findByEmail(cossy);
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
    public boolean creationCompte(String email, String password, boolean cguAccepted, String pseudo) {
        String activationkey = Generators.timeBasedEpochGenerator().generate().toString();
        compteRepo.save(new CompteEntity(email, PasswordUtils.generateSecurePassword(password, salt), cguAccepted, pseudo, activationkey));
        String url = baseUrl + "/compte/activate?userId=" + email + "&key=" + activationkey;
        String body = "Bonjour,\n" +
                "\n" +
                "Merci d'avoir créé un compte sur notre plateforme. Nous sommes ravis de vous accueillir parmi nous !\n" +
                "\n" +
                "Voici les détails de votre compte :\n" +
                "\n" +
                "Nom de compte : " + email +"\n" +
                "Pour activer votre compte, veuillez cliquer sur le lien ci-dessous :\n" +
                url + "\n" +
                "\n" +
                "Si vous n'avez pas créé ce compte, veuillez ignorer cet email.\n" +
                "\n" +
                "Nous vous remercions de votre confiance et restons à votre disposition pour toute question.\n" +
                "\n" +
                "Cordialement,";
        mailService.sendEmail(email, "Confirmation de création de compte", body);
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
        CompteEntity compteEntity = compteRepo.findByEmailAndPassword(email,
                PasswordUtils.generateSecurePassword(oldPassword, salt));
        if (compteEntity != null) {
            compteEntity.setPassword(PasswordUtils.generateSecurePassword(newPassword, salt));
            compteEntity.setNbModificationMdp(compteEntity.getNbModificationMdp() + 1);
            compteEntity.setDateDerniereModificationMdp(LocalDateTime.now());
            compteRepo.save(compteEntity);
            return true;
        }
        return false;
    }

    private boolean forceUpdatePassword(String email, String newPassword) {
        CompteEntity compteEntity = compteRepo.findByEmail(email);
        if (compteEntity != null) {
            compteEntity.setPassword(PasswordUtils.generateSecurePassword(newPassword, salt));
            compteEntity.setNbModificationMdp(compteEntity.getNbModificationMdp() + 1);
            compteEntity.setDateDerniereModificationMdp(LocalDateTime.now());
            compteRepo.save(compteEntity);
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

    @Override
    public void genererMotDePasseEtEnvoyer(String email) {
        String newMdp = Utils.generatePassayPassword();
        boolean isUpdate = forceUpdatePassword(email, newMdp);
        if (isUpdate) {
            String body = "Votre mot de passe a été réinitialisé, voici votre nouveau mot de passe, vous pourrez le modifier une fois connecté : " + newMdp;
            mailService.sendEmail(email, "Mot de passe modifié", body);
        }
    }

    @Override
    public boolean activateUser(String email, String activationKey) {
        CompteEntity compte = compteRepo.findByEmail(email);
        if (compte != null) {
            if (compte.getActivationKey().equals(activationKey) && !compte.getEmailVerified()) {
                compte.setEmailVerified(true);
                compte.setActivationKey(null); // Optionnel : pour éviter une réutilisation
                compteRepo.save(compte);
                return true;
            }
        }
        return false;
    }
}
