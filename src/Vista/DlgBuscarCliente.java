package Vista;

import Controlador.Ctrl_Cliente;
import Modelo.Cliente;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;

public class DlgBuscarCliente extends JDialog {

    private Cliente clienteSeleccionado = null;

    public DlgBuscarCliente(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        cargarTabla();
    }

    public Cliente mostrarDialog() {
        setVisible(true);
        return clienteSeleccionado;
    }

    private void cargarTabla() {
        try {
            Ctrl_Cliente control = new Ctrl_Cliente();
            DefaultTableModel modelo = (DefaultTableModel) tbLista.getModel();
            modelo.setRowCount(0);
            for (Cliente c : control.listar(txtBuscar.getText())) {
                modelo.addRow(new Object[]{
                    c.getIdCliente(), c.getNombre(), c.getApellido(), c.getDni()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void seleccionar() {
        int fila = tbLista.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return;
        }
        clienteSeleccionado = new Cliente();
        clienteSeleccionado.setIdCliente((Integer) tbLista.getValueAt(fila, 0));
        clienteSeleccionado.setNombre((String) tbLista.getValueAt(fila, 1));
        clienteSeleccionado.setApellido((String) tbLista.getValueAt(fila, 2));
        clienteSeleccionado.setDni((String) tbLista.getValueAt(fila, 3));
        dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbLista = new javax.swing.JTable();
        btSeleccionar = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Buscar Cliente");
        setResizable(false);

        jLabel1.setText("Buscar cliente:");

        txtBuscar.addActionListener(this::txtBuscarActionPerformed);

        btBuscar.setText("Buscar");
        btBuscar.addActionListener(this::btBuscarActionPerformed);

        tbLista.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {},
            new String [] {
                "ID", "NOMBRE", "APELLIDO", "DNI"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tbLista.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbLista);

        btSeleccionar.setText("Seleccionar");
        btSeleccionar.addActionListener(this::btSeleccionarActionPerformed);

        btCancelar.setText("Cancelar");
        btCancelar.addActionListener(this::btCancelarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btBuscar))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(btSeleccionar)
                        .addGap(50, 50, 50)
                        .addComponent(btCancelar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btSeleccionar)
                    .addComponent(btCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        cargarTabla();
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscarActionPerformed
        cargarTabla();
    }//GEN-LAST:event_btBuscarActionPerformed

    private void btSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSeleccionarActionPerformed
        seleccionar();
    }//GEN-LAST:event_btSeleccionarActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_btCancelarActionPerformed

    private void tbListaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListaMouseClicked
        if (evt.getClickCount() == 2) {
            seleccionar();
        }
    }//GEN-LAST:event_tbListaMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBuscar;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btSeleccionar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbLista;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
