package br.com.biblioteca.application.usecase;


import br.com.biblioteca.application.dto.EmprestimoResponse;
import br.com.biblioteca.domain.entity.Emprestimo;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.EmprestimoRepository;
import br.com.biblioteca.domain.repository.LivroRepository;

import java.time.Clock;
import java.time.LocalDate;

public class DevolverLivroUseCase {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final Clock clock;

    public DevolverLivroUseCase(EmprestimoRepository emprestimoRepository,
                                LivroRepository livroRepository, Clock clock) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.clock = clock;
    }

    public EmprestimoResponse executar(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(emprestimoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado: " + emprestimoId));

        emprestimo.devolver(LocalDate.now(clock));

        livroRepository.salvar(emprestimo.getLivro());
        Emprestimo salvo = emprestimoRepository.salvar(emprestimo);

        return new EmprestimoResponse(
                salvo.getId(), salvo.getLivro().getId(), salvo.getLivro().getTitulo(),
                salvo.getUsuario().getId(), salvo.getDataEmprestimo(),
                salvo.getDataPrevistaDevolucao(), salvo.getDataDevolucao());
    }
}
