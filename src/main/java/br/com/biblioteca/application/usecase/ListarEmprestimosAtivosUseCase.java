package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.EmprestimoAtivoResponse;
import br.com.biblioteca.domain.repository.EmprestimoRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

public class ListarEmprestimosAtivosUseCase {

    private final EmprestimoRepository repository;
    private final Clock clock;

    public ListarEmprestimosAtivosUseCase(EmprestimoRepository repository, Clock clock){
        this.repository = repository;
        this.clock = clock;
    }

    public List<EmprestimoAtivoResponse> executar() {
        LocalDate hoje = LocalDate.now(clock);
        return repository.listarAtivos().stream()
                .map(e -> new EmprestimoAtivoResponse(
                        e.getId(), e.getLivro().getId(), e.getLivro().getTitulo(),
                        e.getUsuario().getId(), e.getUsuario().getNome(),
                        e.getDataEmprestimo(), e.getDataPrevistaDevolucao(),
                        e.getDataPrevistaDevolucao().isBefore(hoje)))
                .toList();
    }
}
