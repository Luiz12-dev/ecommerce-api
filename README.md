# 🛒 E-Commerce API

API RESTful completa para um sistema de e-commerce, construída com **Java 21** e **Spring Boot 3.5**. Projeto de portfólio demonstrando domínio em arquitetura em camadas, segurança com JWT, relacionamentos JPA, paginação e documentação interativa com Swagger.

---

## 🚀 Tecnologias

| Tecnologia | Versão | Descrição |
|-----------|--------|-----------|
| **Java** | 21 | Linguagem principal (LTS) |
| **Spring Boot** | 3.5.11 | Framework principal |
| **Spring Security** | 6.x | Autenticação e autorização |
| **Spring Data JPA** | 3.x | Persistência e ORM |
| **PostgreSQL** | 16 | Banco de dados relacional |
| **Flyway** | 10.x | Versionamento de migrations |
| **JJWT** | 0.11.5 | Geração e validação de tokens JWT |
| **Lombok** | — | Redução de boilerplate |
| **Bean Validation** | 3.x | Validação de dados de entrada |
| **SpringDoc OpenAPI** | 2.3.0 | Swagger UI interativo |
| **Docker Compose** | — | Infraestrutura containerizada |

---

## 📐 Arquitetura

O projeto segue a **Arquitetura em Camadas (Layered Architecture)** com separação clara de responsabilidades:

```
src/main/java/com/projeto/e_commerce/
├── auth/                  # Autenticação (registro, login, JWT)
├── catalog/               # Categorias de produtos
├── customer/              # Gestão de clientes
├── product/               # Catálogo de produtos
├── address/               # Endereços de entrega
├── order/                 # Pedidos e itens do pedido
├── config/security/       # Configuração Spring Security + JWT
└── exception/             # Tratamento global de exceções
```

Cada módulo contém: **Entity → Repository → Service → Controller → DTOs**

---

## 🗄️ Diagrama de Entidades

```
┌─────────────┐       ┌──────────────┐       ┌──────────────┐
│   APP_USER  │       │   CUSTOMER   │──────<│   ADDRESS    │
│─────────────│       │──────────────│  1:N  │──────────────│
│ id (UUID)   │       │ id (UUID)    │       │ id (UUID)    │
│ full_name   │       │ name         │       │ street       │
│ email       │       │ email        │       │ number       │
│ password    │       │ created_at   │       │ city, state  │
│ role        │       │ updated_at   │       │ zip_code     │
│ active      │       └──────────────┘       │ customer_id  │
└─────────────┘              │               └──────┬───────┘
                             │ 1:N                   │
                      ┌──────┴───────┐               │
                      │  ORDER_TABLE │───────────────┘
                      │──────────────│  N:1 (endereço de entrega)
                      │ id (UUID)    │
                      │ customer_id  │       ┌──────────────┐
                      │ address_id   │       │   CATEGORY   │
                      │ status       │       │──────────────│
                      │ total_amount │       │ id (UUID)    │
                      └──────┬───────┘       │ name         │
                             │ 1:N           │ description  │
                      ┌──────┴───────┐       │ active       │
                      │  ORDER_ITEM  │       └──────┬───────┘
                      │──────────────│              │ 1:N
                      │ id (UUID)    │       ┌──────┴───────┐
                      │ order_id     │       │   PRODUCT    │
                      │ product_id ──│──────>│──────────────│
                      │ quantity     │  N:1  │ id (UUID)    │
                      │ unit_price   │       │ name         │
                      │ subtotal     │       │ price        │
                      └──────────────┘       │ stock_qty    │
                                             │ category_id  │
                                             │ active       │
                                             └──────────────┘
```

---

## 📋 Endpoints

### 🔓 Autenticação (`/api/v1/auth`)
| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| POST | `/register` | Registrar usuário | Público |
| POST | `/login` | Login (retorna JWT) | Público |

