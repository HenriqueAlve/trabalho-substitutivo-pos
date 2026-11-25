package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.CadastrarUsuarioRequest;
import br.com.biblioteca.application.dto.UsuarioResponse;
import br.com.biblioteca.domain.entity.Usuario;
import br.com.biblioteca.domain.repository.UsuarioRepository;

public class CadastrarUsuarioUseCase {

    private final UsuarioRepository usarioRepository;

    public CadastrarUsuarioUseCase(UsuarioRepository repository){
         this.usarioRepository = repository;
    }

    public UsuarioResponse executar(CadastrarUsuarioRequest request){
        Usuario usuario = new Usuario(null, request.nome(),request.email());
        Usuario salvo = usarioRepository.salvar(usuario);
        return new UsuarioResponse(salvo.getId(), salvo.getNome(), salvo.getEmail());
    }
}
