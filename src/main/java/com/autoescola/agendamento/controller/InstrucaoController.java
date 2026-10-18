package com.autoescola.agendamento.controller;

import com.autoescola.agendamento.domain.instrucao.AgendaDeInstrucoes;
import com.autoescola.agendamento.domain.instrucao.DadosAgendamentoInstrucao;
import com.autoescola.agendamento.domain.instrucao.DadosCancelamentoInstrucao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("instrucoes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Instruções", description = "Agendamento e cancelamento de instruções práticas da autoescola")
public class InstrucaoController {

    @Autowired
    private AgendaDeInstrucoes agenda;

    @PostMapping
    @Transactional
    @Operation(summary = "Agendar instrução prática", description = "Realiza o agendamento de uma aula prática validando regras de horário de funcionamento, antecedência mínima, limite de aulas por aluno e disponibilidade do instrutor. Se o instrutor for omitido, escolhe aleatoriamente um instrutor disponível na especialidade.")
    public ResponseEntity agendar(@RequestBody @Valid DadosAgendamentoInstrucao dados) {
        var dto = agenda.agendar(dados);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    @Transactional
    @Operation(summary = "Cancelar instrução prática", description = "Cancela uma instrução agendada com antecedência mínima de 24 horas, exigindo a especificação do motivo (ALUNO_DESISTIU, INSTRUTOR_CANCELOU ou OUTROS).")
    public ResponseEntity cancelar(@RequestBody @Valid DadosCancelamentoInstrucao dados) {
        agenda.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
}
