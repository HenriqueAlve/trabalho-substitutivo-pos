package br.com.biblioteca.application.usecase;

import br.com.biblioteca.domain.repository.EmprestimoRepository;
import br.com.biblioteca.domain.repository.LivroMaisEmprestado;

import java.util.List;

public class ListarLivrosMaisEmprestadosUseCase {

    private final EmprestimoRepository emprestimoRepository;
    public ListarLivrosMaisEmprestadosUseCase(EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository; }

    public List<LivroMaisEmprestado> executar() {
        return emprestimoRepository.maisEmprestados(20);
    }
}
