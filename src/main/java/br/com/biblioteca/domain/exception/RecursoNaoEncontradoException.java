package br.com.biblioteca.domain.exception;

public class RecursoNaoEncontradoException extends RuntimeException {

    String mensagem;

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

}
