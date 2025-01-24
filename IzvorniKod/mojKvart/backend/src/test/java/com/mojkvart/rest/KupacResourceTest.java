package com.mojkvart.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojkvart.model.KupacDTO;
import com.mojkvart.model.LoginDTO;
import com.mojkvart.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

// MockMvc uvezi statički:
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KupacResource.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
    "server.port=8080"
})
public class KupacResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdministratorService administratorService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private ModeratorService moderatorService;

    @MockBean
    private TrgovinaService trgovinaService;

    @MockBean
    private KupacService kupacService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private EmailService mailService;

    // Za pretvaranje objekata u JSON
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void testRegistration() throws Exception {

        KupacDTO noviKupac = new KupacDTO();
        noviKupac.setKupacIme("Marko");
        noviKupac.setKupacPrezime("Markić");
        noviKupac.setKupacEmail("markomarkic@gmail.com");
        noviKupac.setKupacSifra("password123");
        when(administratorService.findByAdministratorEmail("markomarkic@gmail.com"))
                .thenReturn(Optional.empty());
        when(moderatorService.findByModeratorEmail("markomarkic@gmail.com"))
                .thenReturn(Optional.empty());
        when(trgovinaService.findByTrgovinaEmail("markomarkic@gmail.com"))
                .thenReturn(Optional.empty());
        when(kupacService.findByKupacEmail("markomarkic@gmail.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword123");
        mockMvc.perform(post("/api/kupacs/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noviKupac)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Stvoren kupac!"));
    }

   @Test
        void testDuljinalozinke() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("validan.email@example.com");
        loginDTO.setSifra("premala"); // Premala lozinka

        mockMvc.perform(post("/api/kupacs/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginDTO)))
           .andDo(print())
           .andExpect(status().isBadRequest())
           .andExpect(content().string("Lozinka mora biti minimalno duljine 8 znakova!"));
}


    @Test
    void testGetAllKupacs() throws Exception {
        KupacDTO kupac1 = new KupacDTO();
        kupac1.setKupacId(1);
        kupac1.setKupacIme("Pero");
        kupac1.setKupacPrezime("Perić");
        kupac1.setKupacEmail("pero@example.com");
        kupac1.setKupacStatus("N"); 
        kupac1.setKodValidanDo(LocalDateTime.now().plusMinutes(5));

        KupacDTO kupac2 = new KupacDTO();
        kupac2.setKupacId(2);
        kupac2.setKupacIme("Ivo");
        kupac2.setKupacPrezime("Ivić");
        kupac2.setKupacEmail("ivo@example.com");
        kupac2.setKupacStatus("V"); 
        kupac2.setKodValidanDo(LocalDateTime.now().plusMinutes(10));
        List<KupacDTO> kupci = Arrays.asList(kupac1, kupac2);
        Mockito.when(kupacService.findAll()).thenReturn(kupci);
        mockMvc.perform(get("/api/kupacs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].kupacIme").value("Pero"))
                .andExpect(jsonPath("$[0].kupacPrezime").value("Perić"))
                .andExpect(jsonPath("$[0].kupacEmail").value("pero@example.com"))
                .andExpect(jsonPath("$[0].kupacStatus").value("N"))
                .andExpect(jsonPath("$[1].kupacIme").value("Ivo"))
                .andExpect(jsonPath("$[1].kupacPrezime").value("Ivić"))
                .andExpect(jsonPath("$[1].kupacEmail").value("ivo@example.com"))
                .andExpect(jsonPath("$[1].kupacStatus").value("V"))
                .andDo(print());
    }
}
