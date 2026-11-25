package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.ReservaResponse;
import br.com.biblioteca.application.dto.ReservarLivroRequest;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.entity.Reserva;
import br.com.biblioteca.domain.entity.Usuario;
import br.com.biblioteca.domain.exception.RegraDeNegocioException;
import br.com.biblioteca.domain.repository.LivroRepository;
import br.com.biblioteca.domain.repository.ReservaRepository;
import br.com.biblioteca.domain.repository.UsuarioRepository;

import java.time.Clock;
import java.time.LocalDate;

public class ReservarLivroUseCase {

    private final ReservaRepository reservaRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;
    private final Clock clock;

    public ReservarLivroUseCase(
            ReservaRepository reservaRepository,
            LivroRepository livroRepository,
            UsuarioRepository usuarioRepository,
            Clock clock
    ){
        this.reservaRepository = reservaRepository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
        this.clock = clock;

    }

    public ReservaResponse executar(ReservarLivroRequest request){
        Livro livro = livroRepository.buscarPorId(request.livroId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: " + request.livroId()));

        Usuario usuario = usuarioRepository.buscarPorId(request.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + request.usuarioId()));

        if (reservaRepository.existeAtiva(livro.getId(), usuario.getId()))
            throw new RegraDeNegocioException("Usuário já tem reserva ativa deste livro");

        Reserva salva = reservaRepository.salvar(Reserva.nova(livro, usuario, LocalDate.now(clock)));

        return new ReservaResponse(
                salva.getId(), salva.getLivro().getId(), salva.getLivro().getTitulo(),
                salva.getUsuario().getId(), salva.getDataReserva(), salva.getStatus());
    }
}
