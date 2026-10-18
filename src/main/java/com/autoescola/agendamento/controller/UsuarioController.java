package com.autoescola.agendamento.controller;

import com.autoescola.agendamento.domain.ValidacaoException;
import com.autoescola.agendamento.domain.usuario.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("usuarios")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuários", description = "Operações de gerenciamento de usuários, perfis e alteração de senha")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastrar usuário", description = "Cadastra um novo usuário no sistema com senha criptografada via BCrypt. Apenas administradores (ADMIN).")
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {
        var senhaCriptografada = passwordEncoder.encode(dados.senha());
        var usuario = new Usuario(dados, senhaCriptografada);
        repository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));
    }

    @GetMapping
    @Operation(summary = "Listar usuários ativos", description = "Retorna lista paginada (10 por página) de usuários ativos, ordenada por login. Apenas administradores (ADMIN).")
    public ResponseEntity<Page<DadosListagemUsuario>> listar(@PageableDefault(size = 10, sort = {"login"}) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemUsuario::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    @Operation(summary = "Atualizar perfil do usuário", description = "Atualiza o perfil de acesso do usuário (ADMIN ou COMUM). Apenas administradores (ADMIN).")
    public ResponseEntity atualizarPerfil(@RequestBody @Valid DadosAtualizacaoPerfilUsuario dados) {
        var usuario = repository.getReferenceById(dados.id());
        usuario.atualizarPerfil(dados);
        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Excluir usuário", description = "Realiza a exclusão lógica do usuário, tornando-o inativo no sistema. Apenas administradores (ADMIN).")
    public ResponseEntity excluir(@PathVariable Long id) {
        var usuario = repository.getReferenceById(id);
        usuario.excluir();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/alterar-senha")
    @Transactional
    @Operation(summary = "Alterar própria senha", description = "Permite que o usuário autenticado altere sua própria senha, validando a senha atual informada.")
    public ResponseEntity alterarSenha(@RequestBody @Valid DadosAlteracaoSenha dados, @AuthenticationPrincipal Usuario usuarioLogado) {
        var usuario = repository.getReferenceById(usuarioLogado.getId());

        if (!passwordEncoder.matches(dados.senhaAtual(), usuario.getPassword())) {
            throw new ValidacaoException("Senha atual incorreta!");
        }

        usuario.alterarSenha(passwordEncoder.encode(dados.novaSenha()));
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }
}
