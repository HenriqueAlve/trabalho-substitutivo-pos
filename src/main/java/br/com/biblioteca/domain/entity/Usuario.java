package br.com.biblioteca.domain.entity;

import br.com.biblioteca.domain.enums.Perfil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Usuario {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private Perfil perfil;

    public void atualizar(String nome, String email){
        this.nome = nome;
        this.email = email;
    }
}
