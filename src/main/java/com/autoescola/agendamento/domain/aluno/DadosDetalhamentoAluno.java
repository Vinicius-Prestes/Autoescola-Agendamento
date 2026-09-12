package com.autoescola.agendamento.domain.aluno;

import com.autoescola.agendamento.domain.endereco.Endereco;

public record DadosDetalhamentoAluno(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        Endereco endereco,
        Boolean ativo
) {
    public DadosDetalhamentoAluno(Aluno aluno) {
        this(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getCpf(),
                aluno.getEndereco(),
                aluno.getAtivo()
        );
    }
}
