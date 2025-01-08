package com.mojkvart.service;

import jakarta.mail.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private String createBody(String code) {
        return "Poštovani,\n\nVaš šesteroznamenkasti kod za potvrdu registracije u aplikaciju MojKvart je: " + code +
                ".\n\nLijep pozdrav, MojKvart";
    }

    @Async
    public void sendVerificationMail(String to, String code) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

        mimeMessageHelper.setFrom(fromEmail);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject("Potvrdite svoju registraciju");
        mimeMessageHelper.setText(createBody(code), false);

        javaMailSender.send(message);
    }
}