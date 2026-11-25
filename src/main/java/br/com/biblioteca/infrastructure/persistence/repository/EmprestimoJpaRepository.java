package br.com.biblioteca.infrastructure.persistence.repository;

import br.com.biblioteca.domain.repository.LivroMaisEmprestado;
import br.com.biblioteca.infrastructure.persistence.entity.EmprestimoJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmprestimoJpaRepository extends JpaRepository<EmprestimoJpaEntity, Long> {

    List<EmprestimoJpaEntity> findByUsuarioId(Long usuarioId);

    @Query("""
    select new br.com.biblioteca.domain.repository.LivroMaisEmprestado(l.id, l.titulo, l.autor, count(e))
    from EmprestimoJpaEntity e join e.livro l
    group by l.id, l.titulo, l.autor
    order by count(e) desc, l.titulo
    """)
    List<LivroMaisEmprestado> maisEmprestados(Pageable pageable);

    @Query("""
    select e from EmprestimoJpaEntity e
    join fetch e.livro join fetch e.usuario
    where e.dataDevolucao is null
    order by e.dataPrevistaDevolucao
    """)
    List<EmprestimoJpaEntity> findAtivos();
}
