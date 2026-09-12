package com.autoescola.agendamento.domain.instrucao.validacoes.agendamento;

import com.autoescola.agendamento.domain.ValidacaoException;
import com.autoescola.agendamento.domain.aluno.AlunoRepository;
import com.autoescola.agendamento.domain.instrucao.DadosAgendamentoInstrucao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorAlunoAtivo implements ValidadorAgendamentoInstrucao {

    @Autowired
    private AlunoRepository repository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        var aluno = repository.getReferenceById(dados.idAluno());
        if (aluno == null || !aluno.getAtivo()) {
            throw new ValidacaoException("Instrução não pode ser agendada com aluno inativo!");
        }
    }
}
