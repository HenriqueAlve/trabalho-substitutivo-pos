package br.com.biblioteca.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para registrar um empréstimo")
public record EmprestarLivroRequest(
        @Schema(description = "Id do livro a emprestar", example = "1")
        @NotNull
        Long livroId,
        @Schema(description = "Id do usuário que está pegando o livro", example = "1")
        @NotNull
        Long usuarioId
) {
}
