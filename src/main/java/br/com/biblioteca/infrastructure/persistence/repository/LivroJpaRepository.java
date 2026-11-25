package br.com.biblioteca.infrastructure.persistence.repository;

import br.com.biblioteca.infrastructure.persistence.entity.LivroJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LivroJpaRepository extends JpaRepository<LivroJpaEntity, Long>, JpaSpecificationExecutor<LivroJpaEntity> {
}
