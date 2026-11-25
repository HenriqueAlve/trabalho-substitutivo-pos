package br.com.biblioteca.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Empréstimo em aberto, com a previsão de devolução")
public record EmprestimoAtivoResponse(
        @Schema(example = "3") Long emprestimoId,
        @Schema(example = "1") Long livroId,
        @Schema(example = "Clean Code") String titulo,
        @Schema(example = "2") Long usuarioId,
        @Schema(example = "João Souza") String nomeUsuario,
        @Schema(example = "2026-11-24") LocalDate dataEmprestimo,
        @Schema(example = "2026-12-08") LocalDate dataPrevistaDevolucao,
        @Schema(description = "true se a data prevista já passou e o livro não foi devolvido", example = "false") boolean atrasado
) {
}
