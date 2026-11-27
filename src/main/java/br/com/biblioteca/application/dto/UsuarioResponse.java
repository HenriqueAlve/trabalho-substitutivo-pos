package br.com.biblioteca.application.dto;

import br.com.biblioteca.domain.enums.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Usuário da biblioteca")
public record UsuarioResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Maria Silva") String nome,
        @Schema(example = "maria@email.com") String email,
        @Schema(description = "BIBLIOTECARIO ou USUARIO", example = "USUARIO") Perfil perfil
) {
}
