package com.mojkvart.rest;

import com.mojkvart.service.JwtService;
import com.mojkvart.service.ProizvodService;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedException;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ProizvodResource.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
    "server.port=8080"
})
public class ProizvodResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private ProizvodService proizvodService;

    @MockBean
    private JwtService jwtService;

    @Test
    void testNepostojeciProizvod() throws Exception {

        Mockito.when(proizvodService.get(100))
               .thenThrow(new NotFoundException("Proizvod s ID-om 100 ne postoji."));
    
        mockMvc.perform(get("/api/proizvods/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Proizvod s ID-om 100 ne postoji."));
    }
}
