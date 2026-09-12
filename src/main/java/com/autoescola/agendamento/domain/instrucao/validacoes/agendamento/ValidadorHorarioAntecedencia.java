package com.autoescola.agendamento.domain.instrucao.validacoes.agendamento;

import com.autoescola.agendamento.domain.ValidacaoException;
import com.autoescola.agendamento.domain.instrucao.DadosAgendamentoInstrucao;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component("ValidadorHorarioAntecedenciaAgendamento")
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoInstrucao {

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        var agora = LocalDateTime.now();
        var diferencaEmMinutos = Duration.between(agora, dados.data()).toMinutes();

        if (diferencaEmMinutos < 30) {
            throw new ValidacaoException("Instrução deve ser agendada com antecedência mínima de 30 minutos!");
        }
    }
}
