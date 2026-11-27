package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.LivroResponse;
import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.LivroRepository;

public class BuscarLivroPorIdUseCase {

    private final LivroRepository livroRepository;

    public BuscarLivroPorIdUseCase(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public LivroResponse executar(Long id) {
        Livro livro = livroRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: " + id));
        return new LivroResponse(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.isDisponivel());
    }
}
