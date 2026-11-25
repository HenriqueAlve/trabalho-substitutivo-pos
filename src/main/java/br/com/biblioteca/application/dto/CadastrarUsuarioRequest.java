package br.com.biblioteca.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para cadastrar ou atualizar um usuário (aluno ou membro)")
public record CadastrarUsuarioRequest(

        @Schema(description = "Nome completo", example = "Maria Silva")
        @NotBlank
        String nome,
        @Schema(description = "E-mail, único no sistema", example = "maria@email.com")
        @NotBlank
        @Email
        String email
) {
}
