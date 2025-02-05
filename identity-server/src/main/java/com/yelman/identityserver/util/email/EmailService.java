package com.yelman.identityserver.util.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {
    private final JavaMailSender mailSender;


    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;

    }

    @Override
    public void sendVerificationEmail(String email, String token, String name) {
        try {
            String link = "http://localhost:8087/auth/verify-email?token=" + token;
            String htmlPath="template/send-verification-email.html";
            MimeMessage message = mailSender.createMimeMessage();
            String emailContent = EmailTemplate.getEmailTemplate(htmlPath, name, link);
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("E-posta Doğrulama");
            helper.setText(emailContent, true);

            mailSender.send(message);
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    @Override
    public void sendPasswordResetEmail(String email, String token, String name) {
        try {
            String htmlPath = "template/reset-password-email-template.html";
            String link = "http://localhost:8087/auth/reset-password?token=" + token;
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String emailContent = EmailTemplate.getEmailTemplate(htmlPath, name, link);
            helper.setTo(email);
            helper.setSubject("Reset Password");
            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException me) {
            me.printStackTrace();
        }

    }
}

