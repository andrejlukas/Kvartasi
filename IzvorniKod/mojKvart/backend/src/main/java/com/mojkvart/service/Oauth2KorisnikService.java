package com.mojkvart.service;

import com.mojkvart.model.KupacDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class Oauth2KorisnikService extends DefaultOAuth2UserService {

    private final AdministratorService administratorService;
    private final ModeratorService moderatorService;
    private final TrgovinaService trgovinaService;
    private final KupacService kupacService;

    @Getter
    private String status;

    private KupacDTO initializeKupac(String ime, String prezime, String email) {
        KupacDTO kupac = new KupacDTO();
        kupac.setKupacEmail(email);
        kupac.setKupacIme(ime);
        kupac.setKupacPrezime(prezime);
        kupac.setKupacAdresa("45.815,15.9819"); // default Zagreb
        kupac.setKupacStatus("V");
        return kupac;
    }

    public void authenticateKorisnik(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String ime = oAuth2User.getAttribute("given_name");
        String prezime = oAuth2User.getAttribute("family_name");

        if(kupacService.findByKupacEmail(email).isEmpty() &&
            trgovinaService.findByTrgovinaEmail(email).isEmpty() &&
            moderatorService.findByModeratorEmail(email).isEmpty() &&
            administratorService.findByAdministratorEmail(email).isEmpty()) {
            kupacService.create(initializeKupac(ime, prezime, email));
        }

        if(kupacService.findByKupacEmail(email).isPresent() && kupacService.findByKupacEmail(email).get().getKupacStatus().equals("N"))
            kupacService.update(kupacService.findByKupacEmail(email).get().getKupacId(), initializeKupac(ime, prezime, email));


        if(kupacService.findByKupacEmail(email).isPresent())
            status = kupacService.findByKupacEmail(email).get().getKupacStatus();

        // fali status trgovine i moderatora
        /*
        else if(trgovinaService.findByTrgovinaEmail(email).isPresent()) {
            status = trgovinaService.findByTrgovinaEmail(email).get().getTrgovinaStatus();
        } else if(moderatorService.findByModeratorEmail(email).isPresent()) {
            status = moderatorService.findByModeratorEmail(email).get().getModeratorStatus();
        } else
            status = administratorService.findByAdministratorEmail(email).get().getAdministratorStatus();
        */
    }

    public HashMap<String, Object> getClaims(OAuth2User oAuth2User) {
        HashMap<String, Object> claims = new HashMap<>();
        String email = oAuth2User.getAttribute("email");

        if(trgovinaService.findByTrgovinaEmail(email).isPresent())
            claims.put("role", "TRGOVINA");
        else if(moderatorService.findByModeratorEmail(email).isPresent())
            claims.put("role", "MODERATOR");
        else if(administratorService.findByAdministratorEmail(email).isPresent())
            claims.put("role", "AMDIN");
        else
            claims.put("role", "KUPAC");

        return claims;
    }
}