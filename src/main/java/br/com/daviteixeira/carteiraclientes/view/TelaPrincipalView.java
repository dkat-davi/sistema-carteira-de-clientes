package br.com.daviteixeira.carteiraclientes.view;

import br.com.daviteixeira.carteiraclientes.controller.RelatorioController;
import br.com.daviteixeira.carteiraclientes.model.Usuario;
import br.com.daviteixeira.carteiraclientes.util.SessaoUsuario;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JasperPrint;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TelaPrincipalView extends javax.swing.JFrame {

    private final RelatorioController relatorioController = new RelatorioController();

    public TelaPrincipalView() {
        initComponents();

        setLocationRelativeTo(null);
        configurarUsuarioLogado();
        configurarPermissoes();
        mostrarTelaInicial();
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

    private void mostrarTelaInicial() {
        setTitle("Sistema Carteira de Clientes");

        painelPrincipal.removeAll();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.add(lblBoasVindas, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    private void abrirTela(String titulo, JPanel tela) {
        setTitle("Sistema Carteira de Clientes - " + titulo);

        painelPrincipal.removeAll();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.add(tela, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
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
        clienteView.setOnClose(this::mostrarTelaInicial);

        abrirTela(
                "Cadastro de Clientes",
                clienteView.getPainelPrincipal()
        );
    }

    private void itemUsuariosActionPerformed(java.awt.event.ActionEvent evt) {
        UsuarioView usuarioView = new UsuarioView();
        usuarioView.setOnClose(this::mostrarTelaInicial);

        abrirTela(
                "Cadastro de Usuários",
                usuarioView.getPainelPrincipal()
        );
    }

    private void itemLojasActionPerformed(java.awt.event.ActionEvent evt) {
        LojaView lojaView = new LojaView();
        lojaView.setOnClose(this::mostrarTelaInicial);

        abrirTela(
                "Cadastro de Lojas",
                lojaView.getPainelPrincipal()
        );
    }

    private void itemUsuarioLojaActionPerformed(java.awt.event.ActionEvent evt) {
        UsuarioLojaView usuarioLojaView = new UsuarioLojaView();
        usuarioLojaView.setOnClose(this::mostrarTelaInicial);

        abrirTela(
                "Vínculo Usuário x Loja",
                usuarioLojaView.getPainelPrincipal()
        );
    }

    private void itemRegistrarAtendimentoActionPerformed(java.awt.event.ActionEvent evt) {
        AtendimentoView atendimentoView = new AtendimentoView();
        atendimentoView.setOnClose(this::mostrarTelaInicial);

        abrirTela(
                "Registro de Atendimentos",
                atendimentoView.getPainelPrincipal()
        );
    }

    private void itemClientesPorLojaActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            JasperPrint jasperPrint = relatorioController.gerarClientesPorLoja();

            RelatorioViewerView relatorioViewerView = new RelatorioViewerView(
                    "Relatório - Clientes por Loja",
                    jasperPrint
            );

            relatorioViewerView.setOnClose(this::mostrarTelaInicial);

            abrirTela(
                    "Relatório - Clientes por Loja",
                    relatorioViewerView
            );

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao gerar relatório de clientes por loja.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void itemClientesPorVendedorActionPerformed(java.awt.event.ActionEvent evt) {
         try {
            JasperPrint jasperPrint = relatorioController.gerarClientesPorVendedor();

            RelatorioViewerView relatorioViewerView = new RelatorioViewerView(
                "Relatório - Clientes por Vendedor",
                jasperPrint
            );

            relatorioViewerView.setOnClose(this::mostrarTelaInicial);

            abrirTela(
                "Relatório - Clientes por Vendedor",
                relatorioViewerView
            );

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                this,
                "Erro ao gerar relatório de clientes por vendedor.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void itemAtendimentosPorPeriodoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");    

            String dataInicioTexto = JOptionPane.showInputDialog(
                this,
                "Informe a data inicial no formato dd/MM/yyyy:",
                "Atendimentos por Período",
                JOptionPane.QUESTION_MESSAGE
            );

            if (dataInicioTexto == null || dataInicioTexto.trim().isEmpty()) {
                return;
            }

            String dataFimTexto = JOptionPane.showInputDialog(
                this,
                "Informe a data final no formato dd/MM/yyyy:",
                "Atendimentos por Período",
                JOptionPane.QUESTION_MESSAGE
            );

            if (dataFimTexto == null || dataFimTexto.trim().isEmpty()) {
                return;
            }

            LocalDate dataInicio = LocalDate.parse(dataInicioTexto.trim(), formatter);
            LocalDate dataFim = LocalDate.parse(dataFimTexto.trim(), formatter);

            LocalDateTime dataInicioCompleta = dataInicio.atStartOfDay();
            LocalDateTime dataFimCompleta = dataFim.atTime(23, 59, 59);

            JasperPrint jasperPrint = relatorioController.gerarAtendimentosPorPeriodo(
                dataInicioCompleta,
                dataFimCompleta
            );

            RelatorioViewerView relatorioViewerView = new RelatorioViewerView(
                "Relatório - Atendimentos por Período",
                jasperPrint
            );

            relatorioViewerView.setOnClose(this::mostrarTelaInicial);

            abrirTela(
                    "Relatório - Atendimentos por Período",
                    relatorioViewerView
            );

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Data inválida. Use o formato dd/MM/yyyy.",
                    "Validação",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao gerar relatório de atendimentos por período.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void itemUsuariosPorLojaActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            JasperPrint jasperPrint = relatorioController.gerarUsuariosPorLoja();

            RelatorioViewerView relatorioViewerView = new RelatorioViewerView(
                "Relatório - Usuários por Loja",
                jasperPrint
            );

            relatorioViewerView.setOnClose(this::mostrarTelaInicial);

            abrirTela(
                "Relatório - Usuários por Loja",
                relatorioViewerView
            );

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                this,
                "Erro ao gerar relatório de usuários por loja.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
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