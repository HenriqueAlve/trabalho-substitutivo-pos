package br.com.biblioteca.presentation.controller;


import br.com.biblioteca.application.dto.ReservaResponse;
import br.com.biblioteca.application.dto.ReservarLivroRequest;
import br.com.biblioteca.application.usecase.CancelarReservaUseCase;
import br.com.biblioteca.application.usecase.ReservarLivroUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Reserva de livros que estão emprestados")
public class ReservaController {

    private final ReservarLivroUseCase reservar;
    private final CancelarReservaUseCase cancelar;

    @Operation(summary = "Reservar livro",
            description = "Só é possível reservar um livro indisponível (emprestado). Se estiver disponível, faça o empréstimo direto. "
                    + "Um usuário não pode ter duas reservas ativas do mesmo livro.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva criada com status ATIVA"),
            @ApiResponse(responseCode = "400", description = "livroId ou usuarioId ausente"),
            @ApiResponse(responseCode = "404", description = "Livro ou usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Livro disponível, ou usuário já tem reserva ativa deste livro")
    })
    @PostMapping
    public ResponseEntity<ReservaResponse> reservar(@RequestBody @Valid ReservarLivroRequest req) {
        ReservaResponse r = reservar.executar(req);
        return ResponseEntity.created(URI.create("/reservas/" + r.id())).body(r);
    }

    @Operation(summary = "Cancelar reserva", description = "Só reservas ATIVAS podem ser canceladas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva cancelada (status CANCELADA)"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada"),
            @ApiResponse(responseCode = "409", description = "Reserva não está ativa")
    })
    @PutMapping("/{id}/cancelamento")
    public ReservaResponse cancelar(@Parameter(description = "Id da reserva", example = "1") @PathVariable Long id) {
        return cancelar.executar(id);
    }
}
