package com.autoescola.agendamento.domain.instrucao;

import com.autoescola.agendamento.domain.aluno.Aluno;
import com.autoescola.agendamento.domain.aluno.DadosCadastroAluno;
import com.autoescola.agendamento.domain.endereco.DadosEndereco;
import com.autoescola.agendamento.domain.instrutor.DadosCadastroInstrutor;
import com.autoescola.agendamento.domain.instrutor.Especialidade;
import com.autoescola.agendamento.domain.instrutor.Instrutor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InstrucaoTest {

    @Test
    @DisplayName("Deveria instanciar instrucao com aluno, instrutor e data informados")
    void instanciarInstrucaoComSucesso() {
        var dadosEndereco = new DadosEndereco("Rua das Flores", "100", null, "Centro", "São Paulo", "SP", "01001-000");
        var aluno = new Aluno(new DadosCadastroAluno("Carlos Silva", "carlos@email.com", "11988887777", "12345678901", dadosEndereco));
        var instrutor = new Instrutor(new DadosCadastroInstrutor("Mariana Lima", "mariana@email.com", "11977778888", "98765432100", Especialidade.CARROS, dadosEndereco));
        var data = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0);

        var instrucao = new Instrucao(aluno, instrutor, data);

        assertNotNull(instrucao.getAluno());
        assertEquals("Carlos Silva", instrucao.getAluno().getNome());
        assertNotNull(instrucao.getInstrutor());
        assertEquals("Mariana Lima", instrucao.getInstrutor().getNome());
        assertEquals(data, instrucao.getData());
        assertNull(instrucao.getMotivoCancelamento());
    }

    @Test
    @DisplayName("Deveria registrar cancelamento de instrucao com motivo preenchido")
    void cancelarInstrucao() {
        var dadosEndereco = new DadosEndereco("Rua das Flores", "100", null, "Centro", "São Paulo", "SP", "01001-000");
        var aluno = new Aluno(new DadosCadastroAluno("Carlos Silva", "carlos@email.com", "11988887777", "12345678901", dadosEndereco));
        var instrutor = new Instrutor(new DadosCadastroInstrutor("Mariana Lima", "mariana@email.com", "11977778888", "98765432100", Especialidade.CARROS, dadosEndereco));
        var data = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0);

        var instrucao = new Instrucao(aluno, instrutor, data);
        assertNull(instrucao.getMotivoCancelamento());

        instrucao.cancelar(MotivoCancelamento.ALUNO_DESISTIU);

        assertEquals(MotivoCancelamento.ALUNO_DESISTIU, instrucao.getMotivoCancelamento());
    }
}