package com.autoescola.agendamento.domain.instrucao.validacoes;

import com.autoescola.agendamento.domain.ValidacaoException;
import com.autoescola.agendamento.domain.instrucao.DadosAgendamentoInstrucao;
import com.autoescola.agendamento.domain.instrucao.validacoes.agendamento.ValidadorHorarioAntecedencia;
import com.autoescola.agendamento.domain.instrucao.validacoes.agendamento.ValidadorHorarioFuncionamentoAutoescola;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorAgendamentoTest {

    private ValidadorHorarioFuncionamentoAutoescola validadorFuncionamento = new ValidadorHorarioFuncionamentoAutoescola();
    private ValidadorHorarioAntecedencia validadorAntecedencia = new ValidadorHorarioAntecedencia();

    @Test
    @DisplayName("Deveria bloquear agendamento no domingo")
    void agendamentoNoDomingo() {
        var proximoDomingo = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).atTime(10, 0);
        var dados = new DadosAgendamentoInstrucao(1L, 1L, proximoDomingo, null);

        assertThrows(ValidacaoException.class, () -> validadorFuncionamento.validar(dados));
    }

    @Test
    @DisplayName("Deveria bloquear agendamento antes das 06:00")
    void agendamentoAntesDas6() {
        var proximaSegunda = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(5, 0);
        var dados = new DadosAgendamentoInstrucao(1L, 1L, proximaSegunda, null);

        assertThrows(ValidacaoException.class, () -> validadorFuncionamento.validar(dados));
    }

    @Test
    @DisplayName("Deveria bloquear agendamento apos as 20:00")
    void agendamentoAposAs20() {
        var proximaSegunda = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(21, 0);
        var dados = new DadosAgendamentoInstrucao(1L, 1L, proximaSegunda, null);

        assertThrows(ValidacaoException.class, () -> validadorFuncionamento.validar(dados));
    }

    @Test
    @DisplayName("Deveria permitir agendamento em horario comercial valido")
    void agendamentoHorarioValido() {
        var proximaSegunda = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(14, 0);
        var dados = new DadosAgendamentoInstrucao(1L, 1L, proximaSegunda, null);

        assertDoesNotThrow(() -> validadorFuncionamento.validar(dados));
    }

    @Test
    @DisplayName("Deveria bloquear agendamento com menos de 30 minutos de antecedencia")
    void agendamentoComPoucaAntecedencia() {
        var dataComPoucaAntecedencia = LocalDateTime.now().plusMinutes(10);
        var dados = new DadosAgendamentoInstrucao(1L, 1L, dataComPoucaAntecedencia, null);

        assertThrows(ValidacaoException.class, () -> validadorAntecedencia.validar(dados));
    }
}
