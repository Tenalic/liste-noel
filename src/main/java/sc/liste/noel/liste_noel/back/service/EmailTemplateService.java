package sc.liste.noel.liste_noel.back.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Service
public class EmailTemplateService {

    ClassLoaderTemplateResolver resolver;
    TemplateEngine templateEngine;

    public EmailTemplateService() {
        resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    public String generateBodyActivationEmail(String email, String url) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("url", url);
        return templateEngine.process("activation-email", context);
    }

    public String generateBodySuppressionObjet(String titre,
                                               String description,
                                               String url,
                                               String nomListe,
                                               String urlPartage) {
        Context context = new Context();
        context.setVariable("titre", titre);
        context.setVariable("description", description);
        context.setVariable("url", url);
        context.setVariable("nomListe", nomListe);
        context.setVariable("urlListe", urlPartage);
        return templateEngine.process("suppression-objet", context);
    }

    public String generateBodyModificationObjet(String ancienTitre,
                                                String ancienneDescription,
                                                String ancienneUrl,
                                                String nouveauTitre,
                                                String nouvelleDescription,
                                                String nouvelleUrl,
                                                String priorite,
                                                String nomListe,
                                                String urlListe) {
        Context context = new Context();
        context.setVariable("ancienTitre", ancienTitre);
        context.setVariable("ancienneDescription", ancienneDescription);
        context.setVariable("ancienneUrl", ancienneUrl);

        context.setVariable("nouveauTitre", nouveauTitre);
        context.setVariable("nouvelleDescription", nouvelleDescription);
        context.setVariable("nouvelleUrl", nouvelleUrl);
        context.setVariable("priorite", priorite);

        context.setVariable("nomListe", nomListe);
        context.setVariable("urlListe", urlListe);
        return templateEngine.process("modification-objet", context);
    }

}
