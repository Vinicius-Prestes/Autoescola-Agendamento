package com.autoescola.agendamento.domain.instrucao.validacoes.agendamento;

import com.autoescola.agendamento.domain.ValidacaoException;
import com.autoescola.agendamento.domain.instrucao.DadosAgendamentoInstrucao;
import com.autoescola.agendamento.domain.instrucao.InstrucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMaximoInstrucoesAlunoNoDia implements ValidadorAgendamentoInstrucao {

    @Autowired
    private InstrucaoRepository repository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        var primeiroHorario = dados.data().withHour(0).withMinute(0).withSecond(0);
        var ultimoHorario = dados.data().withHour(23).withMinute(59).withSecond(59);

        var quantidadeNoDia = repository.countByAlunoIdAndDataBetweenAndMotivoCancelamentoIsNull(dados.idAluno(), primeiroHorario, ultimoHorario);

        if (quantidadeNoDia != null && quantidadeNoDia >= 2) {
            throw new ValidacaoException("Aluno não pode agendar mais de 2 instruções no mesmo dia!");
        }
    }
}
