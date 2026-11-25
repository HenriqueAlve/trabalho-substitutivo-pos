package br.com.biblioteca.infrastructure.config;

import br.com.biblioteca.application.usecase.*;
import br.com.biblioteca.domain.repository.EmprestimoRepository;
import br.com.biblioteca.domain.repository.LivroRepository;
import br.com.biblioteca.domain.repository.ReservaRepository;
import br.com.biblioteca.domain.repository.UsuarioRepository;
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
    CadastrarUsuarioUseCase cadastrarUsuario(UsuarioRepository repository){
        return new CadastrarUsuarioUseCase(repository);
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
    EmprestarLivroUseCase emprestarLivro(EmprestimoRepository e, UsuarioRepository u, LivroRepository l, Clock c) {
        return new EmprestarLivroUseCase(e, u, l, c);
    }

    @Bean
    DevolverLivroUseCase devolverLivro(LivroRepository repository, EmprestimoRepository emprestimoRepository, Clock clock){
        return new DevolverLivroUseCase(emprestimoRepository, repository, clock);
    }

    @Bean
    ListarEmprestimosDoUsuarioUseCase listarEmprestimosDoUsuario(EmprestimoRepository e, UsuarioRepository u) {
        return new ListarEmprestimosDoUsuarioUseCase(e, u);
    }

    @Bean
    BuscarLivrosUseCase buscarLivros(LivroRepository livroRepository){
        return new BuscarLivrosUseCase(livroRepository);
    }

    @Bean
    ReservarLivroUseCase reservarLivro(ReservaRepository r, LivroRepository l, UsuarioRepository u, Clock c) {
        return new ReservarLivroUseCase(r, l, u, c);
    }

    @Bean
    CancelarReservaUseCase cancelarReserva(ReservaRepository r) {
        return new CancelarReservaUseCase(r);
    }

    @Bean
    ListarReservasDoUsuarioUseCase listarReservasDoUsuario(ReservaRepository r, UsuarioRepository u) {
        return new ListarReservasDoUsuarioUseCase(r, u);
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
