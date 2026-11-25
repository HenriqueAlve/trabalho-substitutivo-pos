package br.com.biblioteca.presentation.controller;

import br.com.biblioteca.application.dto.CadastrarLivroRequest;
import br.com.biblioteca.application.dto.LivroResponse;
import br.com.biblioteca.application.usecase.*;
import br.com.biblioteca.domain.repository.Pagina;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "CRUD e consulta de livros do acervo")
public class LivroController {

    private final CadastrarLivroUseCase cadastrar;
    private final BuscarLivroPorIdUseCase buscar;
    private final AtualizarLivroUseCase atualizar;
    private final ExcluirLivroUseCase excluir;
    private final BuscarLivrosUseCase buscarLivros;

    @Operation(summary = "Cadastrar livro",
            description = "Cria um livro disponível. O ISBN (13 dígitos, sem hífens) deve ser único.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (campo em branco ou ISBN fora do formato)"),
            @ApiResponse(responseCode = "409", description = "ISBN já cadastrado")
    })
    @PostMapping
    public ResponseEntity<LivroResponse> cadastrar(@RequestBody @Valid CadastrarLivroRequest req) {
        LivroResponse response = cadastrar.executar(req);
        return ResponseEntity.created(URI.create("/livros/" + response.id())).body(response);
    }

    @Operation(summary = "Buscar livro por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @GetMapping("/{id}")
    public LivroResponse buscar(@Parameter(description = "Id do livro", example = "1") @PathVariable Long id) {
        return buscar.executar(id);
    }

    @Operation(summary = "Atualizar livro",
            description = "Altera título, autor e ISBN. A disponibilidade não é alterada aqui: ela muda ao emprestar e devolver.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "409", description = "ISBN já usado por outro livro")
    })
    @PutMapping("/{id}")
    public LivroResponse atualizar(@Parameter(description = "Id do livro", example = "1") @PathVariable Long id,
                                   @RequestBody @Valid CadastrarLivroRequest req) {
        return atualizar.executar(id, req);
    }

    @Operation(summary = "Excluir livro",
            description = "Não é possível excluir um livro que já tenha empréstimos ou reservas vinculados.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livro excluído"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "409", description = "Livro em uso (possui empréstimos ou reservas)")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@Parameter(description = "Id do livro", example = "1") @PathVariable Long id) {
        excluir.executar(id);
    }

    @Operation(summary = "Consultar livros",
            description = "Busca paginada com filtros opcionais e combináveis. Título e autor usam busca parcial sem "
                    + "diferenciar maiúsculas; o ISBN é comparado exatamente. Resultado ordenado por título; "
                    + "o tamanho máximo da página é 100.")
    @ApiResponse(responseCode = "200", description = "Página de livros (conteudo, pagina, tamanho, totalElementos, totalPaginas)")
    @GetMapping
    public Pagina<LivroResponse> buscar(
            @Parameter(description = "Parte do título", example = "clean")
            @RequestParam(required = false) String titulo,
            @Parameter(description = "Parte do nome do autor", example = "tolkien")
            @RequestParam(required = false) String autor,
            @Parameter(description = "ISBN exato (13 dígitos)", example = "9780441172719")
            @RequestParam(required = false) String isbn,
            @Parameter(description = "true = só disponíveis; false = só emprestados; omitido = todos", example = "true")
            @RequestParam(required = false) Boolean disponivel,
            @Parameter(description = "Número da página, a partir de 0", example = "0")
            @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Itens por página (máximo 100)", example = "10")
            @RequestParam(defaultValue = "10") int tamanho) {
        return buscarLivros.executar(titulo, autor, isbn, disponivel, pagina, tamanho);
    }
}
