package com.mojkvart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final String AUTOMATIC_MESSAGE_SUBJECT = "[Do not reply] Potvrdite svoju registraciju";

    @Autowired
    private JavaMailSender javaMailSender;

    private String createBody(String code) {
        return "Poštovani,\n\nVaš šesteroznamenkasti kod za potvrdu registracije u aplikaciju MojKvart je: " + code +
                ".\n\nLijep pozdrav, MojKvart";
    }

    public void sendVerificationMail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setSubject(AUTOMATIC_MESSAGE_SUBJECT);
        message.setText(createBody(code));
        message.setTo(to);

        javaMailSender.send(message);
    }
}