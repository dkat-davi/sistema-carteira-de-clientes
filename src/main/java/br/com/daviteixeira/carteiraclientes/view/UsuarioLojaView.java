package br.com.daviteixeira.carteiraclientes.view;

import br.com.daviteixeira.carteiraclientes.controller.UsuarioLojaController;
import br.com.daviteixeira.carteiraclientes.model.Loja;
import br.com.daviteixeira.carteiraclientes.model.Usuario;
import br.com.daviteixeira.carteiraclientes.model.UsuarioLoja;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class UsuarioLojaView extends javax.swing.JFrame {

    private final UsuarioLojaController usuarioLojaController = new UsuarioLojaController();

    public UsuarioLojaView() {
        initComponents();
        configurarTela();
    }

    private void configurarTela() {
        setLocationRelativeTo(null);
        txtId.setEditable(false);
        carregarCombos();
        carregarTabela();
    }

    private void carregarCombos() {
        carregarComboUsuarios();
        carregarComboLojas();
        comboCargo.setSelectedItem("VENDEDOR");
    }

    private void carregarComboUsuarios() {
        DefaultComboBoxModel<Usuario> modelo = new DefaultComboBoxModel<>();

        List<Usuario> usuarios = usuarioLojaController.listarUsuarios();

        for (Usuario usuario : usuarios) {
            modelo.addElement(usuario);
        }

        comboUsuario.setModel(modelo);
    }

    private void carregarComboLojas() {
        DefaultComboBoxModel<Loja> modelo = new DefaultComboBoxModel<>();

        List<Loja> lojas = usuarioLojaController.listarLojas();

        for (Loja loja : lojas) {
            modelo.addElement(loja);
        }

        comboLoja.setModel(modelo);
    }

    private void salvarVinculo() {
        try {
            Usuario usuarioSelecionado = (Usuario) comboUsuario.getSelectedItem();
            Loja lojaSelecionada = (Loja) comboLoja.getSelectedItem();

            UsuarioLoja usuarioLoja = new UsuarioLoja();

            usuarioLoja.setId(obterIdSelecionado());

            if (usuarioSelecionado != null) {
                usuarioLoja.setUsuarioId(usuarioSelecionado.getId());
            }

            if (lojaSelecionada != null) {
                usuarioLoja.setLojaId(lojaSelecionada.getId());
            }

            usuarioLoja.setCargo(comboCargo.getSelectedItem().toString());

            usuarioLojaController.salvar(usuarioLoja);

            JOptionPane.showMessageDialog(
                    this,
                    "Vínculo salvo com sucesso.",
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
                    "Erro ao salvar vínculo usuário x loja.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void excluirVinculo() {
        try {
            int id = obterIdSelecionado();

            if (id <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um vínculo para excluir.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir este vínculo?",
                    "Confirmar exclusão",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcao == JOptionPane.YES_OPTION) {
                usuarioLojaController.excluir(id);

                JOptionPane.showMessageDialog(
                        this,
                        "Vínculo excluído com sucesso.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                limparCampos();
                carregarTabela();
            }

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao excluir vínculo usuário x loja.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void pesquisarVinculos() {
        try {
            List<UsuarioLoja> vinculos = usuarioLojaController.pesquisar(txtPesquisa.getText());
            preencherTabela(vinculos);

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao pesquisar vínculos.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void carregarTabela() {
        List<UsuarioLoja> vinculos = usuarioLojaController.listarTodos();
        preencherTabela(vinculos);
    }

    private void preencherTabela(List<UsuarioLoja> vinculos) {
        DefaultTableModel modelo = (DefaultTableModel) tabelaVinculos.getModel();

        modelo.setRowCount(0);

        for (UsuarioLoja vinculo : vinculos) {
            modelo.addRow(new Object[]{
                vinculo.getId(),
                vinculo.getUsuarioNome(),
                vinculo.getLojaNome(),
                vinculo.getCargo()
            });
        }
    }

    private void preencherCamposPelaTabela() {
        int linhaSelecionada = tabelaVinculos.getSelectedRow();

        if (linhaSelecionada < 0) {
            return;
        }

        int id = Integer.parseInt(tabelaVinculos.getValueAt(linhaSelecionada, 0).toString());

        UsuarioLoja vinculo = usuarioLojaController.buscarPorId(id);

        if (vinculo == null) {
            return;
        }

        txtId.setText(String.valueOf(vinculo.getId()));
        selecionarUsuarioCombo(vinculo.getUsuarioId());
        selecionarLojaCombo(vinculo.getLojaId());
        comboCargo.setSelectedItem(vinculo.getCargo());
    }

    private void selecionarUsuarioCombo(int usuarioId) {
        for (int i = 0; i < comboUsuario.getItemCount(); i++) {
            Usuario usuario = comboUsuario.getItemAt(i);

            if (usuario.getId() == usuarioId) {
                comboUsuario.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selecionarLojaCombo(int lojaId) {
        for (int i = 0; i < comboLoja.getItemCount(); i++) {
            Loja loja = comboLoja.getItemAt(i);

            if (loja.getId() == lojaId) {
                comboLoja.setSelectedIndex(i);
                return;
            }
        }
    }

    private int obterIdSelecionado() {
        if (txtId.getText() == null || txtId.getText().trim().isEmpty()) {
            return 0;
        }

        return Integer.parseInt(txtId.getText());
    }

    private void limparCampos() {
        txtId.setText("");
        txtPesquisa.setText("");

        if (comboUsuario.getItemCount() > 0) {
            comboUsuario.setSelectedIndex(0);
        }

        if (comboLoja.getItemCount() > 0) {
            comboLoja.setSelectedIndex(0);
        }

        comboCargo.setSelectedItem("VENDEDOR");
        tabelaVinculos.clearSelection();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        painelPrincipal = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblUsuario = new javax.swing.JLabel();
        comboUsuario = new javax.swing.JComboBox<>();
        lblLoja = new javax.swing.JLabel();
        comboLoja = new javax.swing.JComboBox<>();
        lblCargo = new javax.swing.JLabel();
        comboCargo = new javax.swing.JComboBox<>();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();
        lblPesquisa = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        scrollTabela = new javax.swing.JScrollPane();
        tabelaVinculos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Vínculo Usuário x Loja");

        lblTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Vínculo Usuário x Loja");

        lblId.setText("ID:");

        lblUsuario.setText("Usuário:");

        lblLoja.setText("Loja:");

        lblCargo.setText("Cargo:");

        comboCargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GERENTE", "VENDEDOR" }));

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

        tabelaVinculos.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "ID", "Usuário", "Loja", "Cargo"
                }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

        tabelaVinculos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaVinculosMouseClicked(evt);
            }
        });

        scrollTabela.setViewportView(tabelaVinculos);

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
                            .addComponent(lblUsuario)
                            .addComponent(lblLoja)
                            .addComponent(lblCargo))
                        .addGap(30, 30, 30)
                        .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboUsuario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboLoja, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(painelPrincipalLayout.createSequentialGroup()
                                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelPrincipalLayout.createSequentialGroup()
                        .addGap(0, 300, Short.MAX_VALUE)
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
                    .addComponent(lblUsuario)
                    .addComponent(comboUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoja)
                    .addComponent(comboLoja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCargo)
                    .addComponent(comboCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(scrollTabela, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
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
        salvarVinculo();
    }

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {
        excluirVinculo();
    }

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {
        pesquisarVinculos();
    }

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void tabelaVinculosMouseClicked(java.awt.event.MouseEvent evt) {
        preencherCamposPelaTabela();
    }

    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<String> comboCargo;
    private javax.swing.JComboBox<Loja> comboLoja;
    private javax.swing.JComboBox<Usuario> comboUsuario;
    private javax.swing.JLabel lblCargo;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblLoja;
    private javax.swing.JLabel lblPesquisa;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel painelPrincipal;
    private javax.swing.JScrollPane scrollTabela;
    private javax.swing.JTable tabelaVinculos;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtPesquisa;
}