package br.com.biblioteca.domain.entity;

import br.com.biblioteca.domain.exception.LivroIndisponivelException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LivroTest {

    private Livro livro(boolean disponivel) {
        return new Livro(1L, "Clean Code", "Robert C. Martin", "9780132350884", disponivel);
    }

    @Test
    @DisplayName("emprestar torna o livro indisponível")
    void emprestarTornaIndisponivel() {
        Livro livro = livro(true);

        livro.emprestar();

        assertThat(livro.isDisponivel()).isFalse();
    }

    @Test
    @DisplayName("emprestar um livro já emprestado lança exceção")
    void emprestarLivroIndisponivelLancaExcecao() {
        Livro livro = livro(false);

        assertThatThrownBy(livro::emprestar)
                .isInstanceOf(LivroIndisponivelException.class)
                .hasMessageContaining("9780132350884");
    }

    @Test
    @DisplayName("devolver torna o livro disponível")
    void devolverTornaDisponivel() {
        Livro livro = livro(false);

        livro.devolver();

        assertThat(livro.isDisponivel()).isTrue();
    }

    @Test
    @DisplayName("atualizar altera título, autor e ISBN sem mexer na disponibilidade")
    void atualizarAlteraDados() {
        Livro livro = livro(false);

        livro.atualizar("Código Limpo", "Uncle Bob", "9780321125217");

        assertThat(livro.getTitulo()).isEqualTo("Código Limpo");
        assertThat(livro.getAutor()).isEqualTo("Uncle Bob");
        assertThat(livro.getIsbn()).isEqualTo("9780321125217");
        assertThat(livro.isDisponivel()).isFalse();
    }
}
