package br.com.biblioteca.presentation.controller;

import br.com.biblioteca.application.dto.EmprestimoAtivoResponse;
import br.com.biblioteca.application.usecase.ListarEmprestimosAtivosUseCase;
import br.com.biblioteca.application.usecase.ListarLivrosMaisEmprestadosUseCase;
import br.com.biblioteca.domain.repository.LivroMaisEmprestado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "Relatórios de uso da biblioteca")
public class RelatorioController {

    private final ListarLivrosMaisEmprestadosUseCase maisEmprestados;
    private final ListarEmprestimosAtivosUseCase ativos;

    @Operation(summary = "20 livros mais emprestados",
            description = "Ranking por número de empréstimos já realizados (devolvidos ou não), do maior para o menor. "
                    + "Em caso de empate, ordena por título.")
    @ApiResponse(responseCode = "200", description = "Até 20 livros com a quantidade de empréstimos (pode ser vazia)")
    @GetMapping("/livros-mais-emprestados")
    public List<LivroMaisEmprestado> maisEmprestados() { return maisEmprestados.executar(); }

    @Operation(summary = "Livros emprestados no momento",
            description = "Empréstimos ainda não devolvidos, ordenados pela data prevista de devolução. "
                    + "O campo atrasado indica se a previsão já passou.")
    @ApiResponse(responseCode = "200", description = "Lista de empréstimos em aberto (pode ser vazia)")
    @GetMapping("/emprestimos-ativos")
    public List<EmprestimoAtivoResponse> ativos() { return ativos.executar(); }
}
