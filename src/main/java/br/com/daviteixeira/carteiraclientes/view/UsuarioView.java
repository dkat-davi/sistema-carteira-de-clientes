package br.com.daviteixeira.carteiraclientes.view;

import br.com.daviteixeira.carteiraclientes.controller.UsuarioController;
import br.com.daviteixeira.carteiraclientes.model.Usuario;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class UsuarioView extends javax.swing.JDialog {

    private final UsuarioController usuarioController = new UsuarioController();
    private Runnable onClose;

    public UsuarioView() {
        this(null);
    }

    public UsuarioView(java.awt.Frame parent) {
        super(parent, false);
        initComponents();
        configurarTela();
    }

    public javax.swing.JPanel getPainelPrincipal() {
        return painelPrincipal;
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    private void configurarTela() {
        setLocationRelativeTo(null);
        txtId.setEditable(false);
        carregarTabela();
        comboRole.setSelectedItem("USUARIO");
    }

    private void salvarUsuario() {
        try {
            Usuario usuario = new Usuario();

            usuario.setId(obterIdSelecionado());
            usuario.setNome(txtNome.getText());
            usuario.setCpf(txtCpf.getText());
            usuario.setLogin(txtLogin.getText());
            usuario.setSenha(new String(txtSenha.getPassword()));
            usuario.setRole(comboRole.getSelectedItem().toString());
            usuario.setAtivo(true);

            usuarioController.salvar(usuario);

            JOptionPane.showMessageDialog(
                    this,
                    "Usuário salvo com sucesso.",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limparCampos();
            carregarTabela();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Validação",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao salvar usuário.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void excluirUsuario() {
        try {
            int id = obterIdSelecionado();

            if (id <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um usuário para excluir.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir este usuário?",
                    "Confirmar exclusão",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcao == JOptionPane.YES_OPTION) {
                usuarioController.excluir(id);

                JOptionPane.showMessageDialog(
                        this,
                        "Usuário excluído com sucesso.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                limparCampos();
                carregarTabela();
            }

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao excluir usuário.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void pesquisarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioController.pesquisar(txtPesquisa.getText());
            preencherTabela(usuarios);

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao pesquisar usuários.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void carregarTabela() {
        List<Usuario> usuarios = usuarioController.listarTodos();
        preencherTabela(usuarios);
    }

    private void preencherTabela(List<Usuario> usuarios) {
        DefaultTableModel modelo = (DefaultTableModel) tabelaUsuarios.getModel();

        modelo.setRowCount(0);

        for (Usuario usuario : usuarios) {
            modelo.addRow(new Object[]{
                usuario.getId(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getLogin(),
                usuario.getRole()
            });
        }
    }

    private void preencherCamposPelaTabela() {
        int linhaSelecionada = tabelaUsuarios.getSelectedRow();

        if (linhaSelecionada < 0) {
            return;
        }

        int id = Integer.parseInt(tabelaUsuarios.getValueAt(linhaSelecionada, 0).toString());

        Usuario usuario = usuarioController.buscarPorId(id);

        if (usuario == null) {
            return;
        }

        txtId.setText(String.valueOf(usuario.getId()));
        txtNome.setText(usuario.getNome());
        txtCpf.setText(usuario.getCpf());
        txtLogin.setText(usuario.getLogin());
        txtSenha.setText("");
        comboRole.setSelectedItem(usuario.getRole());
    }

    private int obterIdSelecionado() {
        if (txtId.getText() == null || txtId.getText().trim().isEmpty()) {
            return 0;
        }

        return Integer.parseInt(txtId.getText());
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtCpf.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        txtPesquisa.setText("");
        comboRole.setSelectedItem("USUARIO");

        tabelaUsuarios.clearSelection();
        txtNome.requestFocus();
    }

    private void fecharTela() {
        if (onClose != null) {
            onClose.run();
            return;
        }

        dispose();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        painelPrincipal = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        lblCpf = new javax.swing.JLabel();
        txtCpf = new javax.swing.JTextField();
        lblLogin = new javax.swing.JLabel();
        txtLogin = new javax.swing.JTextField();
        lblSenha = new javax.swing.JLabel();
        txtSenha = new javax.swing.JPasswordField();
        lblRole = new javax.swing.JLabel();
        comboRole = new javax.swing.JComboBox<>();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();
        lblPesquisa = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        scrollTabela = new javax.swing.JScrollPane();
        tabelaUsuarios = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Usuários");

        lblTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Cadastro de Usuários");

        lblId.setText("ID:");

        lblNome.setText("Nome:");

        lblCpf.setText("CPF:");

        lblLogin.setText("Login:");

        lblSenha.setText("Senha:");

        lblRole.setText("Perfil:");

        comboRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "USUARIO" }));

        btnNovo.setText("Novo");
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });

        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        lblPesquisa.setText("Pesquisar:");

        btnPesquisar.setText("Pesquisar");
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });

        tabelaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "ID", "Nome", "CPF", "Login", "Perfil"
                }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

        tabelaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaUsuariosMouseClicked(evt);
            }
        });

        scrollTabela.setViewportView(tabelaUsuarios);

        javax.swing.GroupLayout painelPrincipalLayout = new javax.swing.GroupLayout(painelPrincipal);
        painelPrincipal.setLayout(painelPrincipalLayout);

        painelPrincipalLayout.setHorizontalGroup(
            painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrollTabela)
                    .addGroup(painelPrincipalLayout.createSequentialGroup()
                        .addComponent(lblPesquisa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesquisa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesquisar))
                    .addGroup(painelPrincipalLayout.createSequentialGroup()
                        .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblId)
                            .addComponent(lblNome)
                            .addComponent(lblCpf)
                            .addComponent(lblLogin)
                            .addComponent(lblSenha)
                            .addComponent(lblRole))
                        .addGap(30, 30, 30)
                        .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNome)
                            .addComponent(txtCpf)
                            .addComponent(txtLogin)
                            .addComponent(txtSenha)
                            .addGroup(painelPrincipalLayout.createSequentialGroup()
                                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboRole, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelPrincipalLayout.createSequentialGroup()
                        .addGap(0, 310, Short.MAX_VALUE)
                        .addComponent(btnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        painelPrincipalLayout.setVerticalGroup(
            painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addGap(20, 20, 20)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblId)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCpf)
                    .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLogin)
                    .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSenha)
                    .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRole)
                    .addComponent(comboRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNovo)
                    .addComponent(btnSalvar)
                    .addComponent(btnExcluir)
                    .addComponent(btnFechar))
                .addGap(18, 18, 18)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPesquisa)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollTabela, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addContainerGap())
        );

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

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {
        limparCampos();
    }

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {
        salvarUsuario();
    }

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {
        excluirUsuario();
    }

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {
        pesquisarUsuarios();
    }

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {
        fecharTela();
    }

    private void tabelaUsuariosMouseClicked(java.awt.event.MouseEvent evt) {
        preencherCamposPelaTabela();
    }

    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<String> comboRole;
    private javax.swing.JLabel lblCpf;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblPesquisa;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel painelPrincipal;
    private javax.swing.JScrollPane scrollTabela;
    private javax.swing.JTable tabelaUsuarios;
    private javax.swing.JTextField txtCpf;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtLogin;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtPesquisa;
    private javax.swing.JPasswordField txtSenha;
}
