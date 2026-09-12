package com.autoescola.agendamento.domain.usuario;

public record DadosListagemUsuario(
        Long id,
        String login,
        Perfil perfil
) {
    public DadosListagemUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin(), usuario.getPerfil());
    }
}
