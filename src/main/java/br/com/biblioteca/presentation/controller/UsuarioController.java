package br.com.biblioteca.presentation.controller;

import br.com.biblioteca.application.dto.CadastrarUsuarioRequest;
import br.com.biblioteca.application.dto.EmprestimoResponse;
import br.com.biblioteca.application.dto.ReservaResponse;
import br.com.biblioteca.application.dto.UsuarioResponse;
import br.com.biblioteca.application.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "CRUD de usuários (alunos ou membros) e seu histórico de empréstimos e reservas")
public class UsuarioController {

    private final CadastrarUsuarioUseCase cadastrar;
    private final BuscarUsuarioPorIdUseCase buscar;
    private final AtualizarUsuarioUseCase atualizar;
    private final ExcluirUsuarioUseCase excluir;
    private final ListarEmprestimosDoUsuarioUseCase listarEmprestimos;
    private final ListarReservasDoUsuarioUseCase listarReservas;

    @Operation(summary = "Cadastrar usuário", description = "O e-mail deve ser único.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (nome em branco ou e-mail mal formado)"),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody @Valid CadastrarUsuarioRequest req) {
        UsuarioResponse response = cadastrar.executar(req);
        return ResponseEntity.created(URI.create("/usuarios/" + response.id())).body(response);
    }

    @Operation(summary = "Buscar usuário por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public UsuarioResponse buscar(@Parameter(description = "Id do usuário", example = "1") @PathVariable Long id) {
        return buscar.executar(id);
    }

    @Operation(summary = "Atualizar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "E-mail já usado por outro usuário")
    })
    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@Parameter(description = "Id do usuário", example = "1") @PathVariable Long id,
                                     @RequestBody @Valid CadastrarUsuarioRequest req) {
        return atualizar.executar(id, req);
    }

    @Operation(summary = "Excluir usuário",
            description = "Não é possível excluir um usuário que já tenha empréstimos ou reservas vinculados.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário excluído"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Usuário em uso (possui empréstimos ou reservas)")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@Parameter(description = "Id do usuário", example = "1") @PathVariable Long id) {
        excluir.executar(id);
    }

    @Operation(summary = "Histórico de empréstimos do usuário",
            description = "Lista todos os empréstimos do usuário, devolvidos ou não.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de empréstimos (pode ser vazia)"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}/emprestimos")
    public List<EmprestimoResponse> historico(@Parameter(description = "Id do usuário", example = "1") @PathVariable Long id) {
        return listarEmprestimos.executar(id);
    }

    @Operation(summary = "Reservas do usuário",
            description = "Lista as reservas do usuário em qualquer status (ATIVA, ATENDIDA, CANCELADA).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reservas (pode ser vazia)"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}/reservas")
    public List<ReservaResponse> reservas(@Parameter(description = "Id do usuário", example = "1") @PathVariable Long id) {
        return listarReservas.executar(id);
    }
}
