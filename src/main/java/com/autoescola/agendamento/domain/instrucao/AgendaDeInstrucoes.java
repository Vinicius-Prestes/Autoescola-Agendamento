package com.autoescola.agendamento.domain.instrucao;

import com.autoescola.agendamento.domain.ValidacaoException;
import com.autoescola.agendamento.domain.aluno.AlunoRepository;
import com.autoescola.agendamento.domain.instrutor.Instrutor;
import com.autoescola.agendamento.domain.instrutor.InstrutorRepository;
import com.autoescola.agendamento.domain.instrucao.validacoes.agendamento.ValidadorAgendamentoInstrucao;
import com.autoescola.agendamento.domain.instrucao.validacoes.cancelamento.ValidadorCancelamentoInstrucao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class AgendaDeInstrucoes {

    @Autowired
    private InstrucaoRepository instrucaoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private List<ValidadorAgendamentoInstrucao> validadoresAgendamento;

    @Autowired
    private List<ValidadorCancelamentoInstrucao> validadoresCancelamento;

    public DadosDetalhamentoInstrucao agendar(DadosAgendamentoInstrucao dados) {
        if (!alunoRepository.existsById(dados.idAluno())) {
            throw new ValidacaoException("Id do aluno informado não existe!");
        }

        if (dados.idInstrutor() != null && !instrutorRepository.existsById(dados.idInstrutor())) {
            throw new ValidacaoException("Id do instrutor informado não existe!");
        }

        validadoresAgendamento.forEach(v -> v.validar(dados));

        var aluno = alunoRepository.getReferenceById(dados.idAluno());
        var instrutor = escolherInstrutor(dados);

        if (instrutor == null) {
            throw new ValidacaoException("Nenhum instrutor disponível nessa data/hora!");
        }

        var instrucao = new Instrucao(aluno, instrutor, dados.data());
        instrucaoRepository.save(instrucao);

        return new DadosDetalhamentoInstrucao(instrucao);
    }

    public void cancelar(DadosCancelamentoInstrucao dados) {
        if (!instrucaoRepository.existsById(dados.idInstrucao())) {
            throw new ValidacaoException("Id da instrução informado não existe!");
        }

        validadoresCancelamento.forEach(v -> v.validar(dados));

        var instrucao = instrucaoRepository.getReferenceById(dados.idInstrucao());
        instrucao.cancelar(dados.motivo());
    }

    private Instrutor escolherInstrutor(DadosAgendamentoInstrucao dados) {
        if (dados.idInstrutor() != null) {
            return instrutorRepository.getReferenceById(dados.idInstrutor());
        }

        var instrutoresLivres = instrutorRepository.buscarInstrutoresLivresNaData(dados.especialidade(), dados.data());
        if (instrutoresLivres.isEmpty()) {
            throw new ValidacaoException("Nenhum instrutor disponível para a data/horário informado!");
        }

        return instrutoresLivres.get(new Random().nextInt(instrutoresLivres.size()));
    }
}
