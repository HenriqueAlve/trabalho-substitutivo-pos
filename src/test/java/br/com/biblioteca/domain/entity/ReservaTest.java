package br.com.biblioteca.domain.entity;

import br.com.biblioteca.domain.enums.Perfil;

import br.com.biblioteca.domain.enums.StatusReserva;
import br.com.biblioteca.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservaTest {

    private final Usuario usuario = new Usuario(2L, "João Souza", "joao@email.com", "hash", Perfil.USUARIO);
    private final LocalDate hoje = LocalDate.of(2026, 11, 25);

    private Livro livro(boolean disponivel) {
        return new Livro(1L, "Clean Code", "Robert C. Martin", "9780132350884", disponivel);
    }

    @Test
    @DisplayName("reserva de livro emprestado nasce ATIVA")
    void reservaNasceAtiva() {
        Reserva reserva = Reserva.nova(livro(false), usuario, hoje);

        assertThat(reserva.getStatus()).isEqualTo(StatusReserva.ATIVA);
        assertThat(reserva.getDataReserva()).isEqualTo(hoje);
    }

    @Test
    @DisplayName("não reserva livro disponível")
    void naoReservaLivroDisponivel() {
        assertThatThrownBy(() -> Reserva.nova(livro(true), usuario, hoje))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("empréstimo direto");
    }

    @Test
    @DisplayName("cancelar uma reserva ativa muda o status para CANCELADA")
    void cancelarReservaAtiva() {
        Reserva reserva = Reserva.nova(livro(false), usuario, hoje);

        reserva.cancelar();

        assertThat(reserva.getStatus()).isEqualTo(StatusReserva.CANCELADA);
    }

    @Test
    @DisplayName("cancelar uma reserva que não está ativa lança exceção")
    void cancelarReservaJaCancelada() {
        Reserva reserva = Reserva.nova(livro(false), usuario, hoje);
        reserva.cancelar();

        assertThatThrownBy(reserva::cancelar).isInstanceOf(RegraDeNegocioException.class);
    }
}
