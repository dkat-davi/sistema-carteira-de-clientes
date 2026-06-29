package br.com.daviteixeira.carteiraclientes.dao;

import br.com.daviteixeira.carteiraclientes.factory.ConnectionFactory;
import br.com.daviteixeira.carteiraclientes.model.UsuarioLoja;
import br.com.daviteixeira.carteiraclientes.model.Loja;
import br.com.daviteixeira.carteiraclientes.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioLojaDAO {

    public void salvar(UsuarioLoja usuarioLoja) {
        String sql = """
            INSERT INTO usuario_loja (
                usuario_id,
                loja_id,
                cargo
            ) VALUES (?, ?, ?)
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, usuarioLoja.getUsuarioId());
                stmt.setInt(2, usuarioLoja.getLojaId());
                stmt.setString(3, usuarioLoja.getCargo());

                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro ao salvar vínculo usuário x loja: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar vínculo usuário x loja.", e);
        }
    }

    public void atualizar(UsuarioLoja usuarioLoja) {
        String sql = """
            UPDATE usuario_loja
            SET usuario_id = ?,
                loja_id = ?,
                cargo = ?
            WHERE id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, usuarioLoja.getUsuarioId());
                stmt.setInt(2, usuarioLoja.getLojaId());
                stmt.setString(3, usuarioLoja.getCargo());
                stmt.setInt(4, usuarioLoja.getId());

                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro ao atualizar vínculo usuário x loja: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar vínculo usuário x loja.", e);
        }
    }

    public void excluir(int id) {
        String sql = """
            DELETE FROM usuario_loja
            WHERE id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro ao excluir vínculo usuário x loja: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir vínculo usuário x loja.", e);
        }
    }

    public UsuarioLoja buscarPorId(int id) {
        String sql = """
            SELECT 
                ul.id,
                ul.usuario_id,
                ul.loja_id,
                ul.cargo,
                u.nome AS usuario_nome,
                l.nome AS loja_nome
            FROM usuario_loja ul
            INNER JOIN usuario u ON u.id = ul.usuario_id
            INNER JOIN loja l ON l.id = ul.loja_id
            WHERE ul.id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return preencherUsuarioLoja(rs);
                    }
                }
            }

            return null;

        } catch (Exception e) {
            System.out.println("Erro ao buscar vínculo por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar vínculo por ID.", e);
        }
    }

    public List<UsuarioLoja> listarTodos() {
        String sql = """
            SELECT 
                ul.id,
                ul.usuario_id,
                ul.loja_id,
                ul.cargo,
                u.nome AS usuario_nome,
                l.nome AS loja_nome
            FROM usuario_loja ul
            INNER JOIN usuario u ON u.id = ul.usuario_id
            INNER JOIN loja l ON l.id = ul.loja_id
            WHERE u.ativo = TRUE
            AND l.ativo = TRUE
            ORDER BY l.nome, u.nome
        """;

        List<UsuarioLoja> vinculos = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {
                while (rs.next()) {
                    vinculos.add(preencherUsuarioLoja(rs));
                }
            }

            return vinculos;

        } catch (Exception e) {
            System.out.println("Erro ao listar vínculos usuário x loja: " + e.getMessage());
            throw new RuntimeException("Erro ao listar vínculos usuário x loja.", e);
        }
    }

    public List<UsuarioLoja> pesquisar(String termo) {
        String sql = """
            SELECT 
                ul.id,
                ul.usuario_id,
                ul.loja_id,
                ul.cargo,
                u.nome AS usuario_nome,
                l.nome AS loja_nome
            FROM usuario_loja ul
            INNER JOIN usuario u ON u.id = ul.usuario_id
            INNER JOIN loja l ON l.id = ul.loja_id
            WHERE u.ativo = TRUE
            AND l.ativo = TRUE
            AND (
                u.nome LIKE ?
                OR l.nome LIKE ?
                OR ul.cargo LIKE ?
            )
            ORDER BY l.nome, u.nome
        """;

        List<UsuarioLoja> vinculos = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                String filtro = "%" + termo + "%";

                stmt.setString(1, filtro);
                stmt.setString(2, filtro);
                stmt.setString(3, filtro);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        vinculos.add(preencherUsuarioLoja(rs));
                    }
                }
            }

            return vinculos;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar vínculos usuário x loja: " + e.getMessage());
            throw new RuntimeException("Erro ao pesquisar vínculos usuário x loja.", e);
        }
    }

    public boolean existeVinculo(int usuarioId, int lojaId, int idIgnorado) {
        String sql = """
            SELECT COUNT(*) AS total
            FROM usuario_loja
            WHERE usuario_id = ?
            AND loja_id = ?
            AND id <> ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, usuarioId);
                stmt.setInt(2, lojaId);
                stmt.setInt(3, idIgnorado);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("total") > 0;
                    }
                }
            }

            return false;

        } catch (Exception e) {
            System.out.println("Erro ao verificar vínculo existente: " + e.getMessage());
            throw new RuntimeException("Erro ao verificar vínculo existente.", e);
        }
    }

    private UsuarioLoja preencherUsuarioLoja(ResultSet rs) throws Exception {
        UsuarioLoja usuarioLoja = new UsuarioLoja();

        usuarioLoja.setId(rs.getInt("id"));
        usuarioLoja.setUsuarioId(rs.getInt("usuario_id"));
        usuarioLoja.setLojaId(rs.getInt("loja_id"));
        usuarioLoja.setCargo(rs.getString("cargo"));
        usuarioLoja.setUsuarioNome(rs.getString("usuario_nome"));
        usuarioLoja.setLojaNome(rs.getString("loja_nome"));

        return usuarioLoja;
    }
    
    public List<Usuario> listarVendedoresPorLoja(int lojaId) {
    String sql = """
        SELECT 
            u.id,
            u.nome,
            u.cpf,
            u.login,
            u.senha,
            u.role,
            u.ativo
        FROM usuario_loja ul
        INNER JOIN usuario u ON u.id = ul.usuario_id
        INNER JOIN loja l ON l.id = ul.loja_id
        WHERE ul.loja_id = ?
        AND ul.cargo = 'VENDEDOR'
        AND u.ativo = TRUE
        AND l.ativo = TRUE
        ORDER BY u.nome
    """;

    List<Usuario> vendedores = new ArrayList<>();

    try {
        Connection con = ConnectionFactory.getInstance().getConnection();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, lojaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();

                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setCpf(rs.getString("cpf"));
                    usuario.setLogin(rs.getString("login"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setRole(rs.getString("role"));
                    usuario.setAtivo(rs.getBoolean("ativo"));

                    vendedores.add(usuario);
                }
            }
        }

        return vendedores;

    } catch (Exception e) {
        System.out.println("Erro ao listar vendedores por loja: " + e.getMessage());
        throw new RuntimeException("Erro ao listar vendedores por loja.", e);
    }
}

    public List<Loja> listarLojasPorUsuario(int usuarioId) {
        String sql = """
            SELECT 
                l.id,
                l.nome,
                l.telefone,
                l.endereco,
                l.cidade,
                l.estado,
                l.ativo
            FROM usuario_loja ul
            INNER JOIN loja l ON l.id = ul.loja_id
            WHERE ul.usuario_id = ?
            AND l.ativo = TRUE
            ORDER BY l.nome
        """;

        List<Loja> lojas = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, usuarioId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Loja loja = new Loja();

                        loja.setId(rs.getInt("id"));
                        loja.setNome(rs.getString("nome"));
                        loja.setTelefone(rs.getString("telefone"));
                        loja.setEndereco(rs.getString("endereco"));
                        loja.setCidade(rs.getString("cidade"));
                        loja.setEstado(rs.getString("estado"));
                        loja.setAtivo(rs.getBoolean("ativo"));

                        lojas.add(loja);
                    }
                }
            }

            return lojas;

        } catch (Exception e) {
            System.out.println("Erro ao listar lojas por usuário: " + e.getMessage());
            throw new RuntimeException("Erro ao listar lojas por usuário.", e);
        }
    }
}