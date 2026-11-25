package br.com.biblioteca.domain.entity;

import br.com.biblioteca.domain.enums.StatusReserva;
import br.com.biblioteca.domain.exception.RegraDeNegocioException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Reserva {

    private Long id;
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataReserva;
    private StatusReserva status;

    public static Reserva nova(Livro livro, Usuario usuario, LocalDate hoje) {
        if (livro.isDisponivel())
            throw new RegraDeNegocioException("Livro disponível: faça o empréstimo direto");
        return new Reserva(null, livro, usuario, hoje, StatusReserva.ATIVA);
    }

    public void cancelar() {
        if (status != StatusReserva.ATIVA)
            throw new RegraDeNegocioException("Só é possível cancelar reserva ativa");
        this.status = StatusReserva.CANCELADA;
    }
}
