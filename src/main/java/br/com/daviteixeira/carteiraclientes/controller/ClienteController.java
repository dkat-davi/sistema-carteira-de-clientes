package br.com.daviteixeira.carteiraclientes.controller;

import br.com.daviteixeira.carteiraclientes.dao.ClienteDAO;
import br.com.daviteixeira.carteiraclientes.dao.LojaDAO;
import br.com.daviteixeira.carteiraclientes.dao.UsuarioLojaDAO;
import br.com.daviteixeira.carteiraclientes.model.Cliente;
import br.com.daviteixeira.carteiraclientes.model.Loja;
import br.com.daviteixeira.carteiraclientes.model.Usuario;
import br.com.daviteixeira.carteiraclientes.util.SessaoUsuario;

import java.util.ArrayList;
import java.util.List;

public class ClienteController {

    private final ClienteDAO clienteDAO;
    private final LojaDAO lojaDAO;
    private final UsuarioLojaDAO usuarioLojaDAO;

    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
        this.lojaDAO = new LojaDAO();
        this.usuarioLojaDAO = new UsuarioLojaDAO();
    }

    public void salvar(Cliente cliente) {
        validar(cliente);

        cliente.setNome(normalizarTexto(cliente.getNome()));
        cliente.setCpf(normalizarTexto(cliente.getCpf()));
        cliente.setTelefone(normalizarTexto(cliente.getTelefone()));
        cliente.setEmail(normalizarTexto(cliente.getEmail()));
        cliente.setEndereco(normalizarTexto(cliente.getEndereco()));

        if (!SessaoUsuario.isAdmin() && cliente.getId() == 0) {
            Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();
            cliente.setVendedorId(usuarioLogado.getId());
        }

        if (cliente.getId() == 0) {
            clienteDAO.salvar(cliente);
        } else {
            validarPermissaoCliente(cliente.getId());
            preservarVendedorAtualParaUsuarioComum(cliente);
            clienteDAO.atualizar(cliente);
        }
    }

    public void excluir(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Selecione um cliente para excluir.");
        }

        validarPermissaoCliente(id);

        clienteDAO.excluir(id);
    }

    public Cliente buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido.");
        }

        Cliente cliente = clienteDAO.buscarPorId(id);

        if (cliente == null) {
            return null;
        }

        validarPermissaoCliente(cliente);

        return cliente;
    }

    public List<Cliente> listarClientes() {
        if (SessaoUsuario.isAdmin()) {
            return clienteDAO.listarTodos();
        }

        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();

        return clienteDAO.listarPorUsuarioComGerencia(usuarioLogado.getId());
    }

    public List<Cliente> pesquisar(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarClientes();
        }

        if (SessaoUsuario.isAdmin()) {
            return clienteDAO.pesquisarTodos(termo.trim());
        }

        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();

        return clienteDAO.pesquisarPorUsuarioComGerencia(termo.trim(), usuarioLogado.getId());
    }

    public List<Loja> listarLojasDisponiveis() {
        if (SessaoUsuario.isAdmin()) {
            return lojaDAO.listarTodas();
        }

        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();

        return usuarioLojaDAO.listarLojasPorUsuario(usuarioLogado.getId());
    }

    public List<Usuario> listarVendedoresPorLoja(int lojaId) {
        if (!SessaoUsuario.isAdmin()) {
            List<Usuario> vendedores = new ArrayList<>();
            vendedores.add(SessaoUsuario.getUsuarioLogado());
            return vendedores;
        }

        return usuarioLojaDAO.listarVendedoresPorLoja(lojaId);
    }

    private void validar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Dados do cliente não informados.");
        }

        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o nome do cliente.");
        }

        if (cliente.getLojaId() <= 0) {
            throw new IllegalArgumentException("Selecione uma loja.");
        }

        if (cliente.getVendedorId() <= 0 && SessaoUsuario.isAdmin()) {
            throw new IllegalArgumentException("Selecione um vendedor.");
        }

        if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty()) {
            if (cliente.getCpf().trim().length() != 11) {
                throw new IllegalArgumentException("O CPF deve conter 11 números.");
            }
        }

        if (cliente.getEmail() != null && !cliente.getEmail().trim().isEmpty()) {
            if (!cliente.getEmail().contains("@")) {
                throw new IllegalArgumentException("Informe um e-mail válido.");
            }
        }
    }

    private void validarPermissaoCliente(int clienteId) {
        Cliente cliente = clienteDAO.buscarPorId(clienteId);

        if (cliente != null) {
            validarPermissaoCliente(cliente);
        }
    }

    private void validarPermissaoCliente(Cliente cliente) {
        if (SessaoUsuario.isAdmin()) {
            return;
        }

        Usuario usuarioLogado = SessaoUsuario.getUsuarioLogado();

        boolean vendedorDoCliente = cliente.getVendedorId() == usuarioLogado.getId();
        boolean gerenteDaLoja = usuarioLojaDAO.isGerenteDaLoja(usuarioLogado.getId(), cliente.getLojaId());

        if (!vendedorDoCliente && !gerenteDaLoja) {
            throw new IllegalArgumentException("Você não possui permissão para acessar este cliente.");
        }
    }

    private void preservarVendedorAtualParaUsuarioComum(Cliente cliente) {
        if (SessaoUsuario.isAdmin()) {
            return;
        }

        Cliente clienteAtual = clienteDAO.buscarPorId(cliente.getId());

        if (clienteAtual != null) {
            cliente.setVendedorId(clienteAtual.getVendedorId());
        }
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return null;
        }

        return texto.trim();
    }
}
