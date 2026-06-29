package br.com.daviteixeira.carteiraclientes.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final String URL = "jdbc:mysql://localhost:3306/carteira_clientes?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo";

    private static final String USUARIO = "root";

    private static final String SENHA = "root";

    private static ConnectionFactory instance;

    private Connection connection;

    private ConnectionFactory() {
        carregarDriver();
    }

    public static synchronized ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }

        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("Conexão estabelecida com sucesso!");
            }

            return connection;

        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao conectar com o banco de dados.", e);
        }
    }

    public void fecharConexao() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão encerrada com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar conexão com o banco de dados: " + e.getMessage());
        }
    }

    private void carregarDriver() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL não encontrado: " + e.getMessage());
            throw new RuntimeException("Driver MySQL não encontrado.", e);
        }
    }
}