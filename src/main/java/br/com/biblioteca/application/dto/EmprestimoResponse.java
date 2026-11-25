package br.com.biblioteca.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Empréstimo de um livro. Datas no formato ISO-8601 (yyyy-MM-dd), calculadas em UTC.")
public record EmprestimoResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long livroId,
        @Schema(example = "Clean Code") String titulo,
        @Schema(example = "1") Long usuarioId,
        @Schema(description = "Data em que o livro foi emprestado", example = "2026-11-24") LocalDate dataEmprestimo,
        @Schema(description = "Data limite de devolução (empréstimo + 14 dias)", example = "2026-12-08") LocalDate dataPrevistaDevolucao,
        @Schema(description = "Data em que foi devolvido; nula enquanto o livro estiver emprestado", example = "2026-12-01", nullable = true) LocalDate dataDevolucao
) {
}
