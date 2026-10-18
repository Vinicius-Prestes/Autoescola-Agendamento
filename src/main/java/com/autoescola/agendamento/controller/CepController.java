package com.autoescola.agendamento.controller;

import com.autoescola.agendamento.infra.viacep.DadosEnderecoViaCep;
import com.autoescola.agendamento.infra.viacep.ViaCepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cep")
@Tag(name = "CEP", description = "Consulta de endereço via WebService externo (ViaCEP)")
public class CepController {

    @Autowired
    private ViaCepService viaCepService;

    @GetMapping("/{cep}")
    @Operation(summary = "Consultar endereço pelo CEP", description = "Consome a API externa do ViaCEP retornando os dados do logradouro, bairro, localidade e UF.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço localizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "CEP inválido ou não encontrado")
    })
    public ResponseEntity<DadosEnderecoViaCep> consultar(@PathVariable String cep) {
        var dados = viaCepService.consultarCep(cep);
        return ResponseEntity.ok(dados);
    }
}