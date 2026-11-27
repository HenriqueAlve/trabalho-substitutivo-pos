package br.com.biblioteca.application.dto;

import br.com.biblioteca.domain.enums.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastrar um usuário (aluno, membro ou bibliotecário)")
public record CadastrarUsuarioRequest(

        @Schema(description = "Nome completo", example = "Maria Silva")
        @NotBlank
        String nome,
        @Schema(description = "E-mail, único no sistema. É o login do usuário.", example = "maria@email.com")
        @NotBlank
        @Email
        String email,
        @Schema(description = "Senha de acesso, de 6 a 72 caracteres. Nunca é devolvida pela API.", example = "minhaSenha123",
                accessMode = Schema.AccessMode.WRITE_ONLY)
        @NotBlank
        @Size(min = 6, max = 72)
        String senha,
        @Schema(description = "Perfil de acesso. Se omitido, o usuário é criado como USUARIO.", example = "USUARIO",
                nullable = true)
        Perfil perfil
) {
}
