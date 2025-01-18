package com.mojkvart.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojkvart.model.PonudaDTO;
import com.mojkvart.service.JwtService;
import com.mojkvart.service.PonudaService;
import com.mojkvart.service.ProizvodService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PonudaResource.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
    "server.port=8080"
})
public class PonudaResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PonudaService ponudaService;

     @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testDodavanjePonudeSEkstremnimRokomTrajanja() throws Exception {
        PonudaDTO ponudaDTO = new PonudaDTO();
        ponudaDTO.setPonudaNaziv("Ekstremno dug rok trajanja");
        ponudaDTO.setPonudaOpis("Ponuda s rokom trajanja od 100 godina.");
        ponudaDTO.setPonudaPopust(1); // ID popusta
        ponudaDTO.setPonudaRok(LocalDateTime.now().plusYears(100).toString()); // Ekstremni rok trajanja

        Mockito.when(ponudaService.create(Mockito.any(PonudaDTO.class))).thenReturn(1);

        mockMvc.perform(post("/api/ponudas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ponudaDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }
}

