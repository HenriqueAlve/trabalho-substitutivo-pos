package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.ReservaResponse;
import br.com.biblioteca.domain.entity.Reserva;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.ReservaRepository;

public class CancelarReservaUseCase {

    private final ReservaRepository reservaRepository;

    public CancelarReservaUseCase(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public ReservaResponse executar(Long id) {
        Reserva reserva = reservaRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva não encontrada: " + id));
        reserva.cancelar();
        Reserva salva = reservaRepository.salvar(reserva);
        return new ReservaResponse(salva.getId(), salva.getLivro().getId(), salva.getLivro().getTitulo(),
                salva.getUsuario().getId(), salva.getDataReserva(), salva.getStatus());
    }
}
