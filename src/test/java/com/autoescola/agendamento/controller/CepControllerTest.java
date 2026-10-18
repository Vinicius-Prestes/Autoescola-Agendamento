package com.autoescola.agendamento.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CepControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Deveria retornar codigo 200 ao consultar CEP valido da FIAP Paulista")
    void consultarCepValido() throws Exception {
        mvc.perform(get("/cep/01311000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uf").value("SP"))
                .andExpect(jsonPath("$.localidade").value("São Paulo"))
                .andExpect(jsonPath("$.logradouro").exists());
    }

    @Test
    @DisplayName("Deveria retornar codigo 400 ao consultar CEP invalido")
    void consultarCepInvalido() throws Exception {
        mvc.perform(get("/cep/123"))
                .andExpect(status().isBadRequest());
    }
}