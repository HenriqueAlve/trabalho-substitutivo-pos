package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.CadastrarLivroRequest;
import br.com.biblioteca.application.dto.LivroResponse;
import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.repository.LivroRepository;

public class CadastrarLivroUseCase {

    private final LivroRepository livroRepository;

    public CadastrarLivroUseCase(LivroRepository livroRepository){
        this.livroRepository = livroRepository;
    }

    public LivroResponse executar(CadastrarLivroRequest request){
        Livro livro = new Livro(null, request.titulo(), request.autor(), request.isbn(), true);
        Livro salvo = livroRepository.salvar(livro);
        return new LivroResponse(salvo.getId(), salvo.getTitulo(), salvo.getAutor(), salvo.getIsbn() , salvo.isDisponivel());
    }
}
