package br.com.daviteixeira.carteiraclientes.controller;

import br.com.daviteixeira.carteiraclientes.dao.LojaDAO;
import br.com.daviteixeira.carteiraclientes.dao.UsuarioDAO;
import br.com.daviteixeira.carteiraclientes.dao.UsuarioLojaDAO;
import br.com.daviteixeira.carteiraclientes.model.Loja;
import br.com.daviteixeira.carteiraclientes.model.Usuario;
import br.com.daviteixeira.carteiraclientes.model.UsuarioLoja;

import java.util.List;

public class UsuarioLojaController {

    private final UsuarioLojaDAO usuarioLojaDAO;
    private final UsuarioDAO usuarioDAO;
    private final LojaDAO lojaDAO;

    public UsuarioLojaController() {
        this.usuarioLojaDAO = new UsuarioLojaDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.lojaDAO = new LojaDAO();
    }

    public void salvar(UsuarioLoja usuarioLoja) {
        validar(usuarioLoja);

        usuarioLoja.setCargo(usuarioLoja.getCargo().trim().toUpperCase());

        boolean jaExiste = usuarioLojaDAO.existeVinculo(
                usuarioLoja.getUsuarioId(),
                usuarioLoja.getLojaId(),
                usuarioLoja.getId()
        );

        if (jaExiste) {
            throw new IllegalArgumentException("Este usuário já está vinculado a esta loja.");
        }

        if (usuarioLoja.getId() == 0) {
            usuarioLojaDAO.salvar(usuarioLoja);
        } else {
            usuarioLojaDAO.atualizar(usuarioLoja);
        }
    }

    public void excluir(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Selecione um vínculo para excluir.");
        }

        usuarioLojaDAO.excluir(id);
    }

    public UsuarioLoja buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do vínculo inválido.");
        }

        return usuarioLojaDAO.buscarPorId(id);
    }

    public List<UsuarioLoja> listarTodos() {
        return usuarioLojaDAO.listarTodos();
    }

    public List<UsuarioLoja> pesquisar(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarTodos();
        }

        return usuarioLojaDAO.pesquisar(termo.trim());
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarTodos();
    }

    public List<Loja> listarLojas() {
        return lojaDAO.listarTodas();
    }

    private void validar(UsuarioLoja usuarioLoja) {
        if (usuarioLoja == null) {
            throw new IllegalArgumentException("Dados do vínculo não informados.");
        }

        if (usuarioLoja.getUsuarioId() <= 0) {
            throw new IllegalArgumentException("Selecione um usuário.");
        }

        if (usuarioLoja.getLojaId() <= 0) {
            throw new IllegalArgumentException("Selecione uma loja.");
        }

        if (usuarioLoja.getCargo() == null || usuarioLoja.getCargo().trim().isEmpty()) {
            throw new IllegalArgumentException("Selecione o cargo.");
        }

        String cargo = usuarioLoja.getCargo().trim().toUpperCase();

        if (!cargo.equals("GERENTE") && !cargo.equals("VENDEDOR")) {
            throw new IllegalArgumentException("Cargo inválido. Use GERENTE ou VENDEDOR.");
        }
    }
}