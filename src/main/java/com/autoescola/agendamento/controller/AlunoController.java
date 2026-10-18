package com.autoescola.agendamento.controller;

import com.autoescola.agendamento.domain.aluno.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("alunos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Alunos", description = "Operações de cadastro, listagem, atualização e inativação de alunos")
public class AlunoController {

    @Autowired
    private AlunoRepository repository;

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastrar novo aluno", description = "Cadastra um aluno com nome, email, telefone, CPF e endereço completo.")
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroAluno dados, UriComponentsBuilder uriBuilder) {
        var aluno = new Aluno(dados);
        repository.save(aluno);

        var uri = uriBuilder.path("/alunos/{id}").buildAndExpand(aluno.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoAluno(aluno));
    }

    @GetMapping
    @Operation(summary = "Listar alunos ativos", description = "Retorna lista paginada (10 por página) de alunos com status ativo, ordenada por nome.")
    public ResponseEntity<Page<DadosListagemAluno>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemAluno::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    @Operation(summary = "Atualizar aluno", description = "Atualiza nome, telefone e endereço do aluno. Não permite alterar CPF nem email.")
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoAluno dados) {
        var aluno = repository.getReferenceById(dados.id());
        aluno.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoAluno(aluno));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Excluir aluno", description = "Realiza a exclusão lógica do aluno, tornando seu status inativo no sistema.")
    public ResponseEntity excluir(@PathVariable Long id) {
        var aluno = repository.getReferenceById(id);
        aluno.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar aluno", description = "Retorna os detalhes completos do aluno identificado pelo ID.")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var aluno = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoAluno(aluno));
    }
}
