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
class InstrutorControllerTest {

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
    @DisplayName("Deveria devolver codigo 400 quando informacoes estao invalidas no cadastro")
    void cadastrarCenarioInvalido() throws Exception {
        mvc.perform(post("/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deveria cadastrar instrutor com sucesso e devolver codigo 201")
    void cadastrarCenarioValido() throws Exception {
        String json = """
                {
                    "nome": "Marcos Andrade",
                    "email": "marcos.andrade@email.com",
                    "telefone": "11987654321",
                    "cnh": "99887766554",
                    "especialidade": "CARROS",
                    "endereco": {
                        "logradouro": "Rua das Flores",
                        "numero": "100",
                        "complemento": "Bloco A",
                        "bairro": "Jardim",
                        "cidade": "São Paulo",
                        "uf": "SP",
                        "cep": "01001-000"
                    }
                }
                """;

        mvc.perform(post("/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.nome").value("Marcos Andrade"))
                .andExpect(jsonPath("$.email").value("marcos.andrade@email.com"))
                .andExpect(jsonPath("$.cnh").value("99887766554"))
                .andExpect(jsonPath("$.especialidade").value("CARROS"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deveria listar instrutores paginados e ordenados")
    void listarInstrutores() throws Exception {
        String json = """
                {
                    "nome": "Bernardo Silva",
                    "email": "bernardo@email.com",
                    "telefone": "11911112222",
                    "cnh": "11122233344",
                    "especialidade": "MOTOS",
                    "endereco": {
                        "logradouro": "Rua 1",
                        "bairro": "Centro",
                        "cidade": "São Paulo",
                        "uf": "SP",
                        "cep": "01000-000"
                    }
                }
                """;

        mvc.perform(post("/instrutores").contentType(MediaType.APPLICATION_JSON).content(json));

        mvc.perform(get("/instrutores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Deveria atualizar informacoes permitidas do instrutor")
    void atualizarInstrutor() throws Exception {
        String jsonCadastro = """
                {
                    "nome": "Carlos Roberto",
                    "email": "carlos.roberto@email.com",
                    "telefone": "11911112222",
                    "cnh": "55566677788",
                    "especialidade": "CAMINHOES",
                    "endereco": {
                        "logradouro": "Rua Original",
                        "bairro": "Centro",
                        "cidade": "São Paulo",
                        "uf": "SP",
                        "cep": "01000-000"
                    }
                }
                """;

        var response = mvc.perform(post("/instrutores").contentType(MediaType.APPLICATION_JSON).content(jsonCadastro))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Number idNum = com.jayway.jsonpath.JsonPath.read(response, "$.id");
        Long id = idNum.longValue();

        String jsonAtualizacao = String.format("""
                {
                    "id": %d,
                    "nome": "Carlos Roberto Atualizado",
                    "telefone": "11999998888"
                }
                """, id);

        mvc.perform(put("/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAtualizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Roberto Atualizado"))
                .andExpect(jsonPath("$.telefone").value("11999998888"))
                .andExpect(jsonPath("$.email").value("carlos.roberto@email.com"))
                .andExpect(jsonPath("$.cnh").value("55566677788"));
    }

    @Test
    @DisplayName("Deveria excluir instrutor logicamente")
    void excluirInstrutor() throws Exception {
        String jsonCadastro = """
                {
                    "nome": "Instrutor Para Excluir",
                    "email": "excluir@email.com",
                    "telefone": "11911112222",
                    "cnh": "99988877766",
                    "especialidade": "VANS",
                    "endereco": {
                        "logradouro": "Rua 1",
                        "bairro": "Centro",
                        "cidade": "São Paulo",
                        "uf": "SP",
                        "cep": "01000-000"
                    }
                }
                """;

        var response = mvc.perform(post("/instrutores").contentType(MediaType.APPLICATION_JSON).content(jsonCadastro))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Number idNum = com.jayway.jsonpath.JsonPath.read(response, "$.id");
        Long id = idNum.longValue();

        mvc.perform(delete("/instrutores/" + id))
                .andExpect(status().isNoContent());

        mvc.perform(get("/instrutores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[?(@.id == " + id + ")]").doesNotExist());
    }
}
