package br.com.biblioteca.presentation.exception;

import br.com.biblioteca.domain.exception.EmprestimoJaDevolvidoException;
import br.com.biblioteca.domain.exception.LivroIndisponivelException;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.exception.RegraDeNegocioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(RecursoNaoEncontradoException.class)
    ResponseEntity<String> naoEncontrado(RecursoNaoEncontradoException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(LivroIndisponivelException.class)
    ResponseEntity<String> indisponivel(LivroIndisponivelException e) {
        return ResponseEntity.status(409).body(e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<String> violacaoDeIntegridade(DataIntegrityViolationException e) {
        String causa = String.valueOf(e.getMostSpecificCause().getMessage());
        String mensagem;
        if (causa.contains("livro_isbn_key")) mensagem = "ISBN já cadastrado";
        else if (causa.contains("usuario_email_key")) mensagem = "E-mail já cadastrado";
        else if (causa.contains("foreign key")) mensagem = "Registro em uso: possui empréstimos ou reservas vinculados";
        else mensagem = "Operação viola uma restrição de dados";
        return ResponseEntity.status(409).body(mensagem);
    }

    @ExceptionHandler({RegraDeNegocioException.class, EmprestimoJaDevolvidoException.class})
    ResponseEntity<String> regraDeNegocio(RuntimeException e) {
        return ResponseEntity.status(409).body(e.getMessage());
    }
}
