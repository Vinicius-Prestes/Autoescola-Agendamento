package com.autoescola.agendamento.domain.instrucao.validacoes.agendamento;

import com.autoescola.agendamento.domain.ValidacaoException;
import com.autoescola.agendamento.domain.instrucao.DadosAgendamentoInstrucao;
import com.autoescola.agendamento.domain.instrucao.InstrucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorInstrutorComOutraInstrucaoNoMesmoHorario implements ValidadorAgendamentoInstrucao {

    @Autowired
    private InstrucaoRepository repository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        if (dados.idInstrutor() == null) {
            return;
        }

        var instrutorOcupado = repository.existsByInstrutorIdAndDataAndMotivoCancelamentoIsNull(dados.idInstrutor(), dados.data());
        if (instrutorOcupado) {
            throw new ValidacaoException("Instrutor já possui outra instrução agendada nesse mesmo horário!");
        }
    }
}
