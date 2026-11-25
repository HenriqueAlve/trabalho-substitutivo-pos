package br.com.biblioteca.domain.repository;

import br.com.biblioteca.domain.entity.Emprestimo;

import java.util.List;
import java.util.Optional;

public interface EmprestimoRepository {

    Emprestimo salvar(Emprestimo emprestimo);
    Optional<Emprestimo> buscarPorId(Long id);
    void excluir(Long id);
    List<Emprestimo> listarPorUsuario(Long usuarioId);
    List<LivroMaisEmprestado> maisEmprestados(int limite);
    List<Emprestimo> listarAtivos();

}
