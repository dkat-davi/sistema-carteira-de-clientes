package br.com.daviteixeira.carteiraclientes.controller;

import br.com.daviteixeira.carteiraclientes.dao.UsuarioDAO;
import br.com.daviteixeira.carteiraclientes.model.Usuario;
import br.com.daviteixeira.carteiraclientes.util.SessaoUsuario;

public class LoginController {

    private final UsuarioDAO usuarioDAO;

    public LoginController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario autenticar(String login, String senha) {
        validarCampos(login, senha);

        Usuario usuario = usuarioDAO.autenticar(login.trim(), senha);

        if (usuario != null) {
            SessaoUsuario.iniciarSessao(usuario);
        }

        return usuario;
    }

    private void validarCampos(String login, String senha) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o login.");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a senha.");
        }
    }
}