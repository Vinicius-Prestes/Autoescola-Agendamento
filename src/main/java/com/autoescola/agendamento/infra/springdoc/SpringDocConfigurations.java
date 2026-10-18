package com.autoescola.agendamento.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Autoescola - API de Agendamento")
                        .description("API REST para gerenciamento de autoescola, contemplando CRUD de instrutores, alunos e usuários, autenticação JWT, integração com WebService externo (ViaCEP) e regras de negócio para agendamento e cancelamento de instruções práticas.")
                        .version("v1.0"));

    }
}
