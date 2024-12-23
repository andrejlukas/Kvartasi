package com.mojkvart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private String createBody(String code) {
        return "Poštovani,\n\nVaš šesteroznamenkasti kod za potvrdu registracije u aplikaciju MojKvart je: " + code +
                ".\n\nLijep pozdrav, MojKvart";
    }

    public void sendVerificationMail(String to, String code) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("progiemailsender@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("Potvrdite svoju registraciju");
        simpleMailMessage.setText(createBody(code));

        javaMailSender.send(simpleMailMessage);
    }
}