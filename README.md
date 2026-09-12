# Autoescola - Sistema de Agendamento de Instruções

API REST desenvolvida em Java com Spring Boot para gerenciamento de autoescola, contemplando o cadastro de alunos e instrutores, controle de usuários e perfis de acesso com Spring Security e JWT, além de regras de negócio para agendamento e cancelamento de instruções práticas.

---

## Tecnologias Utilizadas

- Java 25
- Spring Boot 4.1.1
- Spring Data JPA / Hibernate
- Spring Security (Autenticação Stateless com Tokens JWT via Auth0 Java-JWT)
- Oracle Database (com migrações gerenciadas pelo Flyway)
- Bean Validation (Hibernate Validator)
- Springdoc OpenAPI 2.8.5 (Swagger UI)
- Maven

---

## Configuração do Banco de Dados

As configurações de conexão com o banco de dados Oracle estão definidas no arquivo `src/main/resources/application.properties`:

- As tabelas e dados iniciais são criados automaticamente pelo Flyway na inicialização da aplicação (`db/migration`).
- O sistema já inicia com um usuário administrador padrão:
  - **Login:** admin@autoescola.com
  - **Senha:** 123456

---

## Executando a Aplicação

Para compilar e executar o projeto:

```bash
# Executar a aplicação
./mvnw spring-boot:run

# Executar a suíte de testes automatizados
./mvnw test
```

A aplicação iniciará na porta padrão `8080`.

---

## Documentação e Teste dos Endpoints (Swagger UI)

Com a aplicação em execução, acesse a documentação interativa pelo navegador:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

---

## Autenticação e Segurança

A API utiliza autenticação stateless via Bearer Token JWT.

1. Envie uma requisição `POST /login` com as credenciais no corpo:
```json
{
  "login": "admin@autoescola.com",
  "senha": "123456"
}
```
2. Utilize o token retornado no cabeçalho das requisições protegidas:
```http
Authorization: Bearer <seu_token_aqui>
```

---

## Principais Endpoints

### Autenticação
- POST /login - Autentica usuário e retorna o token JWT.

### Alunos (/alunos)
- POST /alunos - Cadastra um novo aluno.
- GET /alunos - Lista alunos ativos (paginado, 10 por página, ordenado por nome).
- GET /alunos/{id} - Detalha informações de um aluno.
- PUT /alunos - Atualiza nome, telefone e endereço do aluno.
- DELETE /alunos/{id} - Inativa o aluno (exclusão lógica).

### Instrutores (/instrutores)
- POST /instrutores - Cadastra um novo instrutor (com especialidade: MOTOS, CARROS, VANS ou CAMINHOES).
- GET /instrutores - Lista instrutores ativos (paginado, 10 por página, ordenado por nome).
- GET /instrutores/{id} - Detalha informações de um instrutor.
- PUT /instrutores - Atualiza nome, telefone e endereço do instrutor.
- DELETE /instrutores/{id} - Inativa o instrutor (exclusão lógica).

### Usuários (/usuarios)
- POST /usuarios - Cadastra um novo usuário com senha criptografada em BCrypt *(Apenas ADMIN)*.
- GET /usuarios - Lista usuários ativos *(Apenas ADMIN)*.
- PUT /usuarios - Atualiza perfil do usuário para ADMIN ou COMUM *(Apenas ADMIN)*.
- DELETE /usuarios/{id} - Inativa usuário *(Apenas ADMIN)*.
- PUT /usuarios/alterar-senha - Permite ao usuário logado alterar sua própria senha.

### Instruções (/instrucoes)
- POST /instrucoes - Realiza o agendamento de uma instrução prática.
  - Caso o instrutor não seja informado, o sistema escolhe aleatoriamente um instrutor ativo e disponível na especialidade solicitada.
- DELETE /instrucoes - Cancela uma instrução agendada informando o motivo (ALUNO_DESISTIU, INSTRUTOR_CANCELOU ou OUTROS).

---

## Regras de Negócio Implementadas

### Agendamento:
1. **Horário de Funcionamento:** Segunda a Sábado, das 06:00 às 21:00 (última instrução com início às 20:00).
2. **Antecedência Mínima:** O agendamento deve ser feito com pelo menos 30 minutos de antecedência.
3. **Status Ativo:** Aluno e instrutor devem estar com cadastro ativo no sistema.
4. **Limite Diário do Aluno:** O aluno pode realizar no máximo 2 instruções no mesmo dia.
5. **Disponibilidade do Instrutor:** O instrutor não pode ter outra instrução agendada no mesmo horário.

### Cancelamento:
1. **Antecedência Mínima:** O cancelamento só é permitido com antecedência mínima de 24 horas em relação ao horário da instrução.
2. **Motivo Obrigatório:** É obrigatório informar o motivo do cancelamento.
