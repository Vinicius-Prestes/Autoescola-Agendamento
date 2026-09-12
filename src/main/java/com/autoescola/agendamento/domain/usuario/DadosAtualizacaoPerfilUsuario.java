package com.autoescola.agendamento.domain.usuario;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoPerfilUsuario(
        @NotNull
        Long id,

        @NotNull
        Perfil perfil
) {
}
