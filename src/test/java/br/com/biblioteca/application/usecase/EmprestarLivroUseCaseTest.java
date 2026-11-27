package br.com.biblioteca.application.usecase;

import br.com.biblioteca.domain.enums.Perfil;

import br.com.biblioteca.application.dto.EmprestarLivroRequest;
import br.com.biblioteca.application.dto.EmprestimoResponse;
import br.com.biblioteca.domain.entity.Emprestimo;
import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.entity.Usuario;
import br.com.biblioteca.domain.exception.LivroIndisponivelException;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.EmprestimoRepository;
import br.com.biblioteca.domain.repository.LivroRepository;
import br.com.biblioteca.domain.repository.UsuarioRepository;
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
class EmprestarLivroUseCaseTest {

    @Mock EmprestimoRepository emprestimoRepository;
    @Mock UsuarioRepository usuarioRepository;
    @Mock LivroRepository livroRepository;

    private final Clock clock = Clock.fixed(Instant.parse("2026-11-25T10:00:00Z"), ZoneOffset.UTC);
    private EmprestarLivroUseCase useCase;

    private final Usuario usuario = new Usuario(2L, "Maria Silva", "maria@email.com", "hash", Perfil.USUARIO);
    private final EmprestarLivroRequest request = new EmprestarLivroRequest(1L, 2L);

    @BeforeEach
    void setUp() {
        useCase = new EmprestarLivroUseCase(emprestimoRepository, usuarioRepository, livroRepository, clock);
    }

    private Livro livro(boolean disponivel) {
        return new Livro(1L, "Clean Code", "Robert C. Martin", "9780132350884", disponivel);
    }

    @Test
    @DisplayName("empresta o livro, calcula a data prevista e persiste livro e empréstimo")
    void emprestaComSucesso() {
        Livro livro = livro(true);
        when(livroRepository.buscarPorId(1L)).thenReturn(Optional.of(livro));
        when(usuarioRepository.buscarPorId(2L)).thenReturn(Optional.of(usuario));
        when(emprestimoRepository.salvar(any(Emprestimo.class))).thenAnswer(i -> i.getArgument(0));

        EmprestimoResponse response = useCase.executar(request);

        assertThat(response.dataEmprestimo()).isEqualTo(LocalDate.of(2026, 11, 25));
        assertThat(response.dataPrevistaDevolucao()).isEqualTo(LocalDate.of(2026, 12, 9));
        assertThat(response.dataDevolucao()).isNull();
        assertThat(response.livroId()).isEqualTo(1L);
        assertThat(response.usuarioId()).isEqualTo(2L);
        assertThat(livro.isDisponivel()).isFalse();
        verify(livroRepository).salvar(livro);
    }

    @Test
    @DisplayName("livro inexistente lança RecursoNaoEncontradoException")
    void livroInexistente() {
        when(livroRepository.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(request))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Livro");
        verify(emprestimoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("usuário inexistente lança RecursoNaoEncontradoException")
    void usuarioInexistente() {
        when(livroRepository.buscarPorId(1L)).thenReturn(Optional.of(livro(true)));
        when(usuarioRepository.buscarPorId(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(request))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Usuário");
        verify(emprestimoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("livro já emprestado lança LivroIndisponivelException e não persiste nada")
    void livroIndisponivel() {
        when(livroRepository.buscarPorId(1L)).thenReturn(Optional.of(livro(false)));
        when(usuarioRepository.buscarPorId(2L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> useCase.executar(request))
                .isInstanceOf(LivroIndisponivelException.class);
        verify(livroRepository, never()).salvar(any());
        verify(emprestimoRepository, never()).salvar(any());
    }
}
