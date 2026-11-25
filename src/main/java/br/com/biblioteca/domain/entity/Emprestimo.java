package br.com.biblioteca.domain.entity;

import br.com.biblioteca.domain.exception.EmprestimoJaDevolvidoException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Emprestimo {

    private Long id;
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;

    public static Emprestimo novo(Livro livro, Usuario usuario, LocalDate hoje) {
        livro.emprestar();
        return new Emprestimo(null, livro, usuario, hoje, hoje.plusDays(14), null);
    }

    public void devolver(LocalDate hoje) {
        if (dataDevolucao != null) throw new EmprestimoJaDevolvidoException("Empréstimo já devolvido: " + id);
        this.dataDevolucao = hoje;
        livro.devolver();
    }

}
