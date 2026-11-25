package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.EmprestimoResponse;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.EmprestimoRepository;
import br.com.biblioteca.domain.repository.UsuarioRepository;

import java.util.List;

public class ListarEmprestimosDoUsuarioUseCase {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;

    public ListarEmprestimosDoUsuarioUseCase(EmprestimoRepository emprestimoRepository,
                                             UsuarioRepository usuarioRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<EmprestimoResponse> executar(Long usuarioId) {
        usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + usuarioId));

        return emprestimoRepository.listarPorUsuario(usuarioId).stream()
                .map(e -> new EmprestimoResponse(
                        e.getId(), e.getLivro().getId(), e.getLivro().getTitulo(),
                        e.getUsuario().getId(), e.getDataEmprestimo(),
                        e.getDataPrevistaDevolucao(), e.getDataDevolucao()))
                .toList();
    }
}
