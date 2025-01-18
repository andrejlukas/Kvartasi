// KupacProizvodResourceTest.java
package com.mojkvart.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.mojkvart.service.JwtService;
import com.mojkvart.service.KupacProizvodService;
import com.mojkvart.service.ProizvodService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(KupacProizvodResource.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
    "server.port=8080"
})
public class KupacProizvodResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KupacProizvodService kupacProizvodService;


    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtService jwtService;

    @Test
    void testNeimplementiranaFunkcijaKupiProizvode() throws Exception {
        mockMvc.perform(post("/api/kupacProizvods/kosarica/1/kupi")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotImplemented()) 
                .andExpect(content().string("Funkcionalnost kupovine proizvoda iz kosarice trenutno nije implementirana."));
    }
}

