package br.com.daviteixeira.carteiraclientes.controller;

import br.com.daviteixeira.carteiraclientes.dao.AtendimentoDAO;
import br.com.daviteixeira.carteiraclientes.dao.ClienteDAO;
import br.com.daviteixeira.carteiraclientes.model.Atendimento;
import br.com.daviteixeira.carteiraclientes.model.Cliente;
import br.com.daviteixeira.carteiraclientes.model.Usuario;
import br.com.daviteixeira.carteiraclientes.util.SessaoUsuario;

import java.util.List;

public class AtendimentoController {

    private final AtendimentoDAO atendimentoDAO;
    private final ClienteDAO clienteDAO;

    public AtendimentoController() {
        this.atendimentoDAO = new AtendimentoDAO();
        this.clienteDAO = new ClienteDAO();
    }

    public void salvar(Atendimento atendimento) {
        validar(atendimento);
        validarPermissaoCliente(atendimento.getClienteId());

        atendimento.setDescricao(atendimento.getDescricao().trim());

        if (atendimento.getId() == 0) {
            Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();
            atendimento.setUsuarioId(usuarioLogado.getId());

            atendimentoDAO.salvar(atendimento);
        } else {
            validarPermissaoAtendimento(atendimento.getId());
            atendimentoDAO.atualizar(atendimento);
        }
    }

    public void excluir(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Selecione um atendimento para excluir.");
        }

        validarPermissaoAtendimento(id);

        atendimentoDAO.excluir(id);
    }

    public Atendimento buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do atendimento inválido.");
        }

        Atendimento atendimento = atendimentoDAO.buscarPorId(id);

        if (atendimento == null) {
            return null;
        }

        validarPermissaoAtendimento(atendimento);

        return atendimento;
    }

    public List<Atendimento> listarAtendimentos() {
        if (SessaoUsuario.isAdmin()) {
            return atendimentoDAO.listarTodos();
        }

        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();

        return atendimentoDAO.listarPorVendedor(usuarioLogado.getId());
    }

    public List<Atendimento> pesquisar(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarAtendimentos();
        }

        if (SessaoUsuario.isAdmin()) {
            return atendimentoDAO.pesquisarTodos(termo.trim());
        }

        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();

        return atendimentoDAO.pesquisarPorVendedor(termo.trim(), usuarioLogado.getId());
    }

    public List<Cliente> listarClientesDisponiveis() {
        if (SessaoUsuario.isAdmin()) {
            return clienteDAO.listarTodos();
        }

        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();

        return clienteDAO.listarPorVendedor(usuarioLogado.getId());
    }

    private void validar(Atendimento atendimento) {
        if (atendimento == null) {
            throw new IllegalArgumentException("Dados do atendimento não informados.");
        }

        if (atendimento.getClienteId() <= 0) {
            throw new IllegalArgumentException("Selecione um cliente.");
        }

        if (atendimento.getDescricao() == null || atendimento.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a descrição do atendimento.");
        }
    }

    private void validarPermissaoCliente(int clienteId) {
        Cliente cliente = clienteDAO.buscarPorId(clienteId);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        if (SessaoUsuario.isAdmin()) {
            return;
        }

        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();

        if (cliente.getVendedorId() != usuarioLogado.getId()) {
            throw new IllegalArgumentException("Você não possui permissão para registrar atendimento para este cliente.");
        }
    }

    private void validarPermissaoAtendimento(int atendimentoId) {
        Atendimento atendimento = atendimentoDAO.buscarPorId(atendimentoId);

        if (atendimento != null) {
            validarPermissaoAtendimento(atendimento);
        }
    }

    private void validarPermissaoAtendimento(Atendimento atendimento) {
        if (SessaoUsuario.isAdmin()) {
            return;
        }

        Cliente cliente = clienteDAO.buscarPorId(atendimento.getClienteId());

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente do atendimento não encontrado.");
        }

        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();

        if (cliente.getVendedorId() != usuarioLogado.getId()) {
            throw new IllegalArgumentException("Você não possui permissão para acessar este atendimento.");
        }
    }
}