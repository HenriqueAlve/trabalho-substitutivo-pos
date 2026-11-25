CREATE TABLE livro (
                       id          BIGSERIAL PRIMARY KEY,
                       titulo      VARCHAR(255) NOT NULL,
                       autor       VARCHAR(255) NOT NULL,
                       isbn        VARCHAR(13)  NOT NULL UNIQUE,
                       disponivel  BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE usuario (
                         id     BIGSERIAL PRIMARY KEY,
                         nome   VARCHAR(255) NOT NULL,
                         email  VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE emprestimo (
                            id                       BIGSERIAL PRIMARY KEY,
                            livro_id                 BIGINT NOT NULL REFERENCES livro(id),
                            usuario_id               BIGINT NOT NULL REFERENCES usuario(id),
                            data_emprestimo          DATE   NOT NULL,
                            data_prevista_devolucao  DATE   NOT NULL,
                            data_devolucao           DATE
);

CREATE TABLE reserva (
                         id            BIGSERIAL PRIMARY KEY,
                         livro_id      BIGINT      NOT NULL REFERENCES livro(id),
                         usuario_id    BIGINT      NOT NULL REFERENCES usuario(id),
                         data_reserva  DATE        NOT NULL,
                         status        VARCHAR(20) NOT NULL
);

-- no máximo uma reserva ativa por (livro, usuário), garantido pelo banco
CREATE UNIQUE INDEX ux_reserva_ativa ON reserva (livro_id, usuario_id) WHERE status = 'ATIVA';

INSERT INTO livro (titulo, autor, isbn, disponivel) VALUES
                                                        ('Clean Code',                      'Robert C. Martin',                 '9780132350884', TRUE),
                                                        ('Domain-Driven Design',            'Eric Evans',                       '9780321125217', TRUE),
                                                        ('Effective Java',                  'Joshua Bloch',                     '9780134685991', TRUE),
                                                        ('The Pragmatic Programmer',        'Andrew Hunt e David Thomas',       '9780135957059', TRUE),
                                                        ('Design Patterns',                 'Erich Gamma et al.',               '9780201633610', TRUE),
                                                        ('1984',                            'George Orwell',                    '9780451524935', TRUE),
                                                        ('O Senhor dos Anéis',              'J. R. R. Tolkien',                 '9780618640157', TRUE),
                                                        ('Sapiens',                         'Yuval Noah Harari',                '9780062316110', TRUE),
                                                        ('O Hobbit',                        'J. R. R. Tolkien',                 '9780547928227', TRUE),
                                                        ('O Sol é Para Todos',              'Harper Lee',                       '9780061120084', TRUE),
                                                        ('Orgulho e Preconceito',           'Jane Austen',                      '9780141439518', TRUE),
                                                        ('O Grande Gatsby',                 'F. Scott Fitzgerald',              '9780743273565', TRUE),
                                                        ('Harry Potter e a Pedra Filosofal','J. K. Rowling',                    '9780439708180', TRUE),
                                                        ('O Pequeno Príncipe',              'Antoine de Saint-Exupéry',         '9780156012195', TRUE),
                                                        ('Uma Breve História do Tempo',     'Stephen Hawking',                  '9780553380163', TRUE),
                                                        ('Cosmos',                          'Carl Sagan',                       '9780345539434', TRUE),
                                                        ('Dom Quixote',                     'Miguel de Cervantes',              '9780060934347', TRUE),
                                                        ('Crime e Castigo',                 'Fiódor Dostoiévski',               '9780486415871', TRUE),
                                                        ('Rápido e Devagar',                'Daniel Kahneman',                  '9780374533557', TRUE),
                                                        ('Duna',                            'Frank Herbert',                    '9780441172719', TRUE)
    ON CONFLICT (isbn) DO NOTHING;

CREATE INDEX idx_emprestimo_livro ON emprestimo (livro_id);
CREATE INDEX idx_emprestimo_ativos ON emprestimo (data_prevista_devolucao) WHERE data_devolucao IS NULL;
CREATE INDEX idx_emprestimo_usuario ON emprestimo (usuario_id);