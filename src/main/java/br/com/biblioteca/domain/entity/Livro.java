package br.com.biblioteca.domain.entity;


import br.com.biblioteca.domain.exception.LivroIndisponivelException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Livro {

    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private boolean disponivel;

    public void emprestar() {
        if (!disponivel) throw new LivroIndisponivelException("Livro indisponível (já emprestado): ISBN " + isbn);
        this.disponivel = false;
    }
    public void devolver() {
        this.disponivel = true; }

    public void atualizar(String titulo, String autor, String isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
    }
}
