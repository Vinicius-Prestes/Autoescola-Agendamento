package com.autoescola.agendamento.domain.aluno;

import com.autoescola.agendamento.domain.endereco.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoAluno(
        @NotNull(message = "ID do aluno é obrigatório")
        Long id,

        String nome,

        String telefone,

        @Valid
        DadosEndereco endereco
) {
}
