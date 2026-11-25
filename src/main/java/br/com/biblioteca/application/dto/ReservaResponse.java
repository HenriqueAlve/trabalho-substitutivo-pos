package br.com.biblioteca.application.dto;

import br.com.biblioteca.domain.enums.StatusReserva;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Reserva de um livro")
public record ReservaResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long livroId,
        @Schema(example = "Clean Code") String titulo,
        @Schema(example = "2") Long usuarioId,
        @Schema(description = "Data da reserva (ISO-8601, UTC)", example = "2026-11-24") LocalDate dataReserva,
        @Schema(description = "ATIVA, ATENDIDA ou CANCELADA", example = "ATIVA") StatusReserva status
) {
}
