package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.CadastrarLivroRequest;
import br.com.biblioteca.application.dto.LivroResponse;
import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.LivroRepository;
import br.com.biblioteca.domain.repository.Pagina;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LivroUseCasesTest {

    @Mock LivroRepository repository;

    @Test
    @DisplayName("cadastrar cria o livro como disponível")
    void cadastrarNasceDisponivel() {
        when(repository.salvar(any(Livro.class))).thenAnswer(i -> {
            Livro l = i.getArgument(0);
            return new Livro(7L, l.getTitulo(), l.getAutor(), l.getIsbn(), l.isDisponivel());
        });

        LivroResponse response = new CadastrarLivroUseCase(repository)
                .executar(new CadastrarLivroRequest("Clean Code", "Robert C. Martin", "9780132350884"));

        assertThat(response.id()).isEqualTo(7L);
        assertThat(response.disponivel()).isTrue();
        assertThat(response.isbn()).isEqualTo("9780132350884");
    }

    @Test
    @DisplayName("buscar por id devolve o livro")
    void buscarPorId() {
        when(repository.buscarPorId(1L))
                .thenReturn(Optional.of(new Livro(1L, "Clean Code", "Robert C. Martin", "9780132350884", true)));

        LivroResponse response = new BuscarLivroPorIdUseCase(repository).executar(1L);

        assertThat(response.titulo()).isEqualTo("Clean Code");
    }

    @Test
    @DisplayName("buscar por id inexistente lança RecursoNaoEncontradoException")
    void buscarPorIdInexistente() {
        when(repository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> new BuscarLivroPorIdUseCase(repository).executar(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    @DisplayName("excluir remove o livro existente")
    void excluirExistente() {
        when(repository.buscarPorId(1L))
                .thenReturn(Optional.of(new Livro(1L, "Clean Code", "Robert C. Martin", "9780132350884", true)));

        new ExcluirLivroUseCase(repository).executar(1L);

        verify(repository).excluir(1L);
    }

    @Test
    @DisplayName("excluir livro inexistente lança exceção e não exclui nada")
    void excluirInexistente() {
        when(repository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> new ExcluirLivroUseCase(repository).executar(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
        verify(repository, never()).excluir(any());
    }

    @Test
    @DisplayName("consulta limita o tamanho da página a 100 e converte página negativa para 0")
    void consultaLimitaPaginacao() {
        when(repository.buscar(any(), any(), any(), any(), eq(0), eq(100)))
                .thenReturn(new Pagina<>(List.of(), 0, 100, 0, 0));

        new BuscarLivrosUseCase(repository).executar(null, null, null, null, -5, 1000);

        verify(repository).buscar(any(), any(), any(), any(), eq(0), eq(100));
    }

    @Test
    @DisplayName("consulta repassa os filtros e converte os livros em resposta")
    void consultaRepassaFiltros() {
        Livro livro = new Livro(1L, "O Hobbit", "J. R. R. Tolkien", "9780547928227", true);
        when(repository.buscar("hobbit", "tolkien", null, true, 0, 10))
                .thenReturn(new Pagina<>(List.of(livro), 0, 10, 1, 1));

        Pagina<LivroResponse> pagina = new BuscarLivrosUseCase(repository)
                .executar("hobbit", "tolkien", null, true, 0, 10);

        assertThat(pagina.totalElementos()).isEqualTo(1);
        assertThat(pagina.conteudo()).extracting(LivroResponse::titulo).containsExactly("O Hobbit");
        ArgumentCaptor<Integer> tamanho = ArgumentCaptor.forClass(Integer.class);
        verify(repository).buscar(any(), any(), any(), any(), eq(0), tamanho.capture());
        assertThat(tamanho.getValue()).isEqualTo(10);
    }
}
