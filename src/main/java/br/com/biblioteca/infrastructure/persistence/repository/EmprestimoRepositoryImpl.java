package br.com.biblioteca.infrastructure.persistence.repository;

import br.com.biblioteca.domain.entity.Emprestimo;
import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.entity.Usuario;
import br.com.biblioteca.domain.repository.EmprestimoRepository;
import br.com.biblioteca.domain.repository.LivroMaisEmprestado;
import br.com.biblioteca.infrastructure.persistence.entity.EmprestimoJpaEntity;
import br.com.biblioteca.infrastructure.persistence.entity.LivroJpaEntity;
import br.com.biblioteca.infrastructure.persistence.entity.UsuarioJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class EmprestimoRepositoryImpl implements EmprestimoRepository {


    private final EmprestimoJpaRepository jpa;

    @Override
    public Emprestimo salvar(Emprestimo emprestimo) {
        return paraDominio(jpa.save(paraEntity(emprestimo)));
    }

    @Override
    public Optional<Emprestimo> buscarPorId(Long id) {
        return jpa.findById(id).map(this::paraDominio);
    }

    @Override
    public void excluir(Long id) {
        jpa.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Emprestimo> listarPorUsuario(Long usuarioId) {
        return jpa.findByUsuarioId(usuarioId).stream().map(this::paraDominio).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivroMaisEmprestado> maisEmprestados(int limite) {
        return jpa.maisEmprestados(PageRequest.of(0, limite));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Emprestimo> listarAtivos() {
        return jpa.findAtivos().stream().map(this::paraDominio).toList();
    }

    private EmprestimoJpaEntity paraEntity(Emprestimo e) {
        return new EmprestimoJpaEntity(
                e.getId(),
                new LivroJpaEntity(e.getLivro().getId(), e.getLivro().getTitulo(), e.getLivro().getAutor(),
                        e.getLivro().getIsbn(), e.getLivro().isDisponivel()),
                new UsuarioJpaEntity(e.getUsuario().getId(), e.getUsuario().getNome(), e.getUsuario().getEmail()),
                e.getDataEmprestimo(), e.getDataPrevistaDevolucao(), e.getDataDevolucao());
    }

    private Emprestimo paraDominio(EmprestimoJpaEntity e) {
        LivroJpaEntity l = e.getLivro();
        UsuarioJpaEntity u = e.getUsuario();
        return new Emprestimo(
                e.getId(),
                new Livro(l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn(), l.isDisponivel()),
                new Usuario(u.getId(), u.getNome(), u.getEmail()),
                e.getDataEmprestimo(), e.getDataPrevistaDevolucao(), e.getDataDevolucao());
    }
}
