package br.com.biblioteca.application.usecase;

import br.com.biblioteca.domain.repository.EmprestimoRepository;
import br.com.biblioteca.domain.repository.LivroMaisEmprestado;

import java.util.List;

public class ListarLivrosMaisEmprestadosUseCase {

    private final EmprestimoRepository repo;
    public ListarLivrosMaisEmprestadosUseCase(EmprestimoRepository repo) { this.repo = repo; }

    public List<LivroMaisEmprestado> executar() {
        return repo.maisEmprestados(20);
    }
}
