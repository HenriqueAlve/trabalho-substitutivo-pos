package br.com.biblioteca.domain.repository;

public record LivroMaisEmprestado(
        Long livroId,
        String titulo,
        String autor,
        Long quantidade
) {
}
