package com.autoescola.agendamento.domain.instrutor;

import com.autoescola.agendamento.domain.endereco.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroInstrutor(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        String telefone,

        @NotBlank
        String cnh,

        @NotNull
        Especialidade especialidade,

        @NotNull
        @Valid
        DadosEndereco endereco
) {
}
