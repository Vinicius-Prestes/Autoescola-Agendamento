package com.autoescola.agendamento.domain.instrucao.validacoes.agendamento;

import com.autoescola.agendamento.domain.ValidacaoException;
import com.autoescola.agendamento.domain.instrucao.DadosAgendamentoInstrucao;
import com.autoescola.agendamento.domain.instrutor.InstrutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorInstrutorAtivo implements ValidadorAgendamentoInstrucao {

    @Autowired
    private InstrutorRepository repository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        if (dados.idInstrutor() == null) {
            return;
        }

        var instrutor = repository.getReferenceById(dados.idInstrutor());
        if (instrutor == null || !instrutor.getAtivo()) {
            throw new ValidacaoException("Instrução não pode ser agendada com instrutor inativo!");
        }
    }
}
