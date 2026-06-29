package br.com.daviteixeira.carteiraclientes.controller;

import br.com.daviteixeira.carteiraclientes.dao.UsuarioDAO;
import br.com.daviteixeira.carteiraclientes.model.Usuario;

import java.util.List;

public class UsuarioController {

    private final UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public void salvar(Usuario usuario) {
        validar(usuario);

        usuario.setNome(usuario.getNome().trim());
        usuario.setLogin(usuario.getLogin().trim());

        if (usuario.getCpf() != null) {
            usuario.setCpf(usuario.getCpf().trim());
        }

        usuario.setRole(usuario.getRole().trim().toUpperCase());

        if (usuario.getId() == 0) {
            if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
                throw new IllegalArgumentException("Informe a senha do usuário.");
            }

            usuarioDAO.salvar(usuario);
        } else {
            boolean atualizarSenha = usuario.getSenha() != null && !usuario.getSenha().trim().isEmpty();
            usuarioDAO.atualizar(usuario, atualizarSenha);
        }
    }

    public void excluir(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Selecione um usuário para excluir.");
        }

        usuarioDAO.excluir(id);
    }

    public Usuario buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do usuário inválido.");
        }

        return usuarioDAO.buscarPorId(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    public List<Usuario> pesquisar(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarTodos();
        }

        return usuarioDAO.pesquisar(termo.trim());
    }

    private void validar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Dados do usuário não informados.");
        }

        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o nome do usuário.");
        }

        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o login do usuário.");
        }

        if (usuario.getRole() == null || usuario.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o perfil do usuário.");
        }

        String role = usuario.getRole().trim().toUpperCase();

        if (!role.equals("ADMIN") && !role.equals("USUARIO")) {
            throw new IllegalArgumentException("Perfil inválido. Use ADMIN ou USUARIO.");
        }

        if (usuario.getCpf() != null && !usuario.getCpf().trim().isEmpty()) {
            if (usuario.getCpf().trim().length() != 11) {
                throw new IllegalArgumentException("O CPF deve conter 11 números.");
            }
        }
    }
}