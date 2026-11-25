# Biblioteca Online

API REST de uma biblioteca online: cadastro de livros e usuários, empréstimos e devoluções, reservas, consulta com filtros e paginação e relatórios de uso.

Projeto do Tech Challenge — Fase 2 (Arquitetura e Desenvolvimento Java), desenvolvido com **Clean Architecture**.

## Tecnologias

- Java 17 e Spring Boot 3.3 (Web, Data JPA, Validation, Actuator)
- PostgreSQL 16 com migrations Flyway
- Swagger / OpenAPI (springdoc)
- Docker e Docker Compose
- Lombok, JUnit 5 e H2 (testes)

## Como rodar

### Com Docker (aplicação e banco)

```bash
docker compose up --build
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Especificação OpenAPI: http://localhost:8080/v3/api-docs
- Health check: http://localhost:8080/actuator/health

O banco sobe com o schema e **20 livros** de exemplo (migration `V1`). Para recriar do zero, apagando os dados:

```bash
docker compose down -v
```

### Pela IDE (só o banco no Docker)

```bash
docker compose up -d db
```

Depois execute `BibliotecaApplication`. O banco fica exposto na porta **5433** do host (a 5432 costuma estar ocupada por um Postgres local). As variáveis `DB_URL`, `DB_USER` e `DB_PASSWORD` têm valores padrão que apontam para esse banco.

### Testes

```bash
mvn test
```

## Arquitetura

As dependências apontam sempre para dentro: `presentation` e `infrastructure` dependem de `application`, que depende de `domain`. O `domain` não conhece Spring nem JPA.

```
br.com.biblioteca
├── domain            entidades com as regras de negócio, exceções e interfaces dos repositórios (ports)
├── application       casos de uso (um por ação) e DTOs
├── infrastructure    adaptadores JPA, entidades de persistência e configuração dos beans
└── presentation      controllers REST e tratamento global de exceções
```

Os casos de uso são classes Java puras, registradas como beans em `infrastructure/config/UseCaseConfig`. Cada repositório do domínio é implementado na infraestrutura, que converte entre a entidade de domínio e a entidade JPA.

## Endpoints

A documentação completa, com exemplos e códigos de resposta, está no Swagger UI.

| Recurso | Método e rota | Descrição |
|---|---|---|
| Livros | `POST /livros` | Cadastrar |
| | `GET /livros` | Consultar com filtros (`titulo`, `autor`, `isbn`, `disponivel`) e paginação (`pagina`, `tamanho`) |
| | `GET /livros/{id}` | Buscar por id |
| | `PUT /livros/{id}` | Atualizar |
| | `DELETE /livros/{id}` | Excluir |
| Usuários | `POST /usuarios` | Cadastrar |
| | `GET /usuarios/{id}` | Buscar por id |
| | `PUT /usuarios/{id}` | Atualizar |
| | `DELETE /usuarios/{id}` | Excluir |
| | `GET /usuarios/{id}/emprestimos` | Histórico de empréstimos |
| | `GET /usuarios/{id}/reservas` | Reservas do usuário |
| Empréstimos | `POST /emprestimos` | Emprestar um livro |
| | `PUT /emprestimos/{id}/devolucao` | Devolver |
| Reservas | `POST /reservas` | Reservar um livro emprestado |
| | `PUT /reservas/{id}/cancelamento` | Cancelar reserva |
| Relatórios | `GET /relatorios/livros-mais-emprestados` | Os 20 livros mais emprestados |
| | `GET /relatorios/emprestimos-ativos` | Livros emprestados agora e previsão de devolução |

## Regras de negócio

- **ISBN**: 13 dígitos numéricos, único por livro. **E-mail** do usuário também é único.
- **Empréstimo**: só de livro disponível. A data prevista de devolução é a data do empréstimo + 14 dias. Ao devolver, o livro volta a ficar disponível.
- **Reserva**: só de livro indisponível, e um usuário não pode ter duas reservas ativas do mesmo livro. Apenas reservas ativas podem ser canceladas.
- **Exclusão**: livro ou usuário com empréstimos ou reservas vinculados não pode ser excluído.
- **Erros**: dados inválidos respondem `400`, recurso inexistente `404` e violação de regra de negócio `409`.

## Datas e fuso horário

- Todas as datas usam `java.time.LocalDate`, no formato ISO-8601 (`yyyy-MM-dd`).
- O fuso adotado é **UTC**: a JVM, o container, o Hibernate e o Jackson estão configurados em UTC.
- O cálculo das datas usa um `Clock` injetado (`Clock.systemUTC()`), o que mantém as regras de prazo testáveis.

## Otimização de acesso a dados

- **Paginação** na consulta de livros (tamanho máximo de 100 por página, ordenada por título).
- Filtros montados dinamicamente com `Specification`, de modo que só as condições informadas entram na consulta.
- Relatórios resolvidos no banco: `GROUP BY` com `COUNT` e limite de 20 linhas; empréstimos ativos com `join fetch` para evitar o problema N+1.
- Índices em `emprestimo` (`livro_id`, `usuario_id` e um índice parcial para empréstimos em aberto) e índice único parcial para reservas ativas.
- `batch_size` do Hibernate configurado para operações em lote.
