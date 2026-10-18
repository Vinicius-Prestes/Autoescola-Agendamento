package com.autoescola.agendamento.infra.viacep;

import com.autoescola.agendamento.domain.ValidacaoException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ViaCepService {

    private final RestClient restClient;

    public ViaCepService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://viacep.com.br/ws")
                .build();
    }

    public ViaCepService(RestClient restClient) {
        this.restClient = restClient;
    }

    public DadosEnderecoViaCep consultarCep(String cep) {
        if (cep == null || cep.isBlank()) {
            throw new ValidacaoException("CEP não pode ser vazio!");
        }

        var cepFormatado = cep.replaceAll("\\D", "");
        if (cepFormatado.length() != 8) {
            throw new ValidacaoException("CEP deve conter 8 dígitos numéricos!");
        }

        try {
            var resultado = restClient.get()
                    .uri("/{cep}/json", cepFormatado)
                    .retrieve()
                    .body(DadosEnderecoViaCep.class);

            if (resultado == null || Boolean.TRUE.equals(resultado.erro())) {
                throw new ValidacaoException("CEP não encontrado no ViaCEP: " + cep);
            }

            return resultado;
        } catch (ValidacaoException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ValidacaoException("Falha ao comunicar com o serviço do ViaCEP: " + ex.getMessage());
        }
    }
}