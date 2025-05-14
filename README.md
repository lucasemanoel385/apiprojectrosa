# 📦 API Backend - Spring Boot

API desenvolvida com **Java Spring Boot** para gerenciamento de categorias, produtos/itens, clientes, contratos e contabilidade. Oferece endpoints REST com autenticação via **JWT**, versionamento de banco com **Flyway**, e documentação com **SpringDoc**.

---

## ⚙️ Tecnologias

- Java 17+
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- Flyway
- MySQL
- WebSocket
- SpringDoc OpenAPI (Swagger)

---

## 🚀 Funcionalidades

- **CRUD completo** para:
  - Categorias
  - Produtos/Itens
  - Clientes
  - Contratos
  - Itens do contrato

- **Validações personalizadas** conforme regras de negócio

- **Endpoints adicionais**:
  - Filtros e buscas customizadas
  - WebSocket para atualização em tempo real em endpoint específico

- **Gestão contábil**:
  - Registro de entradas (pagamentos/recebimentos)
  - Registro de despesas
  - Filtros por **ano** e **mês**
  - Cálculo de totais e saldo líquido

---

## 🔐 Autenticação

A API utiliza **JWT (JSON Web Token)** para autenticação. Para acessar endpoints protegidos, envie o token no header:

```
Authorization: Bearer <seu-token>
```

---

## 📚 Documentação da API

Acesse a documentação interativa via Swagger:

```
http://localhost:8080/swagger-ui.html
```

Ou

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🧪 Como executar

1. **Clone o repositório:**

```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
```

2. **Configure o banco de dados MySQL:**

Atualize o `application.yml` com suas credenciais e URL do banco.

3. **Execute o projeto:**

```bash
./mvnw spring-boot:run
```

> As migrations do Flyway são executadas automaticamente ao iniciar a aplicação.

---

## 🗂️ Estrutura das entidades

- `Categoria`
- `Produto/Item`
- `Cliente`
- `Contrato`
- `Item do Contrato`
- `Contabilidade` (entradas, despesas, filtros por período)

---

## 🧑‍💻 Autor

Desenvolvido por [Seu Nome].

---

## 📄 Licença

Distribuído sob a licença [MIT](LICENSE).
