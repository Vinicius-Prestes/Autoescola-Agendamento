package com.autoescola.agendamento.domain.instrucao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface InstrucaoRepository extends JpaRepository<Instrucao, Long> {

    boolean existsByInstrutorIdAndDataAndMotivoCancelamentoIsNull(Long idInstrutor, LocalDateTime data);

    @Query("""
            select count(i) from Instrucao i
            where i.aluno.id = :idAluno
            and i.data between :primeiroHorario and :ultimoHorario
            and i.motivoCancelamento is null
            """)
    Long countByAlunoIdAndDataBetweenAndMotivoCancelamentoIsNull(Long idAluno, LocalDateTime primeiroHorario, LocalDateTime ultimoHorario);
}
