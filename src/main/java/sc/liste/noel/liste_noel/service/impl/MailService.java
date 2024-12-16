package sc.liste.noel.liste_noel.service.impl;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {

    @Value("${usernameEmail}")
    private String usernameEmail;

    @Value("${password_email_p1}")
    private String password_email_p1;
    @Value("${password_email_p2}")
    private String password_email_p2;
    @Value("${password_email_p3}")
    private String password_email_p3;
    @Value("${password_email_p4}")
    private String password_email_p4;

    private Session session;

    public void sendEmail(String destinataire, String sujet, String contenu) {
        // provide sender's email ID
        String from = usernameEmail;

        if(session == null) {
            // provide account credentials
            final String username = usernameEmail;
            final String password = password_email_p1 + " " + password_email_p2 + " " + password_email_p3 + " " + password_email_p4;

            // provide host address
            String host = "smtp.gmail.com";

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
            message.setFrom(new InternetAddress(from));
            // set To email field
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(destinataire));
            // set email subject field
            message.setSubject(sujet);
            // set the content of the email message
            message.setText(contenu);

            // send the email message
            Transport.send(message);

            System.out.println("Email Message Sent Successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
