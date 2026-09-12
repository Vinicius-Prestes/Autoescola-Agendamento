package com.autoescola.agendamento.domain.aluno;

import com.autoescola.agendamento.domain.endereco.DadosEndereco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    @DisplayName("Deveria instanciar aluno ativo com dados de cadastro")
    void instanciarAlunoComSucesso() {
        var dadosEndereco = new DadosEndereco("Av Paulista", "1000", "Conj 10", "Bela Vista", "São Paulo", "SP", "01310-100");
        var dadosCadastro = new DadosCadastroAluno("Ana Oliveira", "ana@email.com", "11977776666", "12345678909", dadosEndereco);

        var aluno = new Aluno(dadosCadastro);

        assertTrue(aluno.getAtivo());
        assertEquals("Ana Oliveira", aluno.getNome());
        assertEquals("ana@email.com", aluno.getEmail());
        assertEquals("11977776666", aluno.getTelefone());
        assertEquals("12345678909", aluno.getCpf());
        assertEquals("Av Paulista", aluno.getEndereco().getLogradouro());
    }

    @Test
    @DisplayName("Deveria atualizar apenas nome, telefone e endereço, mantendo email e cpf")
    void atualizarInformacoesRestritas() {
        var dadosEndereco = new DadosEndereco("Av Paulista", "1000", null, "Bela Vista", "São Paulo", "SP", "01310-100");
        var aluno = new Aluno(new DadosCadastroAluno("Ana", "ana@email.com", "11977776666", "12345678909", dadosEndereco));

        var novoEndereco = new DadosEndereco("Av Brasil", "500", "Bloco B", "Jardins", "São Paulo", "SP", "01430-000");
        var dadosAtualizacao = new DadosAtualizacaoAluno(1L, "Ana Souza", "11911112222", novoEndereco);

        aluno.atualizarInformacoes(dadosAtualizacao);

        assertEquals("Ana Souza", aluno.getNome());
        assertEquals("11911112222", aluno.getTelefone());
        assertEquals("Av Brasil", aluno.getEndereco().getLogradouro());
        assertEquals("ana@email.com", aluno.getEmail()); // Não alterado
        assertEquals("12345678909", aluno.getCpf()); // Não alterado
    }

    @Test
    @DisplayName("Deveria realizar exclusão lógica tornando ativo = false")
    void exclusaoLogica() {
        var dadosEndereco = new DadosEndereco("Av Paulista", "1000", null, "Bela Vista", "São Paulo", "SP", "01310-100");
        var aluno = new Aluno(new DadosCadastroAluno("Ana", "ana@email.com", "11977776666", "12345678909", dadosEndereco));

        assertTrue(aluno.getAtivo());
        aluno.excluir();
        assertFalse(aluno.getAtivo());
    }
}
