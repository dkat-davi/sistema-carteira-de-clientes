package br.com.daviteixeira.carteiraclientes.view;

import br.com.daviteixeira.carteiraclientes.controller.ClienteController;
import br.com.daviteixeira.carteiraclientes.model.Cliente;
import br.com.daviteixeira.carteiraclientes.model.Loja;
import br.com.daviteixeira.carteiraclientes.model.Usuario;
import br.com.daviteixeira.carteiraclientes.util.SessaoUsuario;

import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ClienteView extends javax.swing.JFrame {

    private final ClienteController clienteController = new ClienteController();
    private final DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ClienteView() {
        initComponents();
        configurarTela();
    }

    private void configurarTela() {
        setLocationRelativeTo(null);
        txtId.setEditable(false);

        if (!SessaoUsuario.isAdmin()) {
            comboVendedor.setEnabled(false);
        }

        carregarCombos();
        carregarTabela();
    }

    private void carregarCombos() {
        carregarComboLojas();
        carregarVendedoresDaLoja();
    }

    private void carregarComboLojas() {
        DefaultComboBoxModel<Loja> modelo = new DefaultComboBoxModel<>();

        List<Loja> lojas = clienteController.listarLojasDisponiveis();

        for (Loja loja : lojas) {
            modelo.addElement(loja);
        }

        comboLoja.setModel(modelo);
    }

    private void carregarVendedoresDaLoja() {
        DefaultComboBoxModel<Usuario> modelo = new DefaultComboBoxModel<>();

        Loja lojaSelecionada = (Loja) comboLoja.getSelectedItem();

        if (lojaSelecionada != null) {
            List<Usuario> vendedores = clienteController.listarVendedoresPorLoja(lojaSelecionada.getId());

            for (Usuario vendedor : vendedores) {
                modelo.addElement(vendedor);
            }
        }

        comboVendedor.setModel(modelo);
    }

    private void salvarCliente() {
        try {
            Loja lojaSelecionada = (Loja) comboLoja.getSelectedItem();
            Usuario vendedorSelecionado = (Usuario) comboVendedor.getSelectedItem();

            Cliente cliente = new Cliente();

            cliente.setId(obterIdSelecionado());
            cliente.setNome(txtNome.getText());
            cliente.setCpf(txtCpf.getText());
            cliente.setTelefone(txtTelefone.getText());
            cliente.setEmail(txtEmail.getText());
            cliente.setEndereco(txtEndereco.getText());

            if (lojaSelecionada != null) {
                cliente.setLojaId(lojaSelecionada.getId());
            }

            if (vendedorSelecionado != null) {
                cliente.setVendedorId(vendedorSelecionado.getId());
            }

            clienteController.salvar(cliente);

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente salvo com sucesso.",
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
                    "Erro ao salvar cliente.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void excluirCliente() {
        try {
            int id = obterIdSelecionado();

            if (id <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um cliente para excluir.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir este cliente?",
                    "Confirmar exclusão",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcao == JOptionPane.YES_OPTION) {
                clienteController.excluir(id);

                JOptionPane.showMessageDialog(
                        this,
                        "Cliente excluído com sucesso.",
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
                    "Erro ao excluir cliente. Verifique se ele possui atendimentos vinculados.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void pesquisarClientes() {
        try {
            List<Cliente> clientes = clienteController.pesquisar(txtPesquisa.getText());
            preencherTabela(clientes);

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao pesquisar clientes.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void carregarTabela() {
        List<Cliente> clientes = clienteController.listarClientes();
        preencherTabela(clientes);
    }

    private void preencherTabela(List<Cliente> clientes) {
        DefaultTableModel modelo = (DefaultTableModel) tabelaClientes.getModel();

        modelo.setRowCount(0);

        for (Cliente cliente : clientes) {
            modelo.addRow(new Object[]{
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getTelefone(),
                cliente.getLojaNome(),
                cliente.getVendedorNome(),
                formatarData(cliente)
            });
        }
    }

    private String formatarData(Cliente cliente) {
        if (cliente.getDataCadastro() == null) {
            return "";
        }

        return cliente.getDataCadastro().format(formatadorData);
    }

    private void preencherCamposPelaTabela() {
        int linhaSelecionada = tabelaClientes.getSelectedRow();

        if (linhaSelecionada < 0) {
            return;
        }

        int id = Integer.parseInt(tabelaClientes.getValueAt(linhaSelecionada, 0).toString());

        Cliente cliente = clienteController.buscarPorId(id);

        if (cliente == null) {
            return;
        }

        txtId.setText(String.valueOf(cliente.getId()));
        txtNome.setText(cliente.getNome());
        txtCpf.setText(cliente.getCpf());
        txtTelefone.setText(cliente.getTelefone());
        txtEmail.setText(cliente.getEmail());
        txtEndereco.setText(cliente.getEndereco());

        selecionarLojaCombo(cliente.getLojaId());
        carregarVendedoresDaLoja();
        selecionarVendedorCombo(cliente.getVendedorId());
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

    private void selecionarVendedorCombo(int vendedorId) {
        for (int i = 0; i < comboVendedor.getItemCount(); i++) {
            Usuario vendedor = comboVendedor.getItemAt(i);

            if (vendedor.getId() == vendedorId) {
                comboVendedor.setSelectedIndex(i);
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
        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        txtPesquisa.setText("");

        if (comboLoja.getItemCount() > 0) {
            comboLoja.setSelectedIndex(0);
            carregarVendedoresDaLoja();
        }

        tabelaClientes.clearSelection();
        txtNome.requestFocus();
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
        lblTelefone = new javax.swing.JLabel();
        txtTelefone = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblEndereco = new javax.swing.JLabel();
        txtEndereco = new javax.swing.JTextField();
        lblLoja = new javax.swing.JLabel();
        comboLoja = new javax.swing.JComboBox<>();
        lblVendedor = new javax.swing.JLabel();
        comboVendedor = new javax.swing.JComboBox<>();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();
        lblPesquisa = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        scrollTabela = new javax.swing.JScrollPane();
        tabelaClientes = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Clientes");

        lblTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Cadastro de Clientes");

        lblId.setText("ID:");

        lblNome.setText("Nome:");

        lblCpf.setText("CPF:");

        lblTelefone.setText("Telefone:");

        lblEmail.setText("E-mail:");

        lblEndereco.setText("Endereço:");

        lblLoja.setText("Loja:");

        comboLoja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboLojaActionPerformed(evt);
            }
        });

        lblVendedor.setText("Vendedor:");

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

        tabelaClientes.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "ID", "Nome", "CPF", "Telefone", "Loja", "Vendedor", "Data Cadastro"
                }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        tabelaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaClientesMouseClicked(evt);
            }
        });

        scrollTabela.setViewportView(tabelaClientes);

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
                            .addComponent(lblTelefone)
                            .addComponent(lblEmail)
                            .addComponent(lblEndereco)
                            .addComponent(lblLoja)
                            .addComponent(lblVendedor))
                        .addGap(30, 30, 30)
                        .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNome)
                            .addComponent(txtCpf)
                            .addComponent(txtTelefone)
                            .addComponent(txtEmail)
                            .addComponent(txtEndereco)
                            .addComponent(comboLoja, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboVendedor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(painelPrincipalLayout.createSequentialGroup()
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelPrincipalLayout.createSequentialGroup()
                        .addGap(0, 360, Short.MAX_VALUE)
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
                    .addComponent(lblTelefone)
                    .addComponent(txtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEndereco)
                    .addComponent(txtEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoja)
                    .addComponent(comboLoja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVendedor)
                    .addComponent(comboVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(scrollTabela, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
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
        salvarCliente();
    }

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {
        excluirCliente();
    }

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {
        pesquisarClientes();
    }

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void tabelaClientesMouseClicked(java.awt.event.MouseEvent evt) {
        preencherCamposPelaTabela();
    }

    private void comboLojaActionPerformed(java.awt.event.ActionEvent evt) {
        carregarVendedoresDaLoja();
    }

    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<Loja> comboLoja;
    private javax.swing.JComboBox<Usuario> comboVendedor;
    private javax.swing.JLabel lblCpf;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEndereco;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblLoja;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblPesquisa;
    private javax.swing.JLabel lblTelefone;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblVendedor;
    private javax.swing.JPanel painelPrincipal;
    private javax.swing.JScrollPane scrollTabela;
    private javax.swing.JTable tabelaClientes;
    private javax.swing.JTextField txtCpf;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEndereco;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtPesquisa;
    private javax.swing.JTextField txtTelefone;
}