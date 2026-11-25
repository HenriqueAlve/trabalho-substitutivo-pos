package br.com.biblioteca.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Usuário da biblioteca")
public record UsuarioResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Maria Silva") String nome,
        @Schema(example = "maria@email.com") String email
) {
}
