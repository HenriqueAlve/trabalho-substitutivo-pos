package br.com.biblioteca.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Livro do acervo")
public record LivroResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Clean Code") String titulo,
        @Schema(example = "Robert C. Martin") String autor,
        @Schema(example = "9780132350884") String isbn,
        @Schema(description = "false enquanto o livro estiver emprestado", example = "true") boolean disponivel
) {
}
