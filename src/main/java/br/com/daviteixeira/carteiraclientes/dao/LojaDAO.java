package br.com.daviteixeira.carteiraclientes.dao;

import br.com.daviteixeira.carteiraclientes.factory.ConnectionFactory;
import br.com.daviteixeira.carteiraclientes.model.Loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LojaDAO {

    public void salvar(Loja loja) {
        String sql = """
            INSERT INTO loja (
                nome,
                telefone,
                endereco,
                cidade,
                estado,
                ativo
            ) VALUES (?, ?, ?, ?, ?, ?)
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, loja.getNome());
            stmt.setString(2, loja.getTelefone());
            stmt.setString(3, loja.getEndereco());
            stmt.setString(4, loja.getCidade());
            stmt.setString(5, loja.getEstado());
            stmt.setBoolean(6, true);

            stmt.executeUpdate();

            stmt.close();

        } catch (Exception e) {
            System.out.println("Erro ao salvar loja: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar loja.", e);
        }
    }

    public void atualizar(Loja loja) {
        String sql = """
            UPDATE loja
            SET nome = ?,
                telefone = ?,
                endereco = ?,
                cidade = ?,
                estado = ?
            WHERE id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, loja.getNome());
            stmt.setString(2, loja.getTelefone());
            stmt.setString(3, loja.getEndereco());
            stmt.setString(4, loja.getCidade());
            stmt.setString(5, loja.getEstado());
            stmt.setInt(6, loja.getId());

            stmt.executeUpdate();

            stmt.close();

        } catch (Exception e) {
            System.out.println("Erro ao atualizar loja: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar loja.", e);
        }
    }

    public void excluir(int id) {
        String sql = """
            UPDATE loja
            SET ativo = FALSE
            WHERE id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            stmt.executeUpdate();

            stmt.close();

        } catch (Exception e) {
            System.out.println("Erro ao excluir loja: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir loja.", e);
        }
    }

    public Loja buscarPorId(int id) {
        String sql = """
            SELECT id, nome, telefone, endereco, cidade, estado, ativo
            FROM loja
            WHERE id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            Loja loja = null;

            if (rs.next()) {
                loja = preencherLoja(rs);
            }

            rs.close();
            stmt.close();

            return loja;

        } catch (Exception e) {
            System.out.println("Erro ao buscar loja por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar loja por ID.", e);
        }
    }

    public List<Loja> listarTodas() {
        String sql = """
            SELECT id, nome, telefone, endereco, cidade, estado, ativo
            FROM loja
            WHERE ativo = TRUE
            ORDER BY nome
        """;

        List<Loja> lojas = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lojas.add(preencherLoja(rs));
            }

            rs.close();
            stmt.close();

            return lojas;

        } catch (Exception e) {
            System.out.println("Erro ao listar lojas: " + e.getMessage());
            throw new RuntimeException("Erro ao listar lojas.", e);
        }
    }

    public List<Loja> pesquisarPorNome(String nome) {
        String sql = """
            SELECT id, nome, telefone, endereco, cidade, estado, ativo
            FROM loja
            WHERE ativo = TRUE
            AND nome LIKE ?
            ORDER BY nome
        """;

        List<Loja> lojas = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lojas.add(preencherLoja(rs));
            }

            rs.close();
            stmt.close();

            return lojas;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar lojas: " + e.getMessage());
            throw new RuntimeException("Erro ao pesquisar lojas.", e);
        }
    }

    private Loja preencherLoja(ResultSet rs) throws Exception {
        Loja loja = new Loja();

        loja.setId(rs.getInt("id"));
        loja.setNome(rs.getString("nome"));
        loja.setTelefone(rs.getString("telefone"));
        loja.setEndereco(rs.getString("endereco"));
        loja.setCidade(rs.getString("cidade"));
        loja.setEstado(rs.getString("estado"));
        loja.setAtivo(rs.getBoolean("ativo"));

        return loja;
    }
}