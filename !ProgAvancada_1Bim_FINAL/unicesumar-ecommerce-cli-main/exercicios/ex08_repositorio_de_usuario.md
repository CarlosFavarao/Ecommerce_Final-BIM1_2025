
# 🧠 Exercício Prático – Criação do `UserRepository` com JDBC

## 🎯 Objetivo

Reproduzir o mesmo padrão utilizado no `ProductRepository`, criando um repositório para a entidade `User`, utilizando **JDBC puro** e **sem JPA**.

---

## 📘 Contexto

Nós já implementamos o `ProductRepository`, que manipula registros na tabela `products` diretamente via comandos SQL usando JDBC. Agora, deve fazer o mesmo com os dados da tabela `users`.

---

## 📝 Requisitos do Exercício

### 1. Criar a entidade `User`

Crie uma classe chamada `User` no pacote `entities` com os seguintes campos:

```java
private UUID uuid;
private String name;
private String email;
private String password;
```

Implemente os construtores, getters e setters **necessários**.

---

### 2. Criar o repositório `UserRepository`

Crie uma nova classe no pacote `repository`, chamada `UserRepository`, implementando a interface `EntityRepository<User>` da mesma forma que no `ProductRepository`.

Implemente os seguintes métodos:

- `void save(User user)`
- `Optional<User> findById(UUID uuid)`
- `List<User> findAll()`
- `void deleteById(UUID uuid)`

Todos os métodos devem utilizar **PreparedStatements** com **tratamento de exceção** (try-catch) e converter os resultados corretamente, assim como foi feito com `Product`.

---

### 3. Criar o SQL necessário

As operações devem atuar sobre a tabela `users`. Dentro do diretório `scripts/init.sql` contém o código de `CREATE TABLE` necessário
para criar a tabela no banco de dados SQLite.

---

### 4. Testar na classe principal (Main)

A classe Main já foi atualizada para incluir um menu de cadastro de usuários e listagem de usuários. Você deve colocar o seu código para
rodar implementando estes métodos que hoje estão sem implementação.
