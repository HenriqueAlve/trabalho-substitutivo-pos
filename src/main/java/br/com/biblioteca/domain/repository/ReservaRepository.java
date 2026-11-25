package br.com.biblioteca.domain.repository;

import br.com.biblioteca.domain.entity.Reserva;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository {

    Reserva salvar(Reserva reserva);
    Optional<Reserva> buscarPorId(Long id);
    List<Reserva> listarPorUsuario(Long usuarioId);
    boolean existeAtiva(Long livroId, Long usuarioId);
}
