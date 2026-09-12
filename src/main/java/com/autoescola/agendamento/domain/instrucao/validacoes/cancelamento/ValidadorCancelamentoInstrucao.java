package com.autoescola.agendamento.domain.instrucao.validacoes.cancelamento;

import com.autoescola.agendamento.domain.instrucao.DadosCancelamentoInstrucao;

public interface ValidadorCancelamentoInstrucao {
    void validar(DadosCancelamentoInstrucao dados);
}
