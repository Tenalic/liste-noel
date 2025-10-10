package sc.liste.noel.liste_noel.front.service.impl;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {

    private static final Logger LOGGER = LogManager.getLogger(MailService.class);

    @Value("${usernameEmail}")
    private String usernameEmail;

    @Value("${password_email}")
    private String password_email;
    @Value("${host_email}")
    private String host_email;

    private Session session;

    public void sendEmail(String destinataire, String sujet, String contenu) {

        if(session == null) {
            final String username = usernameEmail;
            final String password = password_email;

            // provide host address
            //String host = "smtp.gmail.com";
            String host = host_email;

            // configure SMTP details
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");

            // create the mail Session object
            session = Session.getInstance(props,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
        }



        try {
            // create a MimeMessage object
            Message message = new MimeMessage(session);
            // set From email field
            message.setFrom(new InternetAddress(usernameEmail));
            // set To email field
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(destinataire));
            // set email subject field
            message.setSubject(sujet);
            // set the content of the email message
            message.setText(contenu);

            // send the email message
            Transport.send(message);

            LOGGER.info("Email Message Sent Successfully to " + destinataire + " with the subject : " + sujet);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
