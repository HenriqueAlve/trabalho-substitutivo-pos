package br.com.biblioteca.domain.exception;

public class LivroIndisponivelException extends RuntimeException{

    String mensagem;

    public LivroIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
