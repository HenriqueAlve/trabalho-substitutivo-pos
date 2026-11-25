package br.com.biblioteca.infrastructure.persistence.repository;

import br.com.biblioteca.domain.enums.StatusReserva;
import br.com.biblioteca.infrastructure.persistence.entity.ReservaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaJpaRepository extends JpaRepository<ReservaJpaEntity, Long> {

    List<ReservaJpaEntity> findByUsuarioId(Long usuarioId);
    boolean existsByLivroIdAndUsuarioIdAndStatus(Long livroId, Long usuarioId, StatusReserva status);
}
