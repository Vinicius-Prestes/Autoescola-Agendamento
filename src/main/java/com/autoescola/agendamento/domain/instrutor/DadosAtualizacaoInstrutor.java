package com.autoescola.agendamento.domain.instrutor;

import com.autoescola.agendamento.domain.endereco.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoInstrutor(
        @NotNull(message = "ID do instrutor é obrigatório")
        Long id,

        String nome,

        String telefone,

        @Valid
        DadosEndereco endereco
) {
}
