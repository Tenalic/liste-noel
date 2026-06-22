package sc.liste.noel.liste_noel.back.service.impl;

import com.fasterxml.uuid.Generators;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sc.liste.noel.liste_noel.back.db.entity.CompteEntity;
import sc.liste.noel.liste_noel.back.db.repo.CompteRepo;
import sc.liste.noel.liste_noel.back.exception.CompteNotFoundException;
import sc.liste.noel.liste_noel.back.exception.MailServiceDesactivedException;
import sc.liste.noel.liste_noel.back.exception.MotDePasseException;
import sc.liste.noel.liste_noel.back.service.CompteServiceInterface;
import sc.liste.noel.liste_noel.back.service.EmailTemplateService;
import sc.liste.noel.liste_noel.back.utils.PasswordUtils;
import sc.liste.noel.liste_noel.back.service.PasswordService;
import sc.liste.noel.liste_noel.back.mapper.CompteMapper;
import sc.liste.noel.liste_noel.back.dto.CompteDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CompteServiceImpl implements CompteServiceInterface {

    @Value("${base_url}")
    private String baseUrl;

    private final CompteRepo compteRepo;

    @Value("${salt}")
    private String salt;

    private final MailService mailService;

    @Value("${send_email_active}")
    private Boolean mailServiceActived;

    private final EmailTemplateService emailTemplateService;

    public CompteServiceImpl(CompteRepo compteRepo, MailService mailService, EmailTemplateService emailTemplateService) {
        this.compteRepo = compteRepo;
        this.mailService = mailService;
        this.emailTemplateService = emailTemplateService;
    }

    @Override
    public boolean compteExiste(String email) {
        return Optional.ofNullable(compteRepo.findByEmail(email)).isPresent();
    }

    @Override
    public String getPseudo(String email) throws CompteNotFoundException {
        CompteEntity compte = compteRepo.findByEmail(email);
        if(compte == null) {
            throw new CompteNotFoundException("Compte introuvable");
        }
        return compte.getPseudo();
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
            return CompteMapper.entityToDto(compte);
        } else {
            throw new CompteNotFoundException("Compte non trouvé");
        }
    }

    @Override
    public String creationCompte(String email, String password, boolean cguAccepted, String pseudo) {
        String activationkey = Generators.timeBasedEpochGenerator().generate().toString();
        compteRepo.save(new CompteEntity(email, PasswordUtils.generateSecurePassword(password, salt), cguAccepted, pseudo, activationkey));
        String url = baseUrl + "/compte/activate?userId=" + email + "&key=" + activationkey;

        String body = emailTemplateService.generateBodyActivationEmail(email, url);
        mailService.sendEmail(email, "Confirmation de création de compte", body);
        return email;
    }

    @Override
    public boolean supprimerCompte(String email) {
        if (compteRepo.findByEmail(email) != null) {
            compteRepo.deleteById(email);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void updatePassword(String email, String oldPassword, String newPassword, String confirmationNewPassWord) throws CompteNotFoundException, MotDePasseException {
        CompteEntity compteEntity = compteRepo.findByEmailAndPassword(email,
                PasswordUtils.generateSecurePassword(oldPassword, salt));
        if (compteEntity != null) {
            if (newPassword.equals(confirmationNewPassWord)) {
                compteEntity.setPassword(PasswordUtils.generateSecurePassword(newPassword, salt));
                compteEntity.setNbModificationMdp(compteEntity.getNbModificationMdp() + 1);
                compteEntity.setDateDerniereModificationMdp(LocalDateTime.now());
                compteRepo.save(compteEntity);
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
