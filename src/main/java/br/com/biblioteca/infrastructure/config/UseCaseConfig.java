package br.com.biblioteca.infrastructure.config;

import br.com.biblioteca.application.usecase.*;
import br.com.biblioteca.domain.repository.EmprestimoRepository;
import br.com.biblioteca.domain.repository.LivroRepository;
import br.com.biblioteca.domain.repository.ReservaRepository;
import br.com.biblioteca.domain.repository.UsuarioRepository;
import br.com.biblioteca.application.port.CodificadorDeSenha;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class UseCaseConfig {

    @Bean
    CadastrarLivroUseCase cadastrarLivro(LivroRepository repository){
        return new CadastrarLivroUseCase(repository);
    }

    @Bean
    BuscarLivroPorIdUseCase buscarLivroPorId(LivroRepository repository){
        return new BuscarLivroPorIdUseCase(repository);
    }

    @Bean
    AtualizarLivroUseCase atualizarLivro(LivroRepository repository){
        return new AtualizarLivroUseCase(repository);
    }

    @Bean
    ExcluirLivroUseCase excluirLivro(LivroRepository repository){
        return new ExcluirLivroUseCase(repository);
    }

    @Bean
    CadastrarUsuarioUseCase cadastrarUsuario(UsuarioRepository repository, CodificadorDeSenha codificadorDeSenha){
        return new CadastrarUsuarioUseCase(repository, codificadorDeSenha);
    }

    @Bean
    BuscarUsuarioPorIdUseCase buscarUsuarioPorId(UsuarioRepository repository){
        return new BuscarUsuarioPorIdUseCase(repository);
    }

    @Bean
    AtualizarUsuarioUseCase atualizarUsuario(UsuarioRepository repository){
        return new AtualizarUsuarioUseCase(repository);
    }

    @Bean
    ExcluirUsuarioUseCase excluirUsuario(UsuarioRepository repository){
        return new ExcluirUsuarioUseCase(repository);
    }

    @Bean Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    EmprestarLivroUseCase emprestarLivro(EmprestimoRepository emprestimoRepository, UsuarioRepository usuarioRepository,
                                         LivroRepository livroRepository, Clock clock) {
        return new EmprestarLivroUseCase(emprestimoRepository, usuarioRepository, livroRepository, clock);
    }

    @Bean
    DevolverLivroUseCase devolverLivro(LivroRepository repository, EmprestimoRepository emprestimoRepository, Clock clock){
        return new DevolverLivroUseCase(emprestimoRepository, repository, clock);
    }

    @Bean
    ListarEmprestimosDoUsuarioUseCase listarEmprestimosDoUsuario(EmprestimoRepository emprestimoRepository, UsuarioRepository usuarioRepository) {
        return new ListarEmprestimosDoUsuarioUseCase(emprestimoRepository, usuarioRepository);
    }

    @Bean
    BuscarLivrosUseCase buscarLivros(LivroRepository livroRepository){
        return new BuscarLivrosUseCase(livroRepository);
    }

    @Bean
    ReservarLivroUseCase reservarLivro(ReservaRepository reservaRepository, LivroRepository livroRepository,
                                       UsuarioRepository usuarioRepository, Clock clock) {
        return new ReservarLivroUseCase(reservaRepository, livroRepository, usuarioRepository, clock);
    }

    @Bean
    CancelarReservaUseCase cancelarReserva(ReservaRepository reservaRepository) {
        return new CancelarReservaUseCase(reservaRepository);
    }

    @Bean
    ListarReservasDoUsuarioUseCase listarReservasDoUsuario(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository) {
        return new ListarReservasDoUsuarioUseCase(reservaRepository, usuarioRepository);
    }

    @Bean
    ListarLivrosMaisEmprestadosUseCase listarLivrosMaisEmprestados(EmprestimoRepository repository) {
        return new ListarLivrosMaisEmprestadosUseCase(repository);
    }

    @Bean
    ListarEmprestimosAtivosUseCase listarEmprestimosAtivos(EmprestimoRepository repository, Clock clock) {
        return new ListarEmprestimosAtivosUseCase(repository, clock);
    }
}
