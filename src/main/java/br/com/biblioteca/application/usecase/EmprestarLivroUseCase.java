package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.EmprestarLivroRequest;
import br.com.biblioteca.application.dto.EmprestimoResponse;
import br.com.biblioteca.domain.entity.Emprestimo;
import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.entity.Usuario;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.EmprestimoRepository;
import br.com.biblioteca.domain.repository.LivroRepository;
import br.com.biblioteca.domain.repository.UsuarioRepository;

import java.time.Clock;
import java.time.LocalDate;

public class EmprestarLivroUseCase {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private final Clock clock;

    public EmprestarLivroUseCase(EmprestimoRepository emprestimoRepository,
                                 UsuarioRepository usuarioRepository,
                                 LivroRepository livroRepository,
                                 Clock clock) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
        this.clock = clock;
    }

    public EmprestimoResponse executar(EmprestarLivroRequest request) {
        Livro livro = livroRepository.buscarPorId(request.livroId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: " + request.livroId()));
        Usuario usuario = usuarioRepository.buscarPorId(request.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + request.usuarioId()));

        Emprestimo emprestimo = Emprestimo.novo(livro, usuario, LocalDate.now(clock));

        livroRepository.salvar(livro);
        Emprestimo salvo = emprestimoRepository.salvar(emprestimo);

        return new EmprestimoResponse(
                salvo.getId(),
                salvo.getLivro().getId(),
                salvo.getLivro().getTitulo(),
                salvo.getUsuario().getId(),
                salvo.getDataEmprestimo(),
                salvo.getDataPrevistaDevolucao(),
                salvo.getDataDevolucao());
    }


}
