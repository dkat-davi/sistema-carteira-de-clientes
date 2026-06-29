package br.com.daviteixeira.carteiraclientes.view;

import java.awt.BorderLayout;
import java.awt.Window;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

public class RelatorioViewerView extends javax.swing.JPanel {

    private Runnable onClose;

    public RelatorioViewerView(String titulo, JasperPrint jasperPrint) {
        initComponents();

        lblTitulo.setText(titulo);

        painelRelatorio.setLayout(new BorderLayout());
        painelRelatorio.add(new JRViewer(jasperPrint), BorderLayout.CENTER);
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        painelPrincipal = new javax.swing.JPanel();
        painelTopo = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        btnFechar = new javax.swing.JButton();
        painelRelatorio = new javax.swing.JPanel();

        lblTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        lblTitulo.setText("Relatório");

        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout painelTopoLayout = new javax.swing.GroupLayout(painelTopo);
        painelTopo.setLayout(painelTopoLayout);
        painelTopoLayout.setHorizontalGroup(
            painelTopoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelTopoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        painelTopoLayout.setVerticalGroup(
            painelTopoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelTopoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelTopoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitulo)
                    .addComponent(btnFechar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout painelRelatorioLayout = new javax.swing.GroupLayout(painelRelatorio);
        painelRelatorio.setLayout(painelRelatorioLayout);
        painelRelatorioLayout.setHorizontalGroup(
            painelRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        painelRelatorioLayout.setVerticalGroup(
            painelRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 520, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout painelPrincipalLayout = new javax.swing.GroupLayout(painelPrincipal);
        painelPrincipal.setLayout(painelPrincipalLayout);
        painelPrincipalLayout.setHorizontalGroup(
            painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelTopo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(painelRelatorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        painelPrincipalLayout.setVerticalGroup(
            painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelPrincipalLayout.createSequentialGroup()
                .addComponent(painelTopo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(painelRelatorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {
        if (onClose != null) {
            onClose.run();
            return;
        }

        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }

    private javax.swing.JButton btnFechar;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel painelPrincipal;
    private javax.swing.JPanel painelRelatorio;
    private javax.swing.JPanel painelTopo;
}
