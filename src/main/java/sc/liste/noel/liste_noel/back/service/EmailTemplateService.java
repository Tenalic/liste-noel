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

}
