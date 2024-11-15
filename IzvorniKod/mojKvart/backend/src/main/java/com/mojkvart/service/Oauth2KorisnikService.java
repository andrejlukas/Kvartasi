package com.mojkvart.service;

import com.mojkvart.domain.Kupac;
import com.mojkvart.repos.AdministratorRepository;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.repos.ModeratorRepository;
import com.mojkvart.repos.TrgovinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class Oauth2KorisnikService extends DefaultOAuth2UserService {

    private final AdministratorRepository administratorRepository;
    private final ModeratorRepository moderatorRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final KupacRepository kupacRepository;

    public void authenticateKorisnik(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String ime = oAuth2User.getAttribute("given_name");
        String prezime = oAuth2User.getAttribute("family_name");

        if (kupacRepository.findByKupacEmail(email).isEmpty() &&
            trgovinaRepository.findByTrgovinaEmail(email).isEmpty() &&
            moderatorRepository.findByModeratorEmail(email).isEmpty() &&
            administratorRepository.findByAdministratorEmail(email).isEmpty()) {
            Kupac kupac = new Kupac();
            kupac.setKupacEmail(email);
            kupac.setKupacIme(ime);
            kupac.setKupacPrezime(prezime);
            kupacRepository.save(kupac);
        }
    }

    public HashMap<String, Object> getClaims(OAuth2User oAuth2User) {
        HashMap<String, Object> claims = new HashMap<>();
        String email = oAuth2User.getAttribute("email");

        if(trgovinaRepository.findByTrgovinaEmail(email).isPresent())
            claims.put("role", "TRGOVINA");
        else if(moderatorRepository.findByModeratorEmail(email).isPresent())
            claims.put("role", "MODERATOR");
        else if(administratorRepository.findByAdministratorEmail(email).isPresent())
            claims.put("role", "AMDINISTRATOR");
        else // ili u bazi u tabli kupac ili novonastali korisnik
            claims.put("role", "KUPAC");

        return claims;
    }
}