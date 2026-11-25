package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.ReservaResponse;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.ReservaRepository;
import br.com.biblioteca.domain.repository.UsuarioRepository;

import java.util.List;

public class ListarReservasDoUsuarioUseCase {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;

    public ListarReservasDoUsuarioUseCase(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ReservaResponse> executar(Long usuarioId) {
        usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + usuarioId));
        return reservaRepository.listarPorUsuario(usuarioId).stream()
                .map(r -> new ReservaResponse(r.getId(), r.getLivro().getId(), r.getLivro().getTitulo(),
                        r.getUsuario().getId(), r.getDataReserva(), r.getStatus()))
                .toList();
    }
}
