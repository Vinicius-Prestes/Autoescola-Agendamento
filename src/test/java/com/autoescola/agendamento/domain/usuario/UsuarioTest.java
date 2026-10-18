package com.autoescola.agendamento.domain.usuario;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    @DisplayName("Deveria instanciar usuario ativo com perfil comum por padrao")
    void instanciarUsuarioPerfilPadrao() {
        var dados = new DadosCadastroUsuario("usuario@autoescola.com", "senha123", null);
        var usuario = new Usuario(dados, "");

        assertTrue(usuario.getAtivo());
        assertTrue(usuario.isEnabled());
        assertEquals("usuario@autoescola.com", usuario.getLogin());
        assertEquals("usuario@autoescola.com", usuario.getUsername());
        assertEquals("", usuario.getPassword());
        assertEquals(Perfil.COMUM, usuario.getPerfil());
        assertTrue(usuario.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertFalse(usuario.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Deveria instanciar usuario com perfil ADMIN e authorities correspondentes")
    void instanciarUsuarioPerfilAdmin() {
        var dados = new DadosCadastroUsuario("admin@autoescola.com", "senha123", Perfil.ADMIN);
        var usuario = new Usuario(dados, "");

        assertEquals(Perfil.ADMIN, usuario.getPerfil());
        assertTrue(usuario.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(usuario.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    @DisplayName("Deveria atualizar o perfil do usuario")
    void atualizarPerfil() {
        var dados = new DadosCadastroUsuario("usuario@autoescola.com", "senha123", Perfil.COMUM);
        var usuario = new Usuario(dados, "");

        var dadosAtualizacao = new DadosAtualizacaoPerfilUsuario(1L, Perfil.ADMIN);
        usuario.atualizarPerfil(dadosAtualizacao);

        assertEquals(Perfil.ADMIN, usuario.getPerfil());
    }

    @Test
    @DisplayName("Deveria alterar a senha do usuario")
    void alterarSenha() {
        var dados = new DadosCadastroUsuario("usuario@autoescola.com", "senha123", Perfil.COMUM);
        var usuario = new Usuario(dados, "");

        usuario.alterarSenha("");

        assertEquals("", usuario.getPassword());
    }

    @Test
    @DisplayName("Deveria realizar exclusao logica tornando ativo = false")
    void exclusaoLogica() {
        var dados = new DadosCadastroUsuario("usuario@autoescola.com", "senha123", Perfil.COMUM);
        var usuario = new Usuario(dados, "");

        assertTrue(usuario.getAtivo());
        assertTrue(usuario.isEnabled());

        usuario.excluir();

        assertFalse(usuario.getAtivo());
        assertFalse(usuario.isEnabled());
    }
}