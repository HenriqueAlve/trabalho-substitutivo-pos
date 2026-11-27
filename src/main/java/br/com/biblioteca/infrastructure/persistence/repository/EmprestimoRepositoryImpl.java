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

    private EmprestimoJpaEntity paraEntity(Emprestimo emprestimo) {
        return new EmprestimoJpaEntity(
                emprestimo.getId(),
                new LivroJpaEntity(emprestimo.getLivro().getId(), emprestimo.getLivro().getTitulo(), emprestimo.getLivro().getAutor(),
                        emprestimo.getLivro().getIsbn(), emprestimo.getLivro().isDisponivel()),
                new UsuarioJpaEntity(emprestimo.getUsuario().getId(), emprestimo.getUsuario().getNome(), emprestimo.getUsuario().getEmail(), emprestimo.getUsuario().getSenha(), emprestimo.getUsuario().getPerfil()),
                emprestimo.getDataEmprestimo(), emprestimo.getDataPrevistaDevolucao(), emprestimo.getDataDevolucao());
    }

    private Emprestimo paraDominio(EmprestimoJpaEntity emprestimo) {
        LivroJpaEntity livro = emprestimo.getLivro();
        UsuarioJpaEntity usuario = emprestimo.getUsuario();
        return new Emprestimo(
                emprestimo.getId(),
                new Livro(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.isDisponivel()),
                new Usuario(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfil()),
                emprestimo.getDataEmprestimo(), emprestimo.getDataPrevistaDevolucao(), emprestimo.getDataDevolucao());
    }
}
