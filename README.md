# 🛒 Sistema de Gerenciamento de Produtos e Lojas

Projeto full stack desenvolvido com **Spring Boot (Java)** e **Angular**, com autenticação JWT, envio de e-mails e geração de relatórios via **JasperReports**.

---

## 🚀 Tecnologias Utilizadas

### 🔹 Backend
- **Java 17+**
- **Spring Boot 3+**
  - Spring Web
  - Spring Data JPA
  - Spring Security (JWT)
  - Spring Mail
- **MySQL**
- **JasperReports**
- **Maven**

### 🔹 Frontend
- **Angular 18+**
- **TypeScript**
- **Angular Material** / **Tailwindcss**
- **JWT Interceptor**
- **Serviços REST**

---

## 🧩 Funcionalidades Principais

### 👥 Autenticação e Autorização
- Login e registro de usuários
- Tokens JWT com expiração
- Perfis de acesso: `ADMIN` e `USER`

### 🏬 Gestão de Lojas
- CRUD completo de lojas
- Associação de produtos a uma loja
- Listagem e filtros

### 📦 Gestão de Produtos
- CRUD completo de produtos
- Filtros por categoria, preço e loja
- Upload de imagens (opcional)

### 📧 Envio de E-mails
- Envio automático de confirmação de cadastro ou ações administrativas
- Configuração SMTP via `application.yml`

### 📊 Relatórios
- Geração de relatórios em PDF utilizando **JasperReports**

### 🌐 Interface Web (Angular)
- Design moderno e responsivo
- Dashboard intuitivo com cards
- Integração total com a API REST
- Controle de sessão via JWT

---

## 🖼️ Prints do Sistema

![Tela de Login](https://i.imgur.com/9cwWsBq.png)
![Tela de Registro](https://i.imgur.com/tSzw9LW.png)
![Dashboard](https://i.imgur.com/zkpeKD2.png)
![Lojas](https://i.imgur.com/AdkbxPR.png)
![Produtos](https://i.imgur.com/R14l8Zh.png)

---
