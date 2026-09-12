package com.autoescola.agendamento.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
@WithMockUser
class AlunoControllerTest {

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
    @DisplayName("Deveria devolver codigo 400 quando informacoes estao invalidas no cadastro do aluno")
    void cadastrarCenarioInvalido() throws Exception {
        mvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deveria cadastrar aluno com sucesso e devolver codigo 201")
    void cadastrarCenarioValido() throws Exception {
        String json = """
                {
                    "nome": "Beatriz Lima",
                    "email": "beatriz.lima@email.com",
                    "telefone": "11966665555",
                    "cpf": "12345678901",
                    "endereco": {
                        "logradouro": "Av Paulista",
                        "numero": "500",
                        "complemento": "Apto 42",
                        "bairro": "Bela Vista",
                        "cidade": "São Paulo",
                        "uf": "SP",
                        "cep": "01311-000"
                    }
                }
                """;

        mvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Beatriz Lima"))
                .andExpect(jsonPath("$.email").value("beatriz.lima@email.com"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deveria listar alunos paginados e ordenados")
    void listarAlunos() throws Exception {
        String json = """
                {
                    "nome": "Carlos Drummond",
                    "email": "carlos.drummond@email.com",
                    "telefone": "11955554444",
                    "cpf": "98765432100",
                    "endereco": {
                        "logradouro": "Rua Central",
                        "bairro": "Centro",
                        "cidade": "São Paulo",
                        "uf": "SP",
                        "cep": "01000-000"
                    }
                }
                """;

        mvc.perform(post("/alunos").contentType(MediaType.APPLICATION_JSON).content(json));

        mvc.perform(get("/alunos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Deveria atualizar informacoes permitidas do aluno")
    void atualizarAluno() throws Exception {
        String jsonCadastro = """
                {
                    "nome": "Mariana Ramos",
                    "email": "mariana.ramos@email.com",
                    "telefone": "11911113333",
                    "cpf": "33344455566",
                    "endereco": {
                        "logradouro": "Rua Original",
                        "bairro": "Centro",
                        "cidade": "São Paulo",
                        "uf": "SP",
                        "cep": "01000-000"
                    }
                }
                """;

        var response = mvc.perform(post("/alunos").contentType(MediaType.APPLICATION_JSON).content(jsonCadastro))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Number idNum = com.jayway.jsonpath.JsonPath.read(response, "$.id");
        Long id = idNum.longValue();

        String jsonAtualizacao = String.format("""
                {
                    "id": %d,
                    "nome": "Mariana Ramos Silva",
                    "telefone": "11988889999"
                }
                """, id);

        mvc.perform(put("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Mariana Ramos Silva"))
                .andExpect(jsonPath("$.telefone").value("11988889999"))
                .andExpect(jsonPath("$.email").value("mariana.ramos@email.com"))
                .andExpect(jsonPath("$.cpf").value("33344455566"));
    }

    @Test
    @DisplayName("Deveria excluir aluno logicamente")
    void excluirAluno() throws Exception {
        String jsonCadastro = """
                {
                    "nome": "Aluno Para Excluir",
                    "email": "excluiraluno@email.com",
                    "telefone": "11911112222",
                    "cpf": "77788899900",
                    "endereco": {
                        "logradouro": "Rua 1",
                        "bairro": "Centro",
                        "cidade": "São Paulo",
                        "uf": "SP",
                        "cep": "01000-000"
                    }
                }
                """;

        var response = mvc.perform(post("/alunos").contentType(MediaType.APPLICATION_JSON).content(jsonCadastro))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Number idNumDelete = com.jayway.jsonpath.JsonPath.read(response, "$.id");
        Long id = idNumDelete.longValue();

        mvc.perform(delete("/alunos/" + id))
                .andExpect(status().isNoContent());

        mvc.perform(get("/alunos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[?(@.id == " + id + ")]").doesNotExist());
    }
}
