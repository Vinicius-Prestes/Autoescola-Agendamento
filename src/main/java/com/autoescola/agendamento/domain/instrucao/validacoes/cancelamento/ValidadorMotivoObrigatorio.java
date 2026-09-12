package com.autoescola.agendamento.domain.instrucao.validacoes.cancelamento;

import com.autoescola.agendamento.domain.ValidacaoException;
import com.autoescola.agendamento.domain.instrucao.DadosCancelamentoInstrucao;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMotivoObrigatorio implements ValidadorCancelamentoInstrucao {

    @Override
    public void validar(DadosCancelamentoInstrucao dados) {
        if (dados.motivo() == null) {
            throw new ValidacaoException("O motivo do cancelamento é obrigatório!");
        }
    }
}
