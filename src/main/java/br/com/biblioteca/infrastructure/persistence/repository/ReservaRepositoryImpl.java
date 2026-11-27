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

    private ReservaJpaEntity paraEntity(Reserva reserva) {
        Livro livro = reserva.getLivro();
        Usuario usuario = reserva.getUsuario();
        return new ReservaJpaEntity(
                reserva.getId(),
                new LivroJpaEntity(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.isDisponivel()),
                new UsuarioJpaEntity(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfil()),
                reserva.getDataReserva(), reserva.getStatus());
    }

    private Reserva paraDominio(ReservaJpaEntity reserva) {
        LivroJpaEntity livro = reserva.getLivro();
        UsuarioJpaEntity usuario = reserva.getUsuario();
        return new Reserva(
                reserva.getId(),
                new Livro(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.isDisponivel()),
                new Usuario(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfil()),
                reserva.getDataReserva(), reserva.getStatus());
    }
}
