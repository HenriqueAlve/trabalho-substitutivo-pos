package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.CadastrarUsuarioRequest;
import br.com.biblioteca.application.dto.UsuarioResponse;
import br.com.biblioteca.application.port.CodificadorDeSenha;
import br.com.biblioteca.domain.entity.Usuario;
import br.com.biblioteca.domain.enums.Perfil;
import br.com.biblioteca.domain.repository.UsuarioRepository;

public class CadastrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final CodificadorDeSenha codificadorDeSenha;

    public CadastrarUsuarioUseCase(UsuarioRepository usuarioRepository, CodificadorDeSenha codificadorDeSenha) {
        this.usuarioRepository = usuarioRepository;
        this.codificadorDeSenha = codificadorDeSenha;
    }

    public UsuarioResponse executar(CadastrarUsuarioRequest request) {
        Perfil perfil = request.perfil() != null ? request.perfil() : Perfil.USUARIO;
        Usuario usuario = new Usuario(null, request.nome(), request.email(),
                codificadorDeSenha.codificar(request.senha()), perfil);
        Usuario salvo = usuarioRepository.salvar(usuario);
        return new UsuarioResponse(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getPerfil());
    }
}
