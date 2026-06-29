package br.com.daviteixeira.carteiraclientes;

import br.com.daviteixeira.carteiraclientes.view.LoginView;
import javax.swing.SwingUtilities;

/**
 *
 * @author dkat
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}


