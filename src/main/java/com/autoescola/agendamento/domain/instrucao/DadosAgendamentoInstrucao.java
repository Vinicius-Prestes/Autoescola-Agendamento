package com.autoescola.agendamento.domain.instrucao;

import com.autoescola.agendamento.domain.instrutor.Especialidade;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAgendamentoInstrucao(
        @NotNull
        Long idAluno,

        Long idInstrutor,

        @NotNull
        @Future
        LocalDateTime data,

        Especialidade especialidade
) {
}
