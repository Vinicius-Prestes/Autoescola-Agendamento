package com.autoescola.agendamento.infra.viacep;

import com.autoescola.agendamento.domain.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViaCepServiceTest {

    private final ViaCepService viaCepService = new ViaCepService();

    @Test
    @DisplayName("Deveria consultar CEP valido da FIAP Paulista com sucesso")
    void consultarCepValido() {
        var resultado = viaCepService.consultarCep("01311000");

        assertNotNull(resultado);
        assertEquals("SP", resultado.uf());
        assertEquals("São Paulo", resultado.localidade());
        assertNotNull(resultado.logradouro());
        assertFalse(Boolean.TRUE.equals(resultado.erro()));
    }

    @Test
    @DisplayName("Deveria bloquear CEP com quantidade de digitos incorreta")
    void bloquearCepDigitosIncorretos() {
        assertThrows(ValidacaoException.class, () -> viaCepService.consultarCep("12345"));
        assertThrows(ValidacaoException.class, () -> viaCepService.consultarCep("123456789"));
    }

    @Test
    @DisplayName("Deveria bloquear CEP nulo ou vazio")
    void bloquearCepVazio() {
        assertThrows(ValidacaoException.class, () -> viaCepService.consultarCep(null));
        assertThrows(ValidacaoException.class, () -> viaCepService.consultarCep("   "));
    }
}