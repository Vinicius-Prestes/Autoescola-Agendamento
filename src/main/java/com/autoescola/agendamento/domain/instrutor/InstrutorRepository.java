package com.autoescola.agendamento.domain.instrutor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {
    Page<Instrutor> findAllByAtivoTrue(Pageable pageable);

    @Query("""
            select i from Instrutor i
            where i.ativo = true
            and (:especialidade is null or i.especialidade = :especialidade)
            and i.id not in (
                select ins.instrutor.id from Instrucao ins
                where ins.data = :data
                and ins.motivoCancelamento is null
            )
            """)
    List<Instrutor> buscarInstrutoresLivresNaData(Especialidade especialidade, LocalDateTime data);
}
