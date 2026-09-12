package com.autoescola.agendamento.controller;

import com.autoescola.agendamento.domain.aluno.Aluno;
import com.autoescola.agendamento.domain.aluno.AlunoRepository;
import com.autoescola.agendamento.domain.aluno.DadosCadastroAluno;
import com.autoescola.agendamento.domain.endereco.DadosEndereco;
import com.autoescola.agendamento.domain.instrutor.DadosCadastroInstrutor;
import com.autoescola.agendamento.domain.instrutor.Especialidade;
import com.autoescola.agendamento.domain.instrutor.Instrutor;
import com.autoescola.agendamento.domain.instrutor.InstrutorRepository;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@WithMockUser
class InstrucaoControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Deveria agendar instrucao com sucesso")
    void agendarInstrucaoComSucesso() throws Exception {
        var endereco = new DadosEndereco("Rua A", "10", null, "Bairro", "SP", "SP", "01000-000");
        var aluno = alunoRepository.save(new Aluno(new DadosCadastroAluno("Aluno Teste", "aluno@teste.com", "11999998888", "11122233344", endereco)));
        var instrutor = instrutorRepository.save(new Instrutor(new DadosCadastroInstrutor("Instrutor Teste", "instrutor@teste.com", "11988887777", "99887766554", Especialidade.CARROS, endereco)));

        // Próxima segunda-feira às 10:00
        var proximaSegunda = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);

        String json = String.format("""
                {
                    "idAluno": %d,
                    "idInstrutor": %d,
                    "data": "%s"
                }
                """, aluno.getId(), instrutor.getId(), proximaSegunda);

        mvc.perform(post("/instrucoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAluno").value(aluno.getId()))
                .andExpect(jsonPath("$.idInstrutor").value(instrutor.getId()));
    }
}
