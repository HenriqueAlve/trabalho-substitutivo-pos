package br.com.biblioteca.infrastructure.persistence.repository;

import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.entity.Reserva;
import br.com.biblioteca.domain.entity.Usuario;
import br.com.biblioteca.domain.enums.StatusReserva;
import br.com.biblioteca.domain.repository.ReservaRepository;

import br.com.biblioteca.infrastructure.persistence.entity.LivroJpaEntity;
import br.com.biblioteca.infrastructure.persistence.entity.ReservaJpaEntity;
import br.com.biblioteca.infrastructure.persistence.entity.UsuarioJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservaRepositoryImpl implements ReservaRepository {

    private final ReservaJpaRepository jpa;

    @Override
    @Transactional
    public Reserva salvar(Reserva reserva) {
        return paraDominio(jpa.save(paraEntity(reserva)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reserva> buscarPorId(Long id) {
        return jpa.findById(id).map(this::paraDominio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reserva> listarPorUsuario(Long usuarioId) {
        return jpa.findByUsuarioId(usuarioId).stream().map(this::paraDominio).toList();
    }

    @Override
    public boolean existeAtiva(Long livroId, Long usuarioId) {
        return jpa.existsByLivroIdAndUsuarioIdAndStatus(livroId, usuarioId, StatusReserva.ATIVA);
    }

    private ReservaJpaEntity paraEntity(Reserva r) {
        Livro l = r.getLivro();
        Usuario u = r.getUsuario();
        return new ReservaJpaEntity(
                r.getId(),
                new LivroJpaEntity(l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn(), l.isDisponivel()),
                new UsuarioJpaEntity(u.getId(), u.getNome(), u.getEmail()),
                r.getDataReserva(), r.getStatus());
    }

    private Reserva paraDominio(ReservaJpaEntity e) {
        LivroJpaEntity l = e.getLivro();
        UsuarioJpaEntity u = e.getUsuario();
        return new Reserva(
                e.getId(),
                new Livro(l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn(), l.isDisponivel()),
                new Usuario(u.getId(), u.getNome(), u.getEmail()),
                e.getDataReserva(), e.getStatus());
    }
}
