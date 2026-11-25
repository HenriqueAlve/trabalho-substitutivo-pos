package br.com.biblioteca.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Dados para cadastrar ou atualizar um livro")
public record CadastrarLivroRequest(
        @Schema(description = "Título do livro", example = "Clean Code")
        @NotBlank
        String titulo,
        @Schema(description = "Autor do livro", example = "Robert C. Martin")
        @NotBlank
        String autor,
        @Schema(description = "ISBN-13, somente dígitos (sem hífens). Deve ser único.", example = "9780132350884")
        @NotBlank
        @Pattern(regexp = "\\d{13}", message = "ISBN deve ter 13 dígitos numéricos")
        String isbn
) {
}
