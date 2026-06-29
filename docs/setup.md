# Guia de Configuração do Projeto

Este documento explica como configurar o ambiente de desenvolvimento do projeto **Sistema Carteira de Clientes**.

---

## Requisitos

Antes de iniciar, verifique se você possui instalado:

- Java 17 ou superior;
- Apache NetBeans;
- Docker;
- Docker Compose;
- DBeaver;
- Git;
- MySQL Connector/J;
- JasperReports.

---

## 1. Clonar o Repositório

Clone o projeto do GitHub:

```bash
git clone https://github.com/seu-usuario/sistema-carteira-de-clientes.git
```

Acesse a pasta do projeto:

```bash
cd sistema-carteira-de-clientes
```

---

## 2. Subir o Banco de Dados com Docker

O projeto utiliza MySQL 8 rodando em Docker.

Na raiz do projeto, execute:

```bash
docker compose up -d
```

Verifique se o container está rodando:

```bash
docker ps
```

O banco deve estar disponível em:

```text
Host: localhost
Porta: 3306
Usuário: root
Senha: root
Banco: carteira_clientes
```

---

## 3. Configurar o Banco no DBeaver

Abra o DBeaver e crie uma nova conexão MySQL com os seguintes dados:

```text
Host: localhost
Porta: 3306
Database: carteira_clientes
Usuário: root
Senha: root
```

Caso o banco ainda não exista, ele será criado pelo script `schema.sql`.

---

## 4. Executar o Script de Criação do Banco

No DBeaver, execute o arquivo:

```text
database/schema.sql
```

Esse script cria:

- o banco `carteira_clientes`;
- a tabela `usuario`;
- a tabela `loja`;
- a tabela `usuario_loja`;
- a tabela `cliente`;
- a tabela `atendimento`;
- os relacionamentos entre as tabelas.

---

## 5. Popular o Banco com Dados Iniciais

Depois de executar o `schema.sql`, execute o arquivo:

```text
database/seed.sql
```

Esse script insere dados iniciais para testes, como:

- usuários;
- lojas;
- vínculos entre usuários e lojas;
- clientes;
- atendimentos.

Usuário administrador inicial:

```text
Login: admin
Senha: admin123
Perfil: ADMIN
```

Usuários comuns para teste:

```text
Login: bruno
Senha: 123456

Login: carla
Senha: 123456

Login: elaine
Senha: 123456
```

---

## 6. Abrir o Projeto no NetBeans

Abra o Apache NetBeans.

Em seguida:

1. Clique em `File`;
2. Clique em `Open Project`;
3. Selecione a pasta do projeto;
4. Aguarde o NetBeans carregar a estrutura.

---

## 7. Configurar o Driver JDBC

O projeto utiliza JDBC para comunicação com o MySQL.

Será necessário adicionar o driver **MySQL Connector/J** às bibliotecas do projeto no NetBeans.

No NetBeans:

1. Clique com o botão direito no projeto;
2. Vá em `Properties`;
3. Acesse `Libraries`;
4. Clique em `Add JAR/Folder`;
5. Selecione o arquivo `.jar` do MySQL Connector/J;
6. Salve as alterações.

---

## 8. URL JDBC Utilizada

A URL de conexão utilizada pelo projeto será:

```text
jdbc:mysql://localhost:3306/carteira_clientes?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
```

Credenciais:

```text
Usuário: root
Senha: root
```

Essas informações serão utilizadas na classe `ConnectionFactory`.

---

## 9. Testar a Conexão

Após criar a `ConnectionFactory`, o sistema deverá conseguir abrir conexão com o banco MySQL.

A conexão será usada pelos DAOs para executar comandos SQL.

Fluxo da aplicação:

```text
View
 ↓
Controller
 ↓
DAO
 ↓
ConnectionFactory
 ↓
MySQL
```

---

## 10. Parar o Banco de Dados

Para parar o container MySQL, execute:

```bash
docker compose down
```

Para parar e remover também os volumes do banco:

```bash
docker compose down -v
```

Atenção: o comando com `-v` remove os dados salvos no volume do banco.

---

## Próximo Passo

Após configurar o ambiente, o próximo passo do desenvolvimento é criar a classe:

```text
src/br/com/daviteixeira/carteiraclientes/factory/ConnectionFactory.java
```

Essa classe será responsável por centralizar a criação de conexões JDBC com o banco MySQL.
