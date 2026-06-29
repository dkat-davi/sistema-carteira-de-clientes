package br.com.daviteixeira.carteiraclientes.dao;

import br.com.daviteixeira.carteiraclientes.factory.ConnectionFactory;
import br.com.daviteixeira.carteiraclientes.model.Atendimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AtendimentoDAO {

    private static final String SELECT_ATENDIMENTO = """
        SELECT
            a.id,
            a.cliente_id,
            a.usuario_id,
            a.data_atendimento,
            a.descricao,
            c.nome AS cliente_nome,
            u.nome AS usuario_nome
        FROM atendimento a
        INNER JOIN cliente c ON c.id = a.cliente_id
        INNER JOIN usuario u ON u.id = a.usuario_id
    """;

    public void salvar(Atendimento atendimento) {
        String sql = """
            INSERT INTO atendimento (
                cliente_id,
                usuario_id,
                descricao
            ) VALUES (?, ?, ?)
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, atendimento.getClienteId());
                stmt.setInt(2, atendimento.getUsuarioId());
                stmt.setString(3, atendimento.getDescricao());

                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro ao salvar atendimento: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar atendimento.", e);
        }
    }

    public void atualizar(Atendimento atendimento) {
        String sql = """
            UPDATE atendimento
            SET cliente_id = ?,
                descricao = ?
            WHERE id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, atendimento.getClienteId());
                stmt.setString(2, atendimento.getDescricao());
                stmt.setInt(3, atendimento.getId());

                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro ao atualizar atendimento: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar atendimento.", e);
        }
    }

    public void excluir(int id) {
        String sql = """
            DELETE FROM atendimento
            WHERE id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Erro ao excluir atendimento: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir atendimento.", e);
        }
    }

    public Atendimento buscarPorId(int id) {
        String sql = SELECT_ATENDIMENTO + """
            WHERE a.id = ?
        """;

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return preencherAtendimento(rs);
                    }
                }
            }

            return null;

        } catch (Exception e) {
            System.out.println("Erro ao buscar atendimento por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar atendimento por ID.", e);
        }
    }

    public List<Atendimento> listarTodos() {
        String sql = SELECT_ATENDIMENTO + """
            ORDER BY a.data_atendimento DESC
        """;

        List<Atendimento> atendimentos = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {
                while (rs.next()) {
                    atendimentos.add(preencherAtendimento(rs));
                }
            }

            return atendimentos;

        } catch (Exception e) {
            System.out.println("Erro ao listar atendimentos: " + e.getMessage());
            throw new RuntimeException("Erro ao listar atendimentos.", e);
        }
    }

    public List<Atendimento> listarPorVendedor(int vendedorId) {
        String sql = SELECT_ATENDIMENTO + """
            WHERE c.vendedor_id = ?
            ORDER BY a.data_atendimento DESC
        """;

        List<Atendimento> atendimentos = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, vendedorId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        atendimentos.add(preencherAtendimento(rs));
                    }
                }
            }

            return atendimentos;

        } catch (Exception e) {
            System.out.println("Erro ao listar atendimentos por vendedor: " + e.getMessage());
            throw new RuntimeException("Erro ao listar atendimentos por vendedor.", e);
        }
    }

    public List<Atendimento> pesquisarTodos(String termo) {
        String sql = SELECT_ATENDIMENTO + """
            WHERE c.nome LIKE ?
            OR u.nome LIKE ?
            OR a.descricao LIKE ?
            ORDER BY a.data_atendimento DESC
        """;

        List<Atendimento> atendimentos = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                String filtro = "%" + termo + "%";

                stmt.setString(1, filtro);
                stmt.setString(2, filtro);
                stmt.setString(3, filtro);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        atendimentos.add(preencherAtendimento(rs));
                    }
                }
            }

            return atendimentos;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar atendimentos: " + e.getMessage());
            throw new RuntimeException("Erro ao pesquisar atendimentos.", e);
        }
    }

    public List<Atendimento> pesquisarPorVendedor(String termo, int vendedorId) {
        String sql = SELECT_ATENDIMENTO + """
            WHERE c.vendedor_id = ?
            AND (
                c.nome LIKE ?
                OR u.nome LIKE ?
                OR a.descricao LIKE ?
            )
            ORDER BY a.data_atendimento DESC
        """;

        List<Atendimento> atendimentos = new ArrayList<>();

        try {
            Connection con = ConnectionFactory.getInstance().getConnection();

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                String filtro = "%" + termo + "%";

                stmt.setInt(1, vendedorId);
                stmt.setString(2, filtro);
                stmt.setString(3, filtro);
                stmt.setString(4, filtro);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        atendimentos.add(preencherAtendimento(rs));
                    }
                }
            }

            return atendimentos;

        } catch (Exception e) {
            System.out.println("Erro ao pesquisar atendimentos por vendedor: " + e.getMessage());
            throw new RuntimeException("Erro ao pesquisar atendimentos por vendedor.", e);
        }
    }

    private Atendimento preencherAtendimento(ResultSet rs) throws Exception {
        Atendimento atendimento = new Atendimento();

        atendimento.setId(rs.getInt("id"));
        atendimento.setClienteId(rs.getInt("cliente_id"));
        atendimento.setUsuarioId(rs.getInt("usuario_id"));
        atendimento.setDescricao(rs.getString("descricao"));
        atendimento.setClienteNome(rs.getString("cliente_nome"));
        atendimento.setUsuarioNome(rs.getString("usuario_nome"));

        Timestamp dataAtendimento = rs.getTimestamp("data_atendimento");

        if (dataAtendimento != null) {
            atendimento.setDataAtendimento(dataAtendimento.toLocalDateTime());
        }

        return atendimento;
    }
}