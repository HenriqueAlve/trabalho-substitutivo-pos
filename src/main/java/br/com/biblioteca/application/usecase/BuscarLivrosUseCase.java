package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.LivroResponse;
import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.repository.LivroRepository;
import br.com.biblioteca.domain.repository.Pagina;

public class BuscarLivrosUseCase {

    private final LivroRepository livroRepository;
    public BuscarLivrosUseCase(LivroRepository livroRepository) { this.livroRepository = livroRepository; }

    public Pagina<LivroResponse> executar(String titulo, String autor, String isbn,
                                          Boolean disponivel, int pagina, int tamanho) {
        int tam = Math.min(Math.max(tamanho, 1), 100);
        Pagina<Livro> p = livroRepository.buscar(titulo, autor, isbn, disponivel, Math.max(pagina, 0), tam);
        return new Pagina<>(
                p.conteudo().stream()
                        .map(l -> new LivroResponse(l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn(), l.isDisponivel()))
                        .toList(),
                p.pagina(), p.tamanho(), p.totalElementos(), p.totalPaginas());
    }
}
