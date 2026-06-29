# Sistema Carteira de Clientes

Sistema desktop desenvolvido em **Java Swing** com padrão **MVC**, utilizando **JDBC** para persistência de dados em banco **MySQL 8**.

O objetivo do sistema é controlar uma carteira de clientes, garantindo que cada cliente esteja vinculado a uma loja e a um vendedor responsável, mantendo também o histórico de atendimentos realizados.

Este projeto está sendo desenvolvido para fins acadêmicos, com foco em organização, simplicidade, boas práticas e código didático.

---

## Objetivo do Sistema

O sistema permite gerenciar uma carteira de clientes de forma organizada.

Cada cliente pertence obrigatoriamente a:

- uma loja;
- um vendedor responsável.

Além disso, cada atendimento realizado com o cliente fica registrado no sistema, permitindo acompanhar o histórico de relacionamento e preservar a carteirização.

---

## Tecnologias Utilizadas

- Java 17+
- Java Swing
- JDBC
- MySQL 8
- Docker
- Docker Compose
- DBeaver
- Apache NetBeans
- JasperReports
- Git
- GitHub

---

## Arquitetura

O projeto segue o padrão **MVC**:

```text
View
 ↓
Controller
 ↓
DAO
 ↓
MySQL
```

A estrutura principal do código está organizada em pacotes:

```text
src/br/com/daviteixeira/carteiraclientes
```

Com os seguintes módulos:

```text
controller/
dao/
factory/
model/
util/
view/
```

Mais detalhes sobre a arquitetura estão disponíveis em:

[Documentação da Arquitetura](docs/arquitetura.md)

---

## Funcionalidades Previstas

### Administrador

O perfil `ADMIN` poderá:

- cadastrar usuários;
- cadastrar lojas;
- vincular usuários às lojas;
- cadastrar clientes;
- registrar atendimentos;
- emitir relatórios.

### Usuário

O perfil `USUARIO` poderá:

- acessar somente suas funcionalidades;
- cadastrar clientes;
- realizar atendimentos;
- visualizar apenas seus clientes.

---

## Banco de Dados

O sistema utiliza banco **MySQL 8** rodando em Docker.

Banco utilizado:

```text
carteira_clientes
```

Tabelas principais:

- usuario
- loja
- usuario_loja
- cliente
- atendimento

Mais detalhes sobre a estrutura do banco estão disponíveis em:

[Documentação do Banco de Dados](docs/banco_de_dados.md)

---

## Configuração do Projeto

O passo a passo para configurar o ambiente, subir o banco de dados, executar os scripts SQL e abrir o projeto no NetBeans está disponível em:

[Guia de Configuração](docs/setup.md)

---

## Regras de Negócio

As regras de negócio do sistema estão documentadas em:

[Regras de Negócio](docs/regras_de_negocio.md)

---

## Relatórios

O sistema utilizará **JasperReports** para geração de relatórios.

Relatórios previstos:

- Clientes por Loja;
- Clientes por Vendedor;
- Atendimentos por Período;
- Usuários por Loja.

Mais detalhes estão disponíveis em:

[Documentação de Relatórios](docs/relatorios.md)

---

## Fluxo de Desenvolvimento

A ordem planejada de desenvolvimento do sistema está documentada em:

[Fluxo de Desenvolvimento](docs/fluxo_de_desenvolvimento.md)

---

## Status do Projeto

Projeto em desenvolvimento.

Etapas planejadas:

1. ConnectionFactory
2. Login
3. DAO Usuário
4. Autenticação
5. Tela Principal
6. Cadastro de Loja
7. Cadastro de Usuário
8. Vínculo Usuário x Loja
9. Cadastro de Cliente
10. Atendimento
11. Relatórios JasperReports

---

## Autor

Projeto desenvolvido por Daví Teixeira para fins acadêmicos.
