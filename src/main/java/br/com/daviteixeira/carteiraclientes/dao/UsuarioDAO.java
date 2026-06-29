package br.com.daviteixeira.carteiraclientes.dao;

import br.com.daviteixeira.carteiraclientes.factory.ConnectionFactory;
import br.com.daviteixeira.carteiraclientes.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario autenticar(String login, String senha) {
        String sql = """
            SELECT id, nome, cpf, login, senha, role, ativo
            FROM usuario
            WHERE login = ?
            AND senha = ?
            AND ativo = TRUE
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, login);
                stmt.setString(2, senha);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return preencherUsuario(rs);
                    }
                }
            }

            return null;

        } catch (Exception e) {
            System.out.println("Erro ao autenticar usuário: " + e.getMessage());
            throw new RuntimeException("Erro ao autenticar usuário.", e);
        }
    }

    public void salvar(Usuario usuario) {
        String sql = """
            INSERT INTO usuario (
                nome,
                cpf,
                login,
                senha,
                role,
                ativo
            ) VALUES (?, ?, ?, ?, ?, ?)
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, usuario.getNome());
                stmt.setString(2, usuario.getCpf());
                stmt.setString(3, usuario.getLogin());
                stmt.setString(4, usuario.getSenha());
                stmt.setString(5, usuario.getRole());
                stmt.setBoolean(6, true);

                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro ao salvar usuário: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar usuário.", e);
        }
    }

    public void atualizar(Usuario usuario, boolean atualizarSenha) {
        String sqlComSenha = """
            UPDATE usuario
            SET nome = ?,
                cpf = ?,
                login = ?,
                senha = ?,
                role = ?
            WHERE id = ?
        """;

        String sqlSemSenha = """
            UPDATE usuario
            SET nome = ?,
                cpf = ?,
                login = ?,
                role = ?
            WHERE id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            if (atualizarSenha) {
                try (PreparedStatement stmt = con.prepareStatement(sqlComSenha)) {
                    stmt.setString(1, usuario.getNome());
                    stmt.setString(2, usuario.getCpf());
                    stmt.setString(3, usuario.getLogin());
                    stmt.setString(4, usuario.getSenha());
                    stmt.setString(5, usuario.getRole());
                    stmt.setInt(6, usuario.getId());

                    stmt.executeUpdate();
                }
            } else {
                try (PreparedStatement stmt = con.prepareStatement(sqlSemSenha)) {
                    stmt.setString(1, usuario.getNome());
                    stmt.setString(2, usuario.getCpf());
                    stmt.setString(3, usuario.getLogin());
                    stmt.setString(4, usuario.getRole());
                    stmt.setInt(5, usuario.getId());

                    stmt.executeUpdate();
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar usuário.", e);
        }
    }

    public void excluir(int id) {
        String sql = """
            UPDATE usuario
            SET ativo = FALSE
            WHERE id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro ao excluir usuário: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir usuário.", e);
        }
    }

    public Usuario buscarPorId(int id) {
        String sql = """
            SELECT id, nome, cpf, login, senha, role, ativo
            FROM usuario
            WHERE id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return preencherUsuario(rs);
                    }
                }
            }

            return null;

        } catch (Exception e) {
            System.out.println("Erro ao buscar usuário por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar usuário por ID.", e);
        }
    }

    public List<Usuario> listarTodos() {
        String sql = """
            SELECT id, nome, cpf, login, senha, role, ativo
            FROM usuario
            WHERE ativo = TRUE
            ORDER BY nome
        """;

        List<Usuario> usuarios = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {
                while (rs.next()) {
                    usuarios.add(preencherUsuario(rs));
                }
            }

            return usuarios;

        } catch (Exception e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
            throw new RuntimeException("Erro ao listar usuários.", e);
        }
    }

    public List<Usuario> pesquisar(String termo) {
        String sql = """
            SELECT id, nome, cpf, login, senha, role, ativo
            FROM usuario
            WHERE ativo = TRUE
            AND (
                nome LIKE ?
                OR login LIKE ?
                OR cpf LIKE ?
            )
            ORDER BY nome
        """;

        List<Usuario> usuarios = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                String filtro = "%" + termo + "%";

                stmt.setString(1, filtro);
                stmt.setString(2, filtro);
                stmt.setString(3, filtro);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        usuarios.add(preencherUsuario(rs));
                    }
                }
            }

            return usuarios;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar usuários: " + e.getMessage());
            throw new RuntimeException("Erro ao pesquisar usuários.", e);
        }
    }

    private Usuario preencherUsuario(ResultSet rs) throws Exception {
        Usuario usuario = new Usuario();

        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setCpf(rs.getString("cpf"));
        usuario.setLogin(rs.getString("login"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setRole(rs.getString("role"));
        usuario.setAtivo(rs.getBoolean("ativo"));

        return usuario;
    }
}