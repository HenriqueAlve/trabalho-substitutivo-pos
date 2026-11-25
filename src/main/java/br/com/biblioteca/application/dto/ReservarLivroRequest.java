package br.com.biblioteca.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para reservar um livro que está emprestado")
public record ReservarLivroRequest(
        @Schema(description = "Id do livro a reservar", example = "1")
        @NotNull Long livroId,
        @Schema(description = "Id do usuário que está reservando", example = "2")
        @NotNull Long usuarioId
) {
}
