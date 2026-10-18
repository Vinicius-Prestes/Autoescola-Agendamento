package com.autoescola.agendamento.controller;

import com.autoescola.agendamento.domain.instrutor.*;
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
@RequestMapping("instrutores")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Instrutores", description = "Operações de cadastro, listagem, atualização e inativação de instrutores")
public class InstrutorController {

    @Autowired
    private InstrutorRepository repository;

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastrar novo instrutor", description = "Cadastra um instrutor com nome, email, telefone, CNH, especialidade (MOTOS, CARROS, VANS ou CAMINHOES) e endereço.")
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroInstrutor dados, UriComponentsBuilder uriBuilder) {
        var instrutor = new Instrutor(dados);
        repository.save(instrutor);

        var uri = uriBuilder.path("/instrutores/{id}").buildAndExpand(instrutor.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoInstrutor(instrutor));
    }

    @GetMapping
    @Operation(summary = "Listar instrutores ativos", description = "Retorna lista paginada (10 por página) de instrutores com status ativo, ordenada por nome.")
    public ResponseEntity<Page<DadosListagemInstrutor>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemInstrutor::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    @Operation(summary = "Atualizar instrutor", description = "Atualiza nome, telefone e endereço do instrutor. Não permite alterar email, CNH nem especialidade.")
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoInstrutor dados) {
        var instrutor = repository.getReferenceById(dados.id());
        instrutor.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoInstrutor(instrutor));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Excluir instrutor", description = "Realiza a exclusão lógica do instrutor, tornando seu status inativo no sistema.")
    public ResponseEntity excluir(@PathVariable Long id) {
        var instrutor = repository.getReferenceById(id);
        instrutor.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar instrutor", description = "Retorna os detalhes completos do instrutor identificado pelo ID.")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var instrutor = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoInstrutor(instrutor));
    }
}
