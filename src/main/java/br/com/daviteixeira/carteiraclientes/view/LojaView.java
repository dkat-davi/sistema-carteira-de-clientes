package br.com.daviteixeira.carteiraclientes.view;

import br.com.daviteixeira.carteiraclientes.controller.LojaController;
import br.com.daviteixeira.carteiraclientes.model.Loja;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class LojaView extends javax.swing.JFrame {

    private final LojaController lojaController;
    private Runnable onClose;

    public LojaView() {
        this.lojaController = new LojaController();

        initComponents();

        setLocationRelativeTo(null);
        txtId.setEditable(false);
        carregarTabela();
    }

    public javax.swing.JPanel getPainelPrincipal() {
        return painelPrincipal;
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    private void salvarLoja() {
        try {
            Loja loja = new Loja();

            loja.setId(obterIdSelecionado());
            loja.setNome(txtNome.getText());
            loja.setTelefone(txtTelefone.getText());
            loja.setEndereco(txtEndereco.getText());
            loja.setCidade(txtCidade.getText());
            loja.setEstado(txtEstado.getText());
            loja.setAtivo(true);

            lojaController.salvar(loja);

            JOptionPane.showMessageDialog(
                    this,
                    "Loja salva com sucesso.",
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
                    "Erro ao salvar loja.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void excluirLoja() {
        try {
            int id = obterIdSelecionado();

            if (id <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione uma loja para excluir.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir esta loja?",
                    "Confirmar exclusão",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcao == JOptionPane.YES_OPTION) {
                lojaController.excluir(id);

                JOptionPane.showMessageDialog(
                        this,
                        "Loja excluída com sucesso.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                limparCampos();
                carregarTabela();
            }

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao excluir loja.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void pesquisarLojas() {
        try {
            List<Loja> lojas = lojaController.pesquisarPorNome(txtPesquisa.getText());
            preencherTabela(lojas);

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao pesquisar lojas.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void carregarTabela() {
        List<Loja> lojas = lojaController.listarTodas();
        preencherTabela(lojas);
    }

    private void preencherTabela(List<Loja> lojas) {
        DefaultTableModel modelo = (DefaultTableModel) tabelaLojas.getModel();

        modelo.setRowCount(0);

        for (Loja loja : lojas) {
            modelo.addRow(new Object[]{
                loja.getId(),
                loja.getNome(),
                loja.getTelefone(),
                loja.getCidade(),
                loja.getEstado()
            });
        }
    }

    private void preencherCamposPelaTabela() {
        int linhaSelecionada = tabelaLojas.getSelectedRow();

        if (linhaSelecionada < 0) {
            return;
        }

        int id = Integer.parseInt(tabelaLojas.getValueAt(linhaSelecionada, 0).toString());

        Loja loja = lojaController.buscarPorId(id);

        if (loja == null) {
            return;
        }

        txtId.setText(String.valueOf(loja.getId()));
        txtNome.setText(loja.getNome());
        txtTelefone.setText(loja.getTelefone());
        txtEndereco.setText(loja.getEndereco());
        txtCidade.setText(loja.getCidade());
        txtEstado.setText(loja.getEstado());
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
        txtTelefone.setText("");
        txtEndereco.setText("");
        txtCidade.setText("");
        txtEstado.setText("");
        txtPesquisa.setText("");

        tabelaLojas.clearSelection();
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
        lblTelefone = new javax.swing.JLabel();
        txtTelefone = new javax.swing.JTextField();
        lblEndereco = new javax.swing.JLabel();
        txtEndereco = new javax.swing.JTextField();
        lblCidade = new javax.swing.JLabel();
        txtCidade = new javax.swing.JTextField();
        lblEstado = new javax.swing.JLabel();
        txtEstado = new javax.swing.JTextField();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();
        lblPesquisa = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        scrollTabela = new javax.swing.JScrollPane();
        tabelaLojas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Lojas");

        lblTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Cadastro de Lojas");

        lblId.setText("ID:");

        lblNome.setText("Nome:");

        lblTelefone.setText("Telefone:");

        lblEndereco.setText("Endereço:");

        lblCidade.setText("Cidade:");

        lblEstado.setText("Estado:");

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

        tabelaLojas.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Nome", "Telefone", "Cidade", "Estado"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        tabelaLojas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaLojasMouseClicked(evt);
            }
        });

        scrollTabela.setViewportView(tabelaLojas);

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
                                                        .addComponent(lblTelefone)
                                                        .addComponent(lblEndereco)
                                                        .addComponent(lblCidade)
                                                        .addComponent(lblEstado))
                                                .addGap(20, 20, 20)
                                                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtNome)
                                                        .addComponent(txtTelefone)
                                                        .addComponent(txtEndereco)
                                                        .addComponent(txtCidade)
                                                        .addGroup(painelPrincipalLayout.createSequentialGroup()
                                                                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(0, 0, Short.MAX_VALUE))))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelPrincipalLayout.createSequentialGroup()
                                                .addGap(0, 280, Short.MAX_VALUE)
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
                                        .addComponent(lblTelefone)
                                        .addComponent(txtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblEndereco)
                                        .addComponent(txtEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblCidade)
                                        .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblEstado)
                                        .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(scrollTabela, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
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
        salvarLoja();
    }

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {
        excluirLoja();
    }

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {
        pesquisarLojas();
    }

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {
        fecharTela();
    }

    private void tabelaLojasMouseClicked(java.awt.event.MouseEvent evt) {
        preencherCamposPelaTabela();
    }

    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JLabel lblCidade;
    private javax.swing.JLabel lblEndereco;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblPesquisa;
    private javax.swing.JLabel lblTelefone;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel painelPrincipal;
    private javax.swing.JScrollPane scrollTabela;
    private javax.swing.JTable tabelaLojas;
    private javax.swing.JTextField txtCidade;
    private javax.swing.JTextField txtEndereco;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtPesquisa;
    private javax.swing.JTextField txtTelefone;
}