### 📁 Categorias (`/api/v1/categories`)
| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/` | Listar (paginado) | Público |
| GET | `/{id}` | Buscar por ID | Público |
| POST | `/` | Criar | ADMIN |
| PUT | `/{id}` | Atualizar | ADMIN |
| DELETE | `/{id}` | Desativar (soft delete) | ADMIN |

### 📦 Produtos (`/api/v1/products`)
| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/` | Listar (paginado) | Público |
| GET | `/{id}` | Buscar por ID | Público |
| GET | `/category/{categoryId}` | Filtrar por categoria | Público |
| GET | `/search?name=...` | Buscar por nome | Público |
| POST | `/` | Criar | ADMIN |
| PUT | `/{id}` | Atualizar | ADMIN |
| DELETE | `/{id}` | Desativar (soft delete) | ADMIN |

### 👤 Clientes (`/api/v1/customers`)
| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/` | Listar (paginado) | Autenticado |
| GET | `/{id}` | Buscar por ID | Autenticado |
| POST | `/` | Criar | Autenticado |
| PUT | `/{id}` | Atualizar | Autenticado |
| DELETE | `/{id}` | Deletar | Autenticado |

### 📍 Endereços (`/api/v1/addresses`)
| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/{id}` | Buscar por ID | Autenticado |
| GET | `/customer/{customerId}` | Listar por cliente | Autenticado |
| POST | `/` | Criar | Autenticado |
| PUT | `/{id}` | Atualizar | Autenticado |
| DELETE | `/{id}` | Deletar | Autenticado |

### 🛒 Pedidos (`/api/v1/orders`)
| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/{id}` | Detalhe do pedido | Autenticado |
| GET | `/customer/{customerId}` | Pedidos do cliente | Autenticado |
| POST | `/` | Criar pedido | Autenticado |
| PATCH | `/{id}/status` | Atualizar status | Autenticado |
| PATCH | `/{id}/cancel` | Cancelar (devolve estoque) | Autenticado |

---

## 🧠 Regras de Negócio

- **Controle de estoque**: ao criar um pedido, o estoque é decrementado automaticamente. Ao cancelar, é reestabelecido.
- **Máquina de estados**: pedidos seguem o fluxo `PENDING → CONFIRMED → SHIPPED → DELIVERED`. Transições inválidas são rejeitadas.
- **Preço congelado**: o `unit_price` é registrado no item do pedido no momento da compra, protegendo contra alterações futuras de preço.
- **Soft delete**: categorias e produtos são desativados (não apagados), mantendo integridade referencial com pedidos existentes.
- **Validação de pertencimento**: o endereço de entrega deve pertencer ao cliente que está realizando o pedido.

---

## ⚙️ Como Executar

### Pré-requisitos
- Java 21+
- Maven 3.9+
- Docker e Docker Compose

### 1. Subir o banco de dados
```bash
docker-compose up -d
```

### 2. Executar a aplicação
```bash
./mvnw spring-boot:run
```

### 3. Acessar o Swagger UI
```
http://localhost:8080/swagger-ui.html
```

---

## 🔐 Autenticação

A API utiliza **JWT (JSON Web Token)** com algoritmo HS512. Para acessar endpoints protegidos:

1. Registre um usuário via `POST /api/v1/auth/register`
2. Faça login via `POST /api/v1/auth/login` — o token JWT será retornado
3. Inclua o token nas requisições:
```
Authorization: Bearer <seu_token_aqui>
```

### Perfis de Acesso
| Role | Permissões |
|------|-----------|
| `CUSTOMER` | Gerenciar pedidos, endereços e dados pessoais |
| `ADMIN` | Tudo acima + gerenciar categorias e produtos |

---

## 📁 Migrations (Flyway)

| Versão | Descrição |
|--------|-----------|
| V1 | Criação da tabela `customer` |
| V2 | Criação da tabela `category` |
| V3 | Criação da tabela `product` |
| V4 | Criação da tabela `address` |
| V5 | Criação da tabela `order_table` |
| V6 | Criação da tabela `order_item` |
| V7 | Criação da tabela `app_user` |

---

## 🛠️ Melhorias Futuras

- [ ] Integração com gateway de pagamento
- [ ] Envio de e-mails transacionais (confirmação de pedido)
- [ ] Cache com Redis para catálogo de produtos
- [ ] Testes unitários e de integração
- [ ] Deploy com CI/CD (GitHub Actions + AWS/Railway)

---

## 👨‍💻 Autor

Desenvolvido por **Luiz Otávio** como projeto de portfólio.
