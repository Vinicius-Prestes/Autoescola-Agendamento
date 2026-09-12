package com.autoescola.agendamento.domain.instrucao.validacoes.cancelamento;

import com.autoescola.agendamento.domain.ValidacaoException;
import com.autoescola.agendamento.domain.instrucao.DadosCancelamentoInstrucao;
import com.autoescola.agendamento.domain.instrucao.InstrucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedenciaCancelamento implements ValidadorCancelamentoInstrucao {

    @Autowired
    private InstrucaoRepository repository;

    @Override
    public void validar(DadosCancelamentoInstrucao dados) {
        if (!repository.existsById(dados.idInstrucao())) {
            throw new ValidacaoException("Id da instrução informado não existe!");
        }

        var instrucao = repository.getReferenceById(dados.idInstrucao());
        var agora = LocalDateTime.now();
        var diferencaEmHoras = Duration.between(agora, instrucao.getData()).toHours();

        if (diferencaEmHoras < 24) {
            throw new ValidacaoException("Instrução somente pode ser cancelada com antecedência mínima de 24 horas!");
        }
    }
}
