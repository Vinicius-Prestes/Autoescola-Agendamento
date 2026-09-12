package com.autoescola.agendamento.controller;

import com.autoescola.agendamento.domain.instrucao.AgendaDeInstrucoes;
import com.autoescola.agendamento.domain.instrucao.DadosAgendamentoInstrucao;
import com.autoescola.agendamento.domain.instrucao.DadosCancelamentoInstrucao;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("instrucoes")
public class InstrucaoController {

    @Autowired
    private AgendaDeInstrucoes agenda;

    @PostMapping
    @Transactional
    public ResponseEntity agendar(@RequestBody @Valid DadosAgendamentoInstrucao dados) {
        var dto = agenda.agendar(dados);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity cancelar(@RequestBody @Valid DadosCancelamentoInstrucao dados) {
        agenda.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
}
