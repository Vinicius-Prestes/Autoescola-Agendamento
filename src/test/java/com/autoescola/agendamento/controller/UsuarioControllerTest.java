package com.autoescola.agendamento.controller;

import com.autoescola.agendamento.domain.usuario.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
class UsuarioControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Deveria permitir que ADMIN cadastre novo usuario com senha criptografada")
    @WithMockUser(roles = "ADMIN")
    void cadastrarUsuarioComSucesso() throws Exception {
        String json = """
                {
                    "login": "novo.usuario@autoescola.com",
                    "senha": "senhaSegura123",
                    "perfil": "COMUM"
                }
                """;

        mvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.login").value("novo.usuario@autoescola.com"))
                .andExpect(jsonPath("$.perfil").value("COMUM"));

        var usuarioSalvo = usuarioRepository.findUsuarioByLogin("novo.usuario@autoescola.com").orElseThrow();
        assert passwordEncoder.matches("senhaSegura123", usuarioSalvo.getPassword());
    }

    @Test
    @DisplayName("Deveria bloquear usuario COMUM de cadastrar novos usuarios retornando 403 Forbidden")
    @WithMockUser(roles = "USER")
    void cadastrarUsuarioSemPermissao() throws Exception {
        String json = """
                {
                    "login": "sem.permissao@autoescola.com",
                    "senha": "123",
                    "perfil": "COMUM"
                }
                """;

        mvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deveria efetuar login com sucesso e retornar token JWT")
    void efetuarLogin() throws Exception {
        var hash = passwordEncoder.encode("123456");
        usuarioRepository.save(new Usuario(new DadosCadastroUsuario("login.teste@autoescola.com", "123456", Perfil.ADMIN), hash));

        String json = """
                {
                    "login": "login.teste@autoescola.com",
                    "senha": "123456"
                }
                """;

        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}
