package br.com.daviteixeira.carteiraclientes.view;

import br.com.daviteixeira.carteiraclientes.model.Usuario;
import br.com.daviteixeira.carteiraclientes.util.SessaoUsuario;
import javax.swing.JOptionPane;

public class TelaPrincipalView extends javax.swing.JFrame {

    public TelaPrincipalView() {
        initComponents();

        setLocationRelativeTo(null);
        configurarUsuarioLogado();
        configurarPermissoes();
    }

    private void configurarUsuarioLogado() {
        Usuario usuario = SessaoUsuario.getUsuarioLogado();

        if (usuario == null) {
            lblBoasVindas.setText("Sessão não encontrada.");
            return;
        }

        lblBoasVindas.setText(
                "Bem-vindo, " + usuario.getNome() + " | Perfil: " + usuario.getRole()
        );
    }

    private void configurarPermissoes() {
        boolean admin = SessaoUsuario.isAdmin();

        itemUsuarios.setVisible(admin);
        itemLojas.setVisible(admin);
        itemUsuarioLoja.setVisible(admin);
        menuRelatorios.setVisible(admin);
    }

    private void mostrarModuloEmDesenvolvimento(String modulo) {
        JOptionPane.showMessageDialog(
                this,
                "Módulo de " + modulo + " será desenvolvido nas próximas etapas.",
                "Em desenvolvimento",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void sairDoSistema() {
        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair do sistema?",
                "Sair",
                JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {
            SessaoUsuario.encerrarSessao();

            LoginView loginView = new LoginView();
            loginView.setVisible(true);

            dispose();
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        painelPrincipal = new javax.swing.JPanel();
        lblBoasVindas = new javax.swing.JLabel();
        menuBarPrincipal = new javax.swing.JMenuBar();
        menuCadastros = new javax.swing.JMenu();
        itemClientes = new javax.swing.JMenuItem();
        itemUsuarios = new javax.swing.JMenuItem();
        itemLojas = new javax.swing.JMenuItem();
        itemUsuarioLoja = new javax.swing.JMenuItem();
        menuAtendimentos = new javax.swing.JMenu();
        itemRegistrarAtendimento = new javax.swing.JMenuItem();
        menuRelatorios = new javax.swing.JMenu();
        itemClientesPorLoja = new javax.swing.JMenuItem();
        itemClientesPorVendedor = new javax.swing.JMenuItem();
        itemAtendimentosPorPeriodo = new javax.swing.JMenuItem();
        itemUsuariosPorLoja = new javax.swing.JMenuItem();
        menuSistema = new javax.swing.JMenu();
        itemSair = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema Carteira de Clientes");

        lblBoasVindas.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 22));
        lblBoasVindas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBoasVindas.setText("Bem-vindo ao Sistema Carteira de Clientes");

        javax.swing.GroupLayout painelPrincipalLayout = new javax.swing.GroupLayout(painelPrincipal);
        painelPrincipal.setLayout(painelPrincipalLayout);
        painelPrincipalLayout.setHorizontalGroup(
                painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(painelPrincipalLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblBoasVindas, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                                .addContainerGap())
        );
        painelPrincipalLayout.setVerticalGroup(
                painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(painelPrincipalLayout.createSequentialGroup()
                                .addGap(220, 220, 220)
                                .addComponent(lblBoasVindas)
                                .addContainerGap(295, Short.MAX_VALUE))
        );

        menuCadastros.setText("Cadastros");

        itemClientes.setText("Clientes");
        itemClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemClientesActionPerformed(evt);
            }
        });
        menuCadastros.add(itemClientes);

        itemUsuarios.setText("Usuários");
        itemUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemUsuariosActionPerformed(evt);
            }
        });
        menuCadastros.add(itemUsuarios);

        itemLojas.setText("Lojas");
        itemLojas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemLojasActionPerformed(evt);
            }
        });
        menuCadastros.add(itemLojas);

        itemUsuarioLoja.setText("Vincular Usuário x Loja");
        itemUsuarioLoja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemUsuarioLojaActionPerformed(evt);
            }
        });
        menuCadastros.add(itemUsuarioLoja);

        menuBarPrincipal.add(menuCadastros);

        menuAtendimentos.setText("Atendimentos");

        itemRegistrarAtendimento.setText("Registrar Atendimento");
        itemRegistrarAtendimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemRegistrarAtendimentoActionPerformed(evt);
            }
        });
        menuAtendimentos.add(itemRegistrarAtendimento);

        menuBarPrincipal.add(menuAtendimentos);

        menuRelatorios.setText("Relatórios");

        itemClientesPorLoja.setText("Clientes por Loja");
        itemClientesPorLoja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemClientesPorLojaActionPerformed(evt);
            }
        });
        menuRelatorios.add(itemClientesPorLoja);

        itemClientesPorVendedor.setText("Clientes por Vendedor");
        itemClientesPorVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemClientesPorVendedorActionPerformed(evt);
            }
        });
        menuRelatorios.add(itemClientesPorVendedor);

        itemAtendimentosPorPeriodo.setText("Atendimentos por Período");
        itemAtendimentosPorPeriodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAtendimentosPorPeriodoActionPerformed(evt);
            }
        });
        menuRelatorios.add(itemAtendimentosPorPeriodo);

        itemUsuariosPorLoja.setText("Usuários por Loja");
        itemUsuariosPorLoja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemUsuariosPorLojaActionPerformed(evt);
            }
        });
        menuRelatorios.add(itemUsuariosPorLoja);

        menuBarPrincipal.add(menuRelatorios);

        menuSistema.setText("Sistema");

        itemSair.setText("Sair");
        itemSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemSairActionPerformed(evt);
            }
        });
        menuSistema.add(itemSair);

        menuBarPrincipal.add(menuSistema);

        setJMenuBar(menuBarPrincipal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(painelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(painelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void itemClientesActionPerformed(java.awt.event.ActionEvent evt) {
        ClienteView clienteView = new ClienteView();
        clienteView.setVisible(true);
    }

    private void itemUsuariosActionPerformed(java.awt.event.ActionEvent evt) {
         UsuarioView usuarioView = new UsuarioView();
        usuarioView.setVisible(true);
    }

    private void itemLojasActionPerformed(java.awt.event.ActionEvent evt) {
        LojaView lojaView = new LojaView();
        lojaView.setVisible(true);
    }

    private void itemUsuarioLojaActionPerformed(java.awt.event.ActionEvent evt) {
        UsuarioLojaView usuarioLojaView = new UsuarioLojaView();
        usuarioLojaView.setVisible(true);
    }

    private void itemRegistrarAtendimentoActionPerformed(java.awt.event.ActionEvent evt) {
        AtendimentoView atendimentoView = new AtendimentoView();
        atendimentoView.setVisible(true);
    }

    private void itemClientesPorLojaActionPerformed(java.awt.event.ActionEvent evt) {
        mostrarModuloEmDesenvolvimento("Relatório de Clientes por Loja");
    }

    private void itemClientesPorVendedorActionPerformed(java.awt.event.ActionEvent evt) {
        mostrarModuloEmDesenvolvimento("Relatório de Clientes por Vendedor");
    }

    private void itemAtendimentosPorPeriodoActionPerformed(java.awt.event.ActionEvent evt) {
        mostrarModuloEmDesenvolvimento("Relatório de Atendimentos por Período");
    }

    private void itemUsuariosPorLojaActionPerformed(java.awt.event.ActionEvent evt) {
        mostrarModuloEmDesenvolvimento("Relatório de Usuários por Loja");
    }

    private void itemSairActionPerformed(java.awt.event.ActionEvent evt) {
        sairDoSistema();
    }

    private javax.swing.JMenuItem itemAtendimentosPorPeriodo;
    private javax.swing.JMenuItem itemClientes;
    private javax.swing.JMenuItem itemClientesPorLoja;
    private javax.swing.JMenuItem itemClientesPorVendedor;
    private javax.swing.JMenuItem itemLojas;
    private javax.swing.JMenuItem itemRegistrarAtendimento;
    private javax.swing.JMenuItem itemSair;
    private javax.swing.JMenuItem itemUsuarioLoja;
    private javax.swing.JMenuItem itemUsuarios;
    private javax.swing.JMenuItem itemUsuariosPorLoja;
    private javax.swing.JLabel lblBoasVindas;
    private javax.swing.JMenuBar menuBarPrincipal;
    private javax.swing.JMenu menuAtendimentos;
    private javax.swing.JMenu menuCadastros;
    private javax.swing.JMenu menuRelatorios;
    private javax.swing.JMenu menuSistema;
    private javax.swing.JPanel painelPrincipal;
}