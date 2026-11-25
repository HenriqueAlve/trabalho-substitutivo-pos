package br.com.biblioteca.domain.repository;

import br.com.biblioteca.domain.entity.Livro;

import java.util.Optional;

public interface LivroRepository {
    Livro salvar(Livro livro);
    Optional<Livro> buscarPorId(Long id);
    void excluir(Long id);
    Pagina<Livro> buscar(String titulo, String autor, String isbn, Boolean disponivel, int pagina, int tamanho);
}
