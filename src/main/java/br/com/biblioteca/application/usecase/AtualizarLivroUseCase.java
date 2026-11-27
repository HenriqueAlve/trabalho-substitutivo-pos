package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.CadastrarLivroRequest;
import br.com.biblioteca.application.dto.LivroResponse;
import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.LivroRepository;

public class AtualizarLivroUseCase {

    private final LivroRepository livroRepository;
    public AtualizarLivroUseCase(LivroRepository livroRepository) { this.livroRepository = livroRepository; }

    public LivroResponse executar(Long id, CadastrarLivroRequest req) {
        Livro livro = livroRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: " + id));
        livro.atualizar(req.titulo(), req.autor(), req.isbn());
        Livro salvo = livroRepository.salvar(livro);
        return new LivroResponse(salvo.getId(), salvo.getTitulo(), salvo.getAutor(), salvo.getIsbn(), salvo.isDisponivel());
    }
}
