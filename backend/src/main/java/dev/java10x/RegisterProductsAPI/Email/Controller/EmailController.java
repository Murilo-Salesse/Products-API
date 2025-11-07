package dev.java10x.RegisterProductsAPI.Email.Controller;

import dev.java10x.RegisterProductsAPI.Email.Interfaces.EmailService;
import dev.java10x.RegisterProductsAPI.Reports.Service.ReportService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;
    private final ReportService reportService;

    public EmailController(EmailService emailService, ReportService reportService) {
        this.emailService = emailService;
        this.reportService = reportService;
    }

    @GetMapping("/send")
    public String sendEmail() throws MessagingException {
        emailService.sendEmail("userteste@gmail.com", "Relatório", "Segue o relatório em anexo");
        return "E-mail enviado com sucesso!";
    }

    @GetMapping("/sendFile")
    public String sendEmailWithReport() throws Exception{
        File reportFile = reportService.generateReportTop5AsFile();

        emailService.sendEmailWithAttachment(
                "userteste@gmail.com",
                "Relatório de Produtos Top 5",
                "Segue em anexo o relatório dos 5 produtos mais caros.",
                reportFile
        );

        return "E-mail com relatório enviado com sucesso!";
    }
}
