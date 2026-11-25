package br.com.biblioteca.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "livro")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivroJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private boolean disponivel;
}
