package com.autoescola.agendamento.domain.instrucao;

import java.time.LocalDateTime;

public record DadosDetalhamentoInstrucao(
        Long id,
        Long idAluno,
        Long idInstrutor,
        LocalDateTime data
) {
    public DadosDetalhamentoInstrucao(Instrucao instrucao) {
        this(
                instrucao.getId(),
                instrucao.getAluno().getId(),
                instrucao.getInstrutor().getId(),
                instrucao.getData()
        );
    }
}
