package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.ReservaResponse;
import br.com.biblioteca.application.dto.ReservarLivroRequest;
import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.entity.Reserva;
import br.com.biblioteca.domain.entity.Usuario;
import br.com.biblioteca.domain.enums.StatusReserva;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.exception.RegraDeNegocioException;
import br.com.biblioteca.domain.repository.LivroRepository;
import br.com.biblioteca.domain.repository.ReservaRepository;
import br.com.biblioteca.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaUseCasesTest {

    @Mock ReservaRepository reservaRepository;
    @Mock LivroRepository livroRepository;
    @Mock UsuarioRepository usuarioRepository;

    private final Clock clock = Clock.fixed(Instant.parse("2026-11-25T10:00:00Z"), ZoneOffset.UTC);
    private final Usuario usuario = new Usuario(2L, "João Souza", "joao@email.com");
    private final ReservarLivroRequest request = new ReservarLivroRequest(1L, 2L);

    private Livro livro(boolean disponivel) {
        return new Livro(1L, "Clean Code", "Robert C. Martin", "9780132350884", disponivel);
    }

    private ReservarLivroUseCase reservar() {
        return new ReservarLivroUseCase(reservaRepository, livroRepository, usuarioRepository, clock);
    }

    @Test
    @DisplayName("reserva um livro emprestado e devolve a reserva ATIVA")
    void reservaComSucesso() {
        when(livroRepository.buscarPorId(1L)).thenReturn(Optional.of(livro(false)));
        when(usuarioRepository.buscarPorId(2L)).thenReturn(Optional.of(usuario));
        when(reservaRepository.existeAtiva(1L, 2L)).thenReturn(false);
        when(reservaRepository.salvar(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));

        ReservaResponse response = reservar().executar(request);

        assertThat(response.status()).isEqualTo(StatusReserva.ATIVA);
        assertThat(response.dataReserva()).isEqualTo(LocalDate.of(2026, 11, 25));
        assertThat(response.livroId()).isEqualTo(1L);
        assertThat(response.usuarioId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("não reserva livro disponível")
    void livroDisponivel() {
        when(livroRepository.buscarPorId(1L)).thenReturn(Optional.of(livro(true)));
        when(usuarioRepository.buscarPorId(2L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> reservar().executar(request))
                .isInstanceOf(RegraDeNegocioException.class);
        verify(reservaRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("não permite duas reservas ativas do mesmo livro para o mesmo usuário")
    void reservaDuplicada() {
        when(livroRepository.buscarPorId(1L)).thenReturn(Optional.of(livro(false)));
        when(usuarioRepository.buscarPorId(2L)).thenReturn(Optional.of(usuario));
        when(reservaRepository.existeAtiva(1L, 2L)).thenReturn(true);

        assertThatThrownBy(() -> reservar().executar(request))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("reserva ativa");
        verify(reservaRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("livro inexistente lança RecursoNaoEncontradoException")
    void livroInexistente() {
        when(livroRepository.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservar().executar(request))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    @DisplayName("usuário inexistente lança RecursoNaoEncontradoException")
    void usuarioInexistente() {
        when(livroRepository.buscarPorId(1L)).thenReturn(Optional.of(livro(false)));
        when(usuarioRepository.buscarPorId(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservar().executar(request))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    @DisplayName("cancela uma reserva ativa")
    void cancelaReservaAtiva() {
        Reserva reserva = Reserva.nova(livro(false), usuario, LocalDate.of(2026, 11, 24));
        when(reservaRepository.buscarPorId(5L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.salvar(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));

        ReservaResponse response = new CancelarReservaUseCase(reservaRepository).executar(5L);

        assertThat(response.status()).isEqualTo(StatusReserva.CANCELADA);
    }

    @Test
    @DisplayName("cancelar reserva inexistente lança RecursoNaoEncontradoException")
    void cancelarInexistente() {
        when(reservaRepository.buscarPorId(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> new CancelarReservaUseCase(reservaRepository).executar(5L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    @DisplayName("cancelar uma reserva já cancelada lança exceção e não persiste")
    void cancelarJaCancelada() {
        Reserva reserva = Reserva.nova(livro(false), usuario, LocalDate.of(2026, 11, 24));
        reserva.cancelar();
        when(reservaRepository.buscarPorId(5L)).thenReturn(Optional.of(reserva));

        assertThatThrownBy(() -> new CancelarReservaUseCase(reservaRepository).executar(5L))
                .isInstanceOf(RegraDeNegocioException.class);
        verify(reservaRepository, never()).salvar(any());
    }
}
