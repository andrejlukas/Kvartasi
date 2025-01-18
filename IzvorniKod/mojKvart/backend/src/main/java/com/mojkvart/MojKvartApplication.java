package com.mojkvart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MojKvartApplication {
    public static void main(final String[] args) {
        Dotenv dotenv = Dotenv.configure()
                      .directory("C://ProgiProjekt//Kvartasi//IzvorniKod//mojKvart//backend//.env")
                      .load();

        // Postavi varijable u System environment (opcionalno)
        System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));

        System.setProperty("DATASOURCE_URL", dotenv.get("DATASOURCE_URL"));
        System.setProperty("DATASOURCE_USERNAME", dotenv.get("DATASOURCE_USERNAME"));
        System.setProperty("DATASOURCE_PASSWORD", dotenv.get("DATASOURCE_PASSWORD"));

        System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
        System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
        System.setProperty("GOOGLE_REDIRECT_URI", dotenv.get("GOOGLE_REDIRECT_URI"));
        
        System.setProperty("VERIFICATION_MAIL_SENDER_EMAIL", dotenv.get("VERIFICATION_MAIL_SENDER_EMAIL"));
        System.setProperty("VERIFICATION_MAIL_SENDER_PASSWORD", dotenv.get("VERIFICATION_MAIL_SENDER_PASSWORD"));
        System.setProperty("FRONTEND_URI", dotenv.get("FRONTEND_URI"));
        System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
        
        SpringApplication.run(MojKvartApplication.class, args);
    }
}
