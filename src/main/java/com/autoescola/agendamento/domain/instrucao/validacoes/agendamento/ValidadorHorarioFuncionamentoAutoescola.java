package com.autoescola.agendamento.domain.instrucao.validacoes.agendamento;

import com.autoescola.agendamento.domain.ValidacaoException;
import com.autoescola.agendamento.domain.instrucao.DadosAgendamentoInstrucao;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamentoAutoescola implements ValidadorAgendamentoInstrucao {

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        var data = dados.data();
        var domingo = data.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesDaAbertura = data.getHour() < 6;
        var depoisDoEncerramento = data.getHour() > 20; // Aula de 1h deve terminar até às 21:00

        if (domingo || antesDaAbertura || depoisDoEncerramento) {
            throw new ValidacaoException("Instrução fora do horário de funcionamento da autoescola (Segunda a Sábado das 06:00 às 21:00)!");
        }
    }
}
