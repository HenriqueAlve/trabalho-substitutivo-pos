package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.EmprestimoResponse;
import br.com.biblioteca.domain.entity.Emprestimo;
import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.entity.Usuario;
import br.com.biblioteca.domain.exception.EmprestimoJaDevolvidoException;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.EmprestimoRepository;
import br.com.biblioteca.domain.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
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
class DevolverLivroUseCaseTest {

    @Mock EmprestimoRepository emprestimoRepository;
    @Mock LivroRepository livroRepository;

    private final Clock clock = Clock.fixed(Instant.parse("2026-12-01T10:00:00Z"), ZoneOffset.UTC);
    private DevolverLivroUseCase useCase;

    private final Usuario usuario = new Usuario(2L, "Maria Silva", "maria@email.com");

    @BeforeEach
    void setUp() {
        useCase = new DevolverLivroUseCase(emprestimoRepository, livroRepository, clock);
    }

    private Emprestimo emprestimoEmAberto(Livro livro) {
        return new Emprestimo(10L, livro, usuario,
                LocalDate.of(2026, 11, 25), LocalDate.of(2026, 12, 9), null);
    }

    @Test
    @DisplayName("devolve, registra a data de hoje e libera o livro")
    void devolveComSucesso() {
        Livro livro = new Livro(1L, "Clean Code", "Robert C. Martin", "9780132350884", false);
        when(emprestimoRepository.buscarPorId(10L)).thenReturn(Optional.of(emprestimoEmAberto(livro)));
        when(emprestimoRepository.salvar(any(Emprestimo.class))).thenAnswer(i -> i.getArgument(0));

        EmprestimoResponse response = useCase.executar(10L);

        assertThat(response.dataDevolucao()).isEqualTo(LocalDate.of(2026, 12, 1));
        assertThat(livro.isDisponivel()).isTrue();
        verify(livroRepository).salvar(livro);
    }

    @Test
    @DisplayName("empréstimo inexistente lança RecursoNaoEncontradoException")
    void emprestimoInexistente() {
        when(emprestimoRepository.buscarPorId(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(10L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
        verify(emprestimoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("empréstimo já devolvido lança EmprestimoJaDevolvidoException e não persiste nada")
    void jaDevolvido() {
        Livro livro = new Livro(1L, "Clean Code", "Robert C. Martin", "9780132350884", true);
        Emprestimo devolvido = new Emprestimo(10L, livro, usuario,
                LocalDate.of(2026, 11, 25), LocalDate.of(2026, 12, 9), LocalDate.of(2026, 11, 30));
        when(emprestimoRepository.buscarPorId(10L)).thenReturn(Optional.of(devolvido));

        assertThatThrownBy(() -> useCase.executar(10L))
                .isInstanceOf(EmprestimoJaDevolvidoException.class);
        verify(livroRepository, never()).salvar(any());
        verify(emprestimoRepository, never()).salvar(any());
    }
}
