package dev.java10x.RegisterProductsAPI.Email.Service;

import dev.java10x.RegisterProductsAPI.Email.Interfaces.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String recipient, String content, String body)  {
       try {
           MimeMessage message = mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(message, false);

           helper.setTo(recipient);
           helper.setSubject(content);
           helper.setText(body);

           mailSender.send(message);
       } catch (MessagingException e) {
           e.printStackTrace();
       }
    }

    @Override
    public void sendEmailWithAttachment(String recipient, String content, String body, File attachment) {
       try {
           MimeMessage message = mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(message, true); // TRUE: para aceitar envio de anexo

           helper.setTo(recipient);
           helper.setSubject(content);
           helper.setText(body);
           helper.addAttachment(attachment.getName(), attachment);

           mailSender.send(message);
       } catch (MessagingException e) {
           e.printStackTrace();
       }
    }
}
