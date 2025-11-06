package dev.java10x.RegisterProductsAPI.Email.Interfaces;

import jakarta.mail.MessagingException;

import java.io.File;

public interface EmailService {

    void sendEmail(String recipient, String content, String body) throws MessagingException;
    void sendEmailWithAttachment(String recipient, String content, String body, File attachment);
}
