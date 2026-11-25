package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.EmprestimoAtivoResponse;
import br.com.biblioteca.domain.entity.Emprestimo;
import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.entity.Usuario;
import br.com.biblioteca.domain.repository.EmprestimoRepository;
import br.com.biblioteca.domain.repository.LivroMaisEmprestado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelatoriosUseCasesTest {

    @Mock EmprestimoRepository repository;

    private final Clock clock = Clock.fixed(Instant.parse("2026-11-25T10:00:00Z"), ZoneOffset.UTC);
    private final Usuario usuario = new Usuario(2L, "Maria Silva", "maria@email.com");

    private Emprestimo emAberto(Long id, LocalDate emprestado, LocalDate previsto) {
        Livro livro = new Livro(id, "Livro " + id, "Autor", "978000000000" + id, false);
        return new Emprestimo(id, livro, usuario, emprestado, previsto, null);
    }

    @Test
    @DisplayName("top de livros pede ao repositório exatamente 20 itens")
    void topPede20() {
        List<LivroMaisEmprestado> ranking = List.of(new LivroMaisEmprestado(1L, "Clean Code", "Robert C. Martin", 3L));
        when(repository.maisEmprestados(20)).thenReturn(ranking);

        List<LivroMaisEmprestado> resultado = new ListarLivrosMaisEmprestadosUseCase(repository).executar();

        assertThat(resultado).isEqualTo(ranking);
        verify(repository).maisEmprestados(20);
    }

    @Test
    @DisplayName("empréstimos ativos marcam como atrasado só quando a previsão já passou")
    void ativosMarcamAtraso() {
        when(repository.listarAtivos()).thenReturn(List.of(
                emAberto(1L, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 15)),
                emAberto(2L, LocalDate.of(2026, 11, 11), LocalDate.of(2026, 11, 25)),
                emAberto(3L, LocalDate.of(2026, 11, 25), LocalDate.of(2026, 12, 9))
        ));

        List<EmprestimoAtivoResponse> resultado = new ListarEmprestimosAtivosUseCase(repository, clock).executar();

        assertThat(resultado).extracting(EmprestimoAtivoResponse::atrasado)
                .containsExactly(true, false, false);
        assertThat(resultado.get(0).nomeUsuario()).isEqualTo("Maria Silva");
        assertThat(resultado.get(0).dataPrevistaDevolucao()).isEqualTo(LocalDate.of(2026, 11, 15));
    }
}
