package br.com.biblioteca;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OpenApiDocsTest {

    @Autowired
    MockMvc mvc;

    @Test
    void documentacaoExpoeTodosOsRecursos() throws Exception {
        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.allOf(
                        org.hamcrest.Matchers.containsString("/livros"),
                        org.hamcrest.Matchers.containsString("/usuarios"),
                        org.hamcrest.Matchers.containsString("/emprestimos"),
                        org.hamcrest.Matchers.containsString("/reservas"),
                        org.hamcrest.Matchers.containsString("/relatorios/livros-mais-emprestados"),
                        org.hamcrest.Matchers.containsString("API Biblioteca Online"))));
    }
}
