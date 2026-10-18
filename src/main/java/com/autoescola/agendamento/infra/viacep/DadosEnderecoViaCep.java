package com.autoescola.agendamento.infra.viacep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEnderecoViaCep(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        Boolean erro
) {
}