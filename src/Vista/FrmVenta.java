package Vista;

import Controlador.Ctrl_CabeceraVenta;
import Modelo.CabeceraVenta;
import Modelo.Cliente;
import Modelo.DetalleVenta;
import Modelo.Producto;
import Modelo.Usuario;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmVenta extends javax.swing.JFrame {

    private final Usuario usuarioLogueado;
    private Cliente clienteSeleccionado = null;
    private final List<DetalleVenta> carrito = new ArrayList<>();

    public FrmVenta(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Registrar Venta");
        txtFechaVenta.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
    }

    private void buscarCliente() {
        DlgBuscarCliente dlg = new DlgBuscarCliente(this);
        Cliente c = dlg.mostrarDialog();
        if (c != null) {
            clienteSeleccionado = c;
            txtNombreCliente.setText(c.getNombre() + " " + c.getApellido());
            txtDniCliente.setText(c.getDni());
        }
    }

    private void buscarProducto() {
        DlgBuscarProducto dlg = new DlgBuscarProducto(this);
        Producto p = dlg.mostrarDialog();
        if (p != null) {
            txtProductoId.setText(String.valueOf(p.getIdProducto()));
            txtNombreProducto.setText(p.getNombre());
            txtPrecio.setText(String.format("%.2f", p.getPrecio()));
            txtStock.setText(String.valueOf(p.getCantidad()));
            txtCantidad.setText("1");
            txtDescuento.setText("0");
            txtCantidad.requestFocus();
        }
    }

    private void agregarAlCarrito() {
        if (txtNombreProducto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto");
            return;
        }
        int cantidad;
        double descuento;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "Cantidad debe ser mayor a cero");
                return;
            }
            descuento = Double.parseDouble(txtDescuento.getText().trim());
            if (descuento < 0) {
                JOptionPane.showMessageDialog(this, "Descuento no puede ser negativo");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numericos validos");
            return;
        }
        int stock = Integer.parseInt(txtStock.getText());
        if (cantidad > stock) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente. Stock actual: " + stock);
            return;
        }
        double precio = Double.parseDouble(txtPrecio.getText());
        double subtotal = precio * cantidad;
        double totalPagar = subtotal - descuento;

        DetalleVenta det = new DetalleVenta();
        det.setIdProducto(Integer.parseInt(txtProductoId.getText()));
        det.setCantidad(cantidad);
        det.setPrecioUnitario(precio);
        det.setSubtotal(subtotal);
        det.setDescuento(descuento);
        det.setTotalPagar(totalPagar);
        det.setEstado(1);

        Producto p = new Producto();
        p.setIdProducto(Integer.parseInt(txtProductoId.getText()));
        p.setNombre(txtNombreProducto.getText());
        p.setPrecio(precio);
        det.setProducto(p);

        carrito.add(det);
        actualizarTablaCarrito();
        limpiarProducto();
    }

    private void quitarItem() {
        int fila = tbCarrito.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un item del carrito");
            return;
        }
        carrito.remove(fila);
        actualizarTablaCarrito();
    }

    private void actualizarTablaCarrito() {
        DefaultTableModel modelo = (DefaultTableModel) tbCarrito.getModel();
        modelo.setRowCount(0);
        double total = 0;
        for (DetalleVenta d : carrito) {
            modelo.addRow(new Object[]{
                d.getIdProducto(),
                d.getProducto().getNombre(),
                d.getCantidad(),
                String.format("%.2f", d.getPrecioUnitario()),
                String.format("%.2f", d.getDescuento()),
                String.format("%.2f", d.getTotalPagar())
            });
            total += d.getTotalPagar();
        }
        txtTotal.setText(String.format("%.2f", total));
    }

    private void limpiarProducto() {
        txtProductoId.setText("");
        txtNombreProducto.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtCantidad.setText("");
        txtDescuento.setText("");
    }

    private void guardarVenta() {
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return;
        }
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un producto al carrito");
            return;
        }
        CabeceraVenta cabecera = new CabeceraVenta();
        cabecera.setIdCliente(clienteSeleccionado.getIdCliente());
        cabecera.setIdUsuario(usuarioLogueado.getIdUsuario());
        cabecera.setValorPaga(Double.parseDouble(txtTotal.getText()));
        cabecera.setFechaVenta(new java.sql.Date(System.currentTimeMillis()));
        cabecera.setEstado(1);
        try {
            Ctrl_CabeceraVenta control = new Ctrl_CabeceraVenta();
            boolean rpta = control.guardarVentaCompleta(cabecera, carrito);
            if (rpta) {
                JOptionPane.showMessageDialog(this, "Venta registrada correctamente");
                clienteSeleccionado = null;
                txtNombreCliente.setText("");
                txtDniCliente.setText("");
                carrito.clear();
                actualizarTablaCarrito();
                limpiarProducto();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar venta: " + e.getMessage());
        }
    }

    private void cancelar() {
        clienteSeleccionado = null;
        txtNombreCliente.setText("");
        txtDniCliente.setText("");
        carrito.clear();
        actualizarTablaCarrito();
        limpiarProducto();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        btBuscarCliente = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtFechaVenta = new javax.swing.JTextField();
        txtDniCliente = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtNombreProducto = new javax.swing.JTextField();
        btBuscarProducto = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtDescuento = new javax.swing.JTextField();
        btAgregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCarrito = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        btGuardar = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        btQuitar = new javax.swing.JButton();
        txtProductoId = new javax.swing.JTextField();

        txtProductoId.setVisible(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel1.setText("DATOS DEL CLIENTE");

        jLabel2.setText("Cliente:");

        txtNombreCliente.setEditable(false);

        btBuscarCliente.setText("Buscar Cliente");
        btBuscarCliente.addActionListener(this::btBuscarClienteActionPerformed);

        jLabel3.setText("Fecha:");

        txtFechaVenta.setEditable(false);

        txtDniCliente.setEditable(false);

        jLabel5.setText("DNI:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel4.setText("DATOS DEL PRODUCTO");

        jLabel6.setText("Producto:");

        txtNombreProducto.setEditable(false);

        btBuscarProducto.setText("Buscar Producto");
        btBuscarProducto.addActionListener(this::btBuscarProductoActionPerformed);

        jLabel7.setText("Stock:");

        txtStock.setEditable(false);

        jLabel8.setText("Precio:");

        txtPrecio.setEditable(false);

        jLabel10.setText("Cantidad:");

        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadKeyTyped(evt);
            }
        });

        jLabel11.setText("Descuento:");

        txtDescuento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescuentoKeyTyped(evt);
            }
        });

        btAgregar.setText("Agregar");
        btAgregar.addActionListener(this::btAgregarActionPerformed);

        tbCarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "COD", "PRODUCTO", "CANT", "P.UNIT", "DESC", "SUBTOTAL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbCarrito);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16));
        jLabel9.setText("TOTAL:");

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Segoe UI", 1, 18));
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        btGuardar.setBackground(new java.awt.Color(0, 102, 51));
        btGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btGuardar.setText("Guardar Venta");
        btGuardar.addActionListener(this::btGuardarActionPerformed);

        btCancelar.setText("Cancelar");
        btCancelar.addActionListener(this::btCancelarActionPerformed);

        btQuitar.setText("Quitar Item");
        btQuitar.addActionListener(this::btQuitarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btBuscarCliente)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDniCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btBuscarProducto))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btAgregar))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btGuardar)
                        .addGap(18, 18, 18)
                        .addComponent(btCancelar)
                        .addGap(18, 18, 18)
                        .addComponent(btQuitar)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBuscarCliente)
                    .addComponent(jLabel3)
                    .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDniCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBuscarProducto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAgregar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btGuardar)
                    .addComponent(btCancelar)
                    .addComponent(btQuitar))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscarClienteActionPerformed
        buscarCliente();
    }//GEN-LAST:event_btBuscarClienteActionPerformed

    private void btBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscarProductoActionPerformed
        buscarProducto();
    }//GEN-LAST:event_btBuscarProductoActionPerformed

    private void btAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAgregarActionPerformed
        agregarAlCarrito();
    }//GEN-LAST:event_btAgregarActionPerformed

    private void btGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGuardarActionPerformed
        guardarVenta();
    }//GEN-LAST:event_btGuardarActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btCancelarActionPerformed

    private void btQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btQuitarActionPerformed
        quitarItem();
    }//GEN-LAST:event_btQuitarActionPerformed

    private void txtCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) evt.consume();
    }//GEN-LAST:event_txtCantidadKeyTyped

    private void txtDescuentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescuentoKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '.') evt.consume();
    }//GEN-LAST:event_txtDescuentoKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAgregar;
    private javax.swing.JButton btBuscarCliente;
    private javax.swing.JButton btBuscarProducto;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btGuardar;
    private javax.swing.JButton btQuitar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tbCarrito;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtDescuento;
    private javax.swing.JTextField txtDniCliente;
    private javax.swing.JTextField txtFechaVenta;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtProductoId;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
