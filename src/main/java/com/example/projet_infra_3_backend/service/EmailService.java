package com.example.projet_infra_3_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendEmail(String username,String password, String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Wedato - Nouveau mot de passe");
        message.setText("Bonjour " + username + "!\n\nVotre nouveau mot de passe est : " + password + "\n\nJonathan from Support Team");
        mailSender.send(message);
        System.out.println("Message envoyé avec succes");
    }
}
