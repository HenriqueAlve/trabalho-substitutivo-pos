package br.com.biblioteca.domain.entity;

import br.com.biblioteca.domain.enums.Perfil;

import br.com.biblioteca.domain.exception.EmprestimoJaDevolvidoException;
import br.com.biblioteca.domain.exception.LivroIndisponivelException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmprestimoTest {

    private final Usuario usuario = new Usuario(1L, "Maria Silva", "maria@email.com", "hash", Perfil.USUARIO);

    private Livro livro(boolean disponivel) {
        return new Livro(1L, "Clean Code", "Robert C. Martin", "9780132350884", disponivel);
    }

    @Test
    @DisplayName("novo empréstimo prevê a devolução em 14 dias e deixa o livro indisponível")
    void novoCalculaPrazoDe14Dias() {
        Livro livro = livro(true);
        LocalDate hoje = LocalDate.of(2026, 11, 25);

        Emprestimo emprestimo = Emprestimo.novo(livro, usuario, hoje);

        assertThat(emprestimo.getDataEmprestimo()).isEqualTo(hoje);
        assertThat(emprestimo.getDataPrevistaDevolucao()).isEqualTo(LocalDate.of(2026, 12, 9));
        assertThat(emprestimo.getDataDevolucao()).isNull();
        assertThat(livro.isDisponivel()).isFalse();
    }

    @Test
    @DisplayName("o prazo de 14 dias atravessa a virada de ano corretamente")
    void prazoAtravessaViradaDeAno() {
        Emprestimo emprestimo = Emprestimo.novo(livro(true), usuario, LocalDate.of(2026, 12, 25));

        assertThat(emprestimo.getDataPrevistaDevolucao()).isEqualTo(LocalDate.of(2027, 1, 8));
    }

    @Test
    @DisplayName("não permite emprestar um livro indisponível")
    void naoEmprestaLivroIndisponivel() {
        assertThatThrownBy(() -> Emprestimo.novo(livro(false), usuario, LocalDate.of(2026, 11, 25)))
                .isInstanceOf(LivroIndisponivelException.class);
    }

    @Test
    @DisplayName("devolver registra a data e libera o livro")
    void devolverRegistraDataELiberaLivro() {
        Livro livro = livro(true);
        Emprestimo emprestimo = Emprestimo.novo(livro, usuario, LocalDate.of(2026, 11, 25));

        emprestimo.devolver(LocalDate.of(2026, 12, 1));

        assertThat(emprestimo.getDataDevolucao()).isEqualTo(LocalDate.of(2026, 12, 1));
        assertThat(livro.isDisponivel()).isTrue();
    }

    @Test
    @DisplayName("devolver duas vezes lança exceção")
    void devolverDuasVezesLancaExcecao() {
        Emprestimo emprestimo = Emprestimo.novo(livro(true), usuario, LocalDate.of(2026, 11, 25));
        emprestimo.devolver(LocalDate.of(2026, 12, 1));

        assertThatThrownBy(() -> emprestimo.devolver(LocalDate.of(2026, 12, 2)))
                .isInstanceOf(EmprestimoJaDevolvidoException.class);
    }
}
