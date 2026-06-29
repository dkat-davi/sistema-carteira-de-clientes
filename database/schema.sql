DROP DATABASE IF EXISTS carteira_clientes;

CREATE DATABASE carteira_clientes
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE carteira_clientes;

CREATE TABLE usuario (

    id INT AUTO_INCREMENT PRIMARY KEY,

    nome VARCHAR(150) NOT NULL,

    cpf VARCHAR(11),

    login VARCHAR(50) NOT NULL UNIQUE,

    senha VARCHAR(255) NOT NULL,

    role ENUM('ADMIN','USUARIO') NOT NULL,

    ativo BOOLEAN DEFAULT TRUE

);

CREATE TABLE loja (

    id INT AUTO_INCREMENT PRIMARY KEY,

    nome VARCHAR(120) NOT NULL,

    telefone VARCHAR(20),

    endereco VARCHAR(200),

    cidade VARCHAR(100),

    estado CHAR(2),

    ativo BOOLEAN DEFAULT TRUE

);

CREATE TABLE usuario_loja (

    id INT AUTO_INCREMENT PRIMARY KEY,

    usuario_id INT NOT NULL,

    loja_id INT NOT NULL,

    cargo ENUM('GERENTE','VENDEDOR') NOT NULL,

    CONSTRAINT fk_usuario_loja_usuario
        FOREIGN KEY(usuario_id)
        REFERENCES usuario(id),

    CONSTRAINT fk_usuario_loja_loja
        FOREIGN KEY(loja_id)
        REFERENCES loja(id),

    UNIQUE(usuario_id, loja_id)

);

CREATE TABLE cliente (

    id INT AUTO_INCREMENT PRIMARY KEY,

    nome VARCHAR(150) NOT NULL,

    cpf VARCHAR(11),

    telefone VARCHAR(20),

    email VARCHAR(150),

    endereco VARCHAR(200),

    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP,

    loja_id INT NOT NULL,

    vendedor_id INT NOT NULL,

    CONSTRAINT fk_cliente_loja
        FOREIGN KEY(loja_id)
        REFERENCES loja(id),

    CONSTRAINT fk_cliente_vendedor
        FOREIGN KEY(vendedor_id)
        REFERENCES usuario(id)

);

CREATE TABLE atendimento (

    id INT AUTO_INCREMENT PRIMARY KEY,

    cliente_id INT NOT NULL,

    usuario_id INT NOT NULL,

    data_atendimento DATETIME DEFAULT CURRENT_TIMESTAMP,

    descricao TEXT,

    CONSTRAINT fk_atendimento_cliente
        FOREIGN KEY(cliente_id)
        REFERENCES cliente(id),

    CONSTRAINT fk_atendimento_usuario
        FOREIGN KEY(usuario_id)
        REFERENCES usuario(id)
);