package br.com.daviteixeira.carteiraclientes.dao;

import br.com.daviteixeira.carteiraclientes.factory.ConnectionFactory;
import br.com.daviteixeira.carteiraclientes.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private static final String SELECT_CLIENTE = """
        SELECT
            c.id,
            c.nome,
            c.cpf,
            c.telefone,
            c.email,
            c.endereco,
            c.data_cadastro,
            c.loja_id,
            c.vendedor_id,
            l.nome AS loja_nome,
            u.nome AS vendedor_nome
        FROM cliente c
        INNER JOIN loja l ON l.id = c.loja_id
        INNER JOIN usuario u ON u.id = c.vendedor_id
    """;

    public void salvar(Cliente cliente) {
        String sql = """
            INSERT INTO cliente (
                nome,
                cpf,
                telefone,
                email,
                endereco,
                loja_id,
                vendedor_id
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, cliente.getNome());
                stmt.setString(2, cliente.getCpf());
                stmt.setString(3, cliente.getTelefone());
                stmt.setString(4, cliente.getEmail());
                stmt.setString(5, cliente.getEndereco());
                stmt.setInt(6, cliente.getLojaId());
                stmt.setInt(7, cliente.getVendedorId());

                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro ao salvar cliente: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar cliente.", e);
        }
    }

    public void atualizar(Cliente cliente) {
        String sql = """
            UPDATE cliente
            SET nome = ?,
                cpf = ?,
                telefone = ?,
                email = ?,
                endereco = ?,
                loja_id = ?,
                vendedor_id = ?
            WHERE id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, cliente.getNome());
                stmt.setString(2, cliente.getCpf());
                stmt.setString(3, cliente.getTelefone());
                stmt.setString(4, cliente.getEmail());
                stmt.setString(5, cliente.getEndereco());
                stmt.setInt(6, cliente.getLojaId());
                stmt.setInt(7, cliente.getVendedorId());
                stmt.setInt(8, cliente.getId());

                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro ao atualizar cliente: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar cliente.", e);
        }
    }

    public void excluir(int id) {
        String sql = """
            DELETE FROM cliente
            WHERE id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro ao excluir cliente: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir cliente.", e);
        }
    }

    public Cliente buscarPorId(int id) {
        String sql = SELECT_CLIENTE + """
            WHERE c.id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return preencherCliente(rs);
                    }
                }
            }

            return null;

        } catch (Exception e) {
            System.out.println("Erro ao buscar cliente por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar cliente por ID.", e);
        }
    }

    public List<Cliente> listarTodos() {
        String sql = SELECT_CLIENTE + """
            ORDER BY c.nome
        """;

        List<Cliente> clientes = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {
                while (rs.next()) {
                    clientes.add(preencherCliente(rs));
                }
            }

            return clientes;

        } catch (Exception e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
            throw new RuntimeException("Erro ao listar clientes.", e);
        }
    }

    public List<Cliente> listarPorVendedor(int vendedorId) {
        String sql = SELECT_CLIENTE + """
            WHERE c.vendedor_id = ?
            ORDER BY c.nome
        """;

        List<Cliente> clientes = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, vendedorId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        clientes.add(preencherCliente(rs));
                    }
                }
            }

            return clientes;

        } catch (Exception e) {
            System.out.println("Erro ao listar clientes por vendedor: " + e.getMessage());
            throw new RuntimeException("Erro ao listar clientes por vendedor.", e);
        }
    }

    public List<Cliente> pesquisarTodos(String termo) {
        String sql = SELECT_CLIENTE + """
            WHERE c.nome LIKE ?
            OR c.cpf LIKE ?
            OR c.telefone LIKE ?
            OR l.nome LIKE ?
            OR u.nome LIKE ?
            ORDER BY c.nome
        """;

        List<Cliente> clientes = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                String filtro = "%" + termo + "%";

                stmt.setString(1, filtro);
                stmt.setString(2, filtro);
                stmt.setString(3, filtro);
                stmt.setString(4, filtro);
                stmt.setString(5, filtro);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        clientes.add(preencherCliente(rs));
                    }
                }
            }

            return clientes;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar clientes: " + e.getMessage());
            throw new RuntimeException("Erro ao pesquisar clientes.", e);
        }
    }

    public List<Cliente> pesquisarPorVendedor(String termo, int vendedorId) {
        String sql = SELECT_CLIENTE + """
            WHERE c.vendedor_id = ?
            AND (
                c.nome LIKE ?
                OR c.cpf LIKE ?
                OR c.telefone LIKE ?
                OR l.nome LIKE ?
                OR u.nome LIKE ?
            )
            ORDER BY c.nome
        """;

        List<Cliente> clientes = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                String filtro = "%" + termo + "%";

                stmt.setInt(1, vendedorId);
                stmt.setString(2, filtro);
                stmt.setString(3, filtro);
                stmt.setString(4, filtro);
                stmt.setString(5, filtro);
                stmt.setString(6, filtro);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        clientes.add(preencherCliente(rs));
                    }
                }
            }

            return clientes;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar clientes por vendedor: " + e.getMessage());
            throw new RuntimeException("Erro ao pesquisar clientes por vendedor.", e);
        }
    }

    private Cliente preencherCliente(ResultSet rs) throws Exception {
        Cliente cliente = new Cliente();

        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setEmail(rs.getString("email"));
        cliente.setEndereco(rs.getString("endereco"));
        cliente.setLojaId(rs.getInt("loja_id"));
        cliente.setVendedorId(rs.getInt("vendedor_id"));
        cliente.setLojaNome(rs.getString("loja_nome"));
        cliente.setVendedorNome(rs.getString("vendedor_nome"));

        Timestamp dataCadastro = rs.getTimestamp("data_cadastro");

        if (dataCadastro != null) {
            cliente.setDataCadastro(dataCadastro.toLocalDateTime());
        }

        return cliente;
    }
}