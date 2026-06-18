package sc.liste.noel.liste_noel.back.service.impl;

import com.fasterxml.uuid.Generators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sc.liste.noel.liste_noel.back.db.entity.CompteEntity;
import sc.liste.noel.liste_noel.back.db.repo.CompteRepo;
import sc.liste.noel.liste_noel.back.exception.CompteNotFoundException;
import sc.liste.noel.liste_noel.back.exception.MailServiceDesactivedException;
import sc.liste.noel.liste_noel.back.exception.MotDePasseException;
import sc.liste.noel.liste_noel.back.service.CompteServiceInterface;
import sc.liste.noel.liste_noel.back.utils.PasswordUtils;
import sc.liste.noel.liste_noel.back.service.PasswordService;
import sc.liste.noel.liste_noel.back.CompteMapper;
import sc.liste.noel.liste_noel.back.dto.CompteDto;
import sc.liste.noel.liste_noel.back.dto.TokenDto;

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

    @Value("${salt}")
    private String salt;

    @Autowired
    private MailService mailService;

    @Value("${send_email_active}")
    private Boolean mailServiceActived;

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
    public CompteDto connexion(String email, String password) throws CompteNotFoundException {
        CompteEntity compte = compteRepo.findByEmailAndPassword(email, PasswordUtils.generateSecurePassword(password, salt));
        if (compte != null) {
            compte.setNbConnexion(compte.getNbConnexion() + 1);
            compte.setDateDerniereConnexion(LocalDateTime.now());
            compteRepo.save(compte);
            return CompteMapper.EntityToDto(compte);
        } else {
            throw new CompteNotFoundException("Compte non trouvé");
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
    public String creationCompte(String email, String password, boolean cguAccepted, String pseudo) {
        String activationkey = Generators.timeBasedEpochGenerator().generate().toString();
        compteRepo.save(new CompteEntity(email, PasswordUtils.generateSecurePassword(password, salt), cguAccepted, pseudo, activationkey));
        String url = baseUrl + "/compte/activate?userId=" + email + "&key=" + activationkey;
        String body = "Bonjour,\n" +
                "\n" +
                "Merci d'avoir créé un compte sur notre plateforme. Nous sommes ravis de vous accueillir parmi nous !\n" +
                "\n" +
                "Voici les détails de votre compte :\n" +
                "\n" +
                "Nom de compte : " + email + "\n" +
                "Pour activer votre compte, veuillez cliquer sur le lien ci-dessous :\n" +
                url + "\n" +
                "\n" +
                "Si vous n'avez pas créé ce compte, veuillez ignorer cet email.\n" +
                "\n" +
                "Nous vous remercions de votre confiance et restons à votre disposition pour toute question.\n" +
                "\n" +
                "Cordialement,";
        mailService.sendEmail(email, "Confirmation de création de compte", body);
        return email;
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

    @Override
    public boolean updatePassword(String email, String oldPassword, String newPassword, String confirmationNewPassWord) throws CompteNotFoundException, MotDePasseException {
        CompteEntity compteEntity = compteRepo.findByEmailAndPassword(email,
                PasswordUtils.generateSecurePassword(oldPassword, salt));
        if (compteEntity != null) {
            if (newPassword.equals(confirmationNewPassWord)) {
                compteEntity.setPassword(PasswordUtils.generateSecurePassword(newPassword, salt));
                compteEntity.setNbModificationMdp(compteEntity.getNbModificationMdp() + 1);
                compteEntity.setDateDerniereModificationMdp(LocalDateTime.now());
                compteRepo.save(compteEntity);
                return true;
            } else {
                throw new MotDePasseException("Les mots de passes ne sont pas identique");
            }
        } else {
            throw new CompteNotFoundException("Compte introuvable ou le mot de passe n'est pas corecte");
        }
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
    public void genererMotDePasseEtEnvoyer(String email) throws MailServiceDesactivedException {

        if (mailServiceActived) {
            String newMdp = PasswordService.generatePassayPassword();
            boolean isUpdate = forceUpdatePassword(email, newMdp);
            if (isUpdate) {
                String body = "Votre mot de passe a été réinitialisé, voici votre nouveau mot de passe, vous pourrez le modifier une fois connecté : " + newMdp;
                mailService.sendEmail(email, "Mot de passe modifié", body);
            }
        } else {
            throw new MailServiceDesactivedException("L'envois de mail est désactivé");
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
