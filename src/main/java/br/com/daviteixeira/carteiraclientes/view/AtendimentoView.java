package br.com.daviteixeira.carteiraclientes.view;

import br.com.daviteixeira.carteiraclientes.controller.AtendimentoController;
import br.com.daviteixeira.carteiraclientes.model.Atendimento;
import br.com.daviteixeira.carteiraclientes.model.Cliente;

import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AtendimentoView extends javax.swing.JFrame {

    private final AtendimentoController atendimentoController = new AtendimentoController();
    private final DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public AtendimentoView() {
        initComponents();
        configurarTela();
    }

    private void configurarTela() {
        setLocationRelativeTo(null);
        txtId.setEditable(false);

        carregarComboClientes();
        carregarTabela();
    }

    private void carregarComboClientes() {
        DefaultComboBoxModel<Cliente> modelo = new DefaultComboBoxModel<>();

        List<Cliente> clientes = atendimentoController.listarClientesDisponiveis();

        for (Cliente cliente : clientes) {
            modelo.addElement(cliente);
        }

        comboCliente.setModel(modelo);
    }

    private void salvarAtendimento() {
        try {
            Cliente clienteSelecionado = (Cliente) comboCliente.getSelectedItem();

            Atendimento atendimento = new Atendimento();

            atendimento.setId(obterIdSelecionado());

            if (clienteSelecionado != null) {
                atendimento.setClienteId(clienteSelecionado.getId());
            }

            atendimento.setDescricao(txtDescricao.getText());

            atendimentoController.salvar(atendimento);

            JOptionPane.showMessageDialog(
                    this,
                    "Atendimento salvo com sucesso.",
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
                    "Erro ao salvar atendimento.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void excluirAtendimento() {
        try {
            int id = obterIdSelecionado();

            if (id <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um atendimento para excluir.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir este atendimento?",
                    "Confirmar exclusão",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcao == JOptionPane.YES_OPTION) {
                atendimentoController.excluir(id);

                JOptionPane.showMessageDialog(
                        this,
                        "Atendimento excluído com sucesso.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                limparCampos();
                carregarTabela();
            }

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
                    "Erro ao excluir atendimento.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void pesquisarAtendimentos() {
        try {
            List<Atendimento> atendimentos = atendimentoController.pesquisar(txtPesquisa.getText());
            preencherTabela(atendimentos);

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao pesquisar atendimentos.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void carregarTabela() {
        List<Atendimento> atendimentos = atendimentoController.listarAtendimentos();
        preencherTabela(atendimentos);
    }

    private void preencherTabela(List<Atendimento> atendimentos) {
        DefaultTableModel modelo = (DefaultTableModel) tabelaAtendimentos.getModel();

        modelo.setRowCount(0);

        for (Atendimento atendimento : atendimentos) {
            modelo.addRow(new Object[]{
                atendimento.getId(),
                atendimento.getClienteNome(),
                atendimento.getUsuarioNome(),
                formatarData(atendimento),
                atendimento.getDescricao()
            });
        }
    }

    private String formatarData(Atendimento atendimento) {
        if (atendimento.getDataAtendimento() == null) {
            return "";
        }

        return atendimento.getDataAtendimento().format(formatadorData);
    }

    private void preencherCamposPelaTabela() {
        int linhaSelecionada = tabelaAtendimentos.getSelectedRow();

        if (linhaSelecionada < 0) {
            return;
        }

        int id = Integer.parseInt(tabelaAtendimentos.getValueAt(linhaSelecionada, 0).toString());

        Atendimento atendimento = atendimentoController.buscarPorId(id);

        if (atendimento == null) {
            return;
        }

        txtId.setText(String.valueOf(atendimento.getId()));
        selecionarClienteCombo(atendimento.getClienteId());
        txtDescricao.setText(atendimento.getDescricao());
    }

    private void selecionarClienteCombo(int clienteId) {
        for (int i = 0; i < comboCliente.getItemCount(); i++) {
            Cliente cliente = comboCliente.getItemAt(i);

            if (cliente.getId() == clienteId) {
                comboCliente.setSelectedIndex(i);
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
        txtDescricao.setText("");
        txtPesquisa.setText("");

        if (comboCliente.getItemCount() > 0) {
            comboCliente.setSelectedIndex(0);
        }

        tabelaAtendimentos.clearSelection();
        txtDescricao.requestFocus();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        painelPrincipal = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblCliente = new javax.swing.JLabel();
        comboCliente = new javax.swing.JComboBox<>();
        lblDescricao = new javax.swing.JLabel();
        scrollDescricao = new javax.swing.JScrollPane();
        txtDescricao = new javax.swing.JTextArea();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();
        lblPesquisa = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        scrollTabela = new javax.swing.JScrollPane();
        tabelaAtendimentos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registro de Atendimentos");

        lblTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Registro de Atendimentos");

        lblId.setText("ID:");

        lblCliente.setText("Cliente:");

        lblDescricao.setText("Descrição:");

        txtDescricao.setColumns(20);
        txtDescricao.setRows(5);
        scrollDescricao.setViewportView(txtDescricao);

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

        tabelaAtendimentos.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "ID", "Cliente", "Usuário", "Data", "Descrição"
                }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        tabelaAtendimentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaAtendimentosMouseClicked(evt);
            }
        });

        scrollTabela.setViewportView(tabelaAtendimentos);

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
                            .addComponent(lblCliente)
                            .addComponent(lblDescricao))
                        .addGap(30, 30, 30)
                        .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(scrollDescricao)
                            .addGroup(painelPrincipalLayout.createSequentialGroup()
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelPrincipalLayout.createSequentialGroup()
                        .addGap(0, 330, Short.MAX_VALUE)
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
                    .addComponent(lblCliente)
                    .addComponent(comboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDescricao)
                    .addComponent(scrollDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(scrollTabela, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
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
        salvarAtendimento();
    }

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {
        excluirAtendimento();
    }

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {
        pesquisarAtendimentos();
    }

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void tabelaAtendimentosMouseClicked(java.awt.event.MouseEvent evt) {
        preencherCamposPelaTabela();
    }

    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<Cliente> comboCliente;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblDescricao;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblPesquisa;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel painelPrincipal;
    private javax.swing.JScrollPane scrollDescricao;
    private javax.swing.JScrollPane scrollTabela;
    private javax.swing.JTable tabelaAtendimentos;
    private javax.swing.JTextArea txtDescricao;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtPesquisa;
}