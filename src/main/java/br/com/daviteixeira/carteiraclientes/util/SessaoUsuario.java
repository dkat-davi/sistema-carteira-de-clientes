package br.com.daviteixeira.carteiraclientes.util;

import br.com.daviteixeira.carteiraclientes.model.Usuario;

public class SessaoUsuario {

    private static Usuario usuarioLogado;

    private SessaoUsuario() {
    }

    public static void iniciarSessao(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean isUsuarioLogado() {
        return usuarioLogado != null;
    }

    public static boolean isAdmin() {
        return usuarioLogado != null && usuarioLogado.isAdmin();
    }

    public static void encerrarSessao() {
        usuarioLogado = null;
    }
}