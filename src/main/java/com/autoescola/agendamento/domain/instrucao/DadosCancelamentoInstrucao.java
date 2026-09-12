package com.autoescola.agendamento.domain.instrucao;

import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoInstrucao(
        @NotNull
        Long idInstrucao,

        @NotNull
        MotivoCancelamento motivo
) {
}
