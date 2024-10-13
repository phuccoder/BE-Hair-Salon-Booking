package com.example.hairsalon.components.notifications;

import com.example.hairsalon.components.events.MailEvent;
import com.example.hairsalon.repositories.IAccountRepository;
import jakarta.mail.*;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


import java.util.Properties;
@Component
public class MailService {
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";

    @Value("${app.mail.host}")
    private String host;
    @Value("${app.mail.port}")
    private String port;
    @Value("${app.mail.username}")
    private String email;
    @Value("${app.mail.password}")
    private String password;
    private JavaMailSender javaMailSender;
    private IAccountRepository userRepository;
    @Autowired
    ThymeleafService thymeleafService;

    @Async
    @EventListener
    public void sendMail(MailEvent event) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
        Message message = new MimeMessage(session);

        try {
            if(event.getType().equals("verify")){
                message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(event.getUser().getAccountEmail())});

                message.setFrom(new InternetAddress(email));
                message.setSubject("Verify Account");
                message.setContent(thymeleafService.getVerifyContent(event.getUser(),event.getUrl()), CONTENT_TYPE_TEXT_HTML);
                Transport.send(message);
            }else if (event.getType().equals("forgot")){
                message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(event.getUser().getAccountEmail())});

                message.setFrom(new InternetAddress(email));
                message.setSubject("Quên tài khoản");
                message.setContent(thymeleafService.getResetPasswordContent(event.getUser(), event.getUrl()), CONTENT_TYPE_TEXT_HTML);
                Transport.send(message);
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}