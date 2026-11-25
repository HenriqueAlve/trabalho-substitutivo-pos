package br.com.biblioteca.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI biblioteca() {
        return new OpenAPI().info(new Info()
                .title("API Biblioteca Online")
                .version("1.0.0")
                .description("""
                        Sistema de biblioteca online: cadastro de livros e usuários, empréstimos, \
                        devoluções, reservas, consulta com filtros e relatórios.

                        **Convenções**
                        - Datas no formato ISO-8601 (`yyyy-MM-dd`), calculadas em **UTC**.
                        - Prazo de empréstimo: **14 dias** a partir da data do empréstimo.
                        - Consultas de livros são **paginadas** (`pagina` começa em 0; `tamanho` máximo 100).
                        - Erros de regra de negócio respondem **409**; recurso inexistente responde **404**; \
                        dados inválidos respondem **400**.
                        """));
    }
}
