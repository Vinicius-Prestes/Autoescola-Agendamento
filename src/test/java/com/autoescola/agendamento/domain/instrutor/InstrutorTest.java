package com.autoescola.agendamento.domain.instrutor;

import com.autoescola.agendamento.domain.endereco.DadosEndereco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstrutorTest {

    @Test
    @DisplayName("Deveria instanciar instrutor ativo com dados de cadastro")
    void instanciarInstrutorComSucesso() {
        var dadosEndereco = new DadosEndereco("Rua A", "123", "Apto 1", "Bairro B", "Cidade C", "SP", "01234-567");
        var dadosCadastro = new DadosCadastroInstrutor("Carlos Silva", "carlos@email.com", "11988887777", "12345678900", Especialidade.CARROS, dadosEndereco);

        var instrutor = new Instrutor(dadosCadastro);

        assertTrue(instrutor.getAtivo());
        assertEquals("Carlos Silva", instrutor.getNome());
        assertEquals("carlos@email.com", instrutor.getEmail());
        assertEquals("11988887777", instrutor.getTelefone());
        assertEquals("12345678900", instrutor.getCnh());
        assertEquals(Especialidade.CARROS, instrutor.getEspecialidade());
        assertEquals("Rua A", instrutor.getEndereco().getLogradouro());
    }

    @Test
    @DisplayName("Deveria atualizar apenas nome, telefone e endereço, mantendo email, cnh e especialidade")
    void atualizarInformacoesRestritas() {
        var dadosEndereco = new DadosEndereco("Rua A", "123", null, "Bairro B", "Cidade C", "SP", "01234-567");
        var instrutor = new Instrutor(new DadosCadastroInstrutor("Carlos", "carlos@email.com", "11988887777", "12345678900", Especialidade.CARROS, dadosEndereco));

        var novoEndereco = new DadosEndereco("Rua Nova", "456", "Sala 2", "Bairro Novo", "Cidade Nova", "RJ", "98765-432");
        var dadosAtualizacao = new DadosAtualizacaoInstrutor(1L, "Carlos Santos", "11999990000", novoEndereco);

        instrutor.atualizarInformacoes(dadosAtualizacao);

        assertEquals("Carlos Santos", instrutor.getNome());
        assertEquals("11999990000", instrutor.getTelefone());
        assertEquals("Rua Nova", instrutor.getEndereco().getLogradouro());
        assertEquals("carlos@email.com", instrutor.getEmail()); // Não alterado
        assertEquals("12345678900", instrutor.getCnh()); // Não alterado
        assertEquals(Especialidade.CARROS, instrutor.getEspecialidade()); // Não alterado
    }

    @Test
    @DisplayName("Deveria realizar exclusão lógica tornando ativo = false")
    void exclusaoLogica() {
        var dadosEndereco = new DadosEndereco("Rua A", "123", null, "Bairro B", "Cidade C", "SP", "01234-567");
        var instrutor = new Instrutor(new DadosCadastroInstrutor("Carlos", "carlos@email.com", "11988887777", "12345678900", Especialidade.MOTOS, dadosEndereco));

        assertTrue(instrutor.getAtivo());
        instrutor.excluir();
        assertFalse(instrutor.getAtivo());
    }
}
