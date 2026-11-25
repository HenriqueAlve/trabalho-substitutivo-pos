package br.com.biblioteca.infrastructure.persistence.repository;

import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.repository.LivroRepository;
import br.com.biblioteca.domain.repository.Pagina;
import br.com.biblioteca.infrastructure.persistence.entity.LivroJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LivroRepositoryImpl implements LivroRepository {


    private final LivroJpaRepository jpa;

    @Override
    public Livro salvar(Livro livro) {
        return paraDominio(jpa.save(paraEntity(livro)));
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        return jpa.findById(id).map(this::paraDominio);
    }

    @Override
    public void excluir(Long id) {
        jpa.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagina<Livro> buscar(String titulo, String autor, String isbn, Boolean disponivel,
                                int pagina, int tamanho) {
        Specification<LivroJpaEntity> spec = (root, q, cb) -> cb.conjunction();

        if (titulo != null && !titulo.isBlank())
            spec = spec.and((r, q, cb) -> cb.like(cb.lower(r.get("titulo")), "%" + titulo.toLowerCase() + "%"));
        if (autor != null && !autor.isBlank())
            spec = spec.and((r, q, cb) -> cb.like(cb.lower(r.get("autor")), "%" + autor.toLowerCase() + "%"));
        if (isbn != null && !isbn.isBlank())
            spec = spec.and((r, q, cb) -> cb.equal(r.get("isbn"), isbn.replace("-", "")));
        if (disponivel != null)
            spec = spec.and((r, q, cb) -> cb.equal(r.get("disponivel"), disponivel));

        Page<LivroJpaEntity> p = jpa.findAll(spec, PageRequest.of(pagina, tamanho, Sort.by("titulo")));
        return new Pagina<>(p.getContent().stream().map(this::paraDominio).toList(),
                p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages());
    }

    private LivroJpaEntity paraEntity(Livro l) {
        return new LivroJpaEntity(l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn(), l.isDisponivel());
    }

    private Livro paraDominio(LivroJpaEntity e) {
        return new Livro(e.getId(), e.getTitulo(), e.getAutor(), e.getIsbn(), e.isDisponivel());
    }
}
