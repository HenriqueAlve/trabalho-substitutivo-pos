package br.com.biblioteca.presentation.controller;

import br.com.biblioteca.application.dto.EmprestarLivroRequest;
import br.com.biblioteca.application.dto.EmprestimoResponse;
import br.com.biblioteca.application.usecase.DevolverLivroUseCase;
import br.com.biblioteca.application.usecase.EmprestarLivroUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
@Tag(name = "Empréstimos", description = "Registro de empréstimos e devoluções de livros")
public class EmprestimoController {


    private final EmprestarLivroUseCase emprestar;
    private final DevolverLivroUseCase devolver;

    @Operation(summary = "Emprestar livro",
            description = "Associa o livro ao usuário e calcula a data prevista de devolução (hoje + 14 dias, em UTC). "
                    + "O livro passa a ficar indisponível.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Empréstimo registrado"),
            @ApiResponse(responseCode = "400", description = "livroId ou usuarioId ausente"),
            @ApiResponse(responseCode = "404", description = "Livro ou usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Livro indisponível (já emprestado)")
    })
    @PostMapping
    @Transactional
    public ResponseEntity<EmprestimoResponse> emprestar(@RequestBody @Valid EmprestarLivroRequest req) {
        EmprestimoResponse r = emprestar.executar(req);
        return ResponseEntity.created(URI.create("/emprestimos/" + r.id())).body(r);
    }

    @Operation(summary = "Devolver livro",
            description = "Registra a data de devolução (hoje, em UTC) e torna o livro disponível novamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Devolução registrada"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado"),
            @ApiResponse(responseCode = "409", description = "Empréstimo já devolvido")
    })
    @PutMapping("/{id}/devolucao")
    @Transactional
    public EmprestimoResponse devolver(@Parameter(description = "Id do empréstimo", example = "1") @PathVariable Long id) {
        return devolver.executar(id);
    }
}
