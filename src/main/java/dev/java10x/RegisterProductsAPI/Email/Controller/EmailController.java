package dev.java10x.RegisterProductsAPI.Email.Controller;

import dev.java10x.RegisterProductsAPI.Email.Interfaces.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send")
    public String sendEmail() throws MessagingException {
        emailService.sendEmail("murilosalesse15@gmail.com", "Relatório", "Segue o relatório em anexo");
        return "E-mail enviado com sucesso!";
    }

}
