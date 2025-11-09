/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.integradorpa2.igu.Gatos;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mycompany.integradorpa2.dao.GatoDAO;
import com.mycompany.integradorpa2.dao.GatoDAOJpa;
import com.mycompany.integradorpa2.igu.Main.Navigator;
import com.mycompany.integradorpa2.logica.Gato;
import com.mycompany.integradorpa2.logica.Zona;
import com.mycompany.integradorpa2.logica.enums.EstadoSalud;
import java.util.Optional;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
/**
 *
 * @author Usuario
 */
public class ReadGato extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ReadGato.class.getName());

    /**
     * Creates new form ReadGato
     */
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;

    public ReadGato() {
        initComponents();
        setLocationRelativeTo(null);
        configurarTabla();
        cargarTabla();

        // Filtro de búsqueda (por cualquier columna)
        sorter = new TableRowSorter<>(modelo);
        tablaGatos.setRowSorter(sorter);
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });

        // Doble click -> abrir edición
        
        tablaGatos.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int viewRow = tablaGatos.getSelectedRow();
                    if (viewRow >= 0) {
                        int modelRow = tablaGatos.convertRowIndexToModel(viewRow);
                        Long id = (Long) modelo.getValueAt(modelRow, 0);
                        // Abrí tu pantalla de Update pasando el id:
                        UpdateGato update = new UpdateGato(id);
                        update.setLocationRelativeTo(ReadGato.this);
                        update.setVisible(true);
                        // opcional: dispose();
                    }
                }
            }
        
        });
        
    }
    
    private void configurarTabla() {
        modelo = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Raza", "Edad", "Estado", "Adoptado", "Zona"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; } // solo lectura
            @Override public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Long.class;     // ID
                    case 3 -> Integer.class;  // Edad
                    default -> String.class;
                };
            }
        };
        tablaGatos.setModel(modelo);
        tablaGatos.getTableHeader().setReorderingAllowed(false);
    }
    private final GatoDAO gatoDao = new GatoDAOJpa();
    private void cargarTabla() {
        modelo.setRowCount(0); // limpiar
        List<Gato> gatos = gatoDao.listarTodos();
        for (Gato g : gatos) {
            Long id        = g.getId();
            String nombre  = nvl(g.getNombre());
            String raza    = nvl(g.getRaza());
            Integer edad   = g.getEdad() != null ? g.getEdad() : 0;
            EstadoSalud es = g.getEstadoDeSalud();
            String estado  = es != null ? es.name() : "";
            String adopt   = (g.isAdoptado() ? "Sí" : "No");
            Zona z         = g.getZona();
            String zona    = (z != null ? nvl(z.getNombreZona()) : "");

            modelo.addRow(new Object[]{id, nombre, raza, edad, estado, adopt, zona});
        }
    }

    private void filtrar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto)); // (?i) = case-insensitive
        }
    }
    
    private static String nvl(String s) { return s == null ? "" : s; }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lbltitulo2 = new javax.swing.JLabel();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        botonActualizar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaGatos = new javax.swing.JTable();
        botonSalir = new javax.swing.JButton();
        botonEliminar = new javax.swing.JButton();
        botonGenerarQR = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbltitulo2.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lbltitulo2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbltitulo2.setText("Leer Gatos");

        lblBuscar.setText("Buscar gato:");

        txtBuscar.setMinimumSize(new java.awt.Dimension(200, 22));
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        botonActualizar.setText("Actualizar");

        tablaGatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaGatos);

        botonSalir.setText("Salir");
        botonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSalirActionPerformed(evt);
            }
        });

        botonEliminar.setText("Eliminar");
        botonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarActionPerformed(evt);
            }
        });

        botonGenerarQR.setText("Generar QR");
        botonGenerarQR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGenerarQRActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbltitulo2)
                        .addGap(43, 43, 43))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)))
                .addComponent(botonActualizar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botonGenerarQR)
                        .addGap(18, 18, 18)
                        .addComponent(botonEliminar)
                        .addGap(18, 18, 18)
                        .addComponent(botonSalir)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lbltitulo2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscar)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonActualizar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonSalir)
                    .addComponent(botonEliminar)
                    .addComponent(botonGenerarQR)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSalirActionPerformed
        // TODO add your handling code here:
        Navigator.go(this, new GatosCRUD());
    }//GEN-LAST:event_botonSalirActionPerformed

    private void botonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarActionPerformed
        // TODO add your handling code here:
        int viewRow = tablaGatos.getSelectedRow();
        if (viewRow < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccioná un gato en la tabla.");
            return;
        }

        int modelRow = tablaGatos.convertRowIndexToModel(viewRow);
        Long id = (Long) modelo.getValueAt(modelRow, 0);
        String nombre = (String) modelo.getValueAt(modelRow, 1);

        int r = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "¿Eliminar el gato ID " + id + " (“" + nombre + "”)?",
            "Confirmar eliminación",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
        );
        if (r != javax.swing.JOptionPane.YES_OPTION) return;

        try {
            gatoDao.eliminar(id);
            javax.swing.JOptionPane.showMessageDialog(this, "Gato eliminado.");
            cargarTabla(); // refrescá la grilla
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "No se pudo eliminar: " + ex.getMessage(),
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_botonEliminarActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void botonGenerarQRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGenerarQRActionPerformed
        // TODO add your handling code here:
        int viewRow = tablaGatos.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un gato de la tabla.");
            return;
        }

        int modelRow = tablaGatos.convertRowIndexToModel(viewRow);
        Object val = tablaGatos.getModel().getValueAt(modelRow, 0);
        if (val == null) {
            JOptionPane.showMessageDialog(this, "No se pudo obtener el ID del gato.");
            return;
        }

        Long gatoId = (val instanceof Number) ? ((Number) val).longValue()
                                              : Long.parseLong(val.toString());

        try {
            // 1) Buscar gato
            Gato gato = gatoDao.buscarPorId(gatoId)
                    .orElseThrow(() -> new IllegalArgumentException("Gato no encontrado: " + gatoId));

            // 2) Definir contenido del QR (por ahora algo simple)
            String contenidoQR = "GATO:" + gato.getId() + ":" + gato.getNombre();
            // si querés: "https://miapp.com/gato/" + gato.getId();

            // 3) Guardar el string en la entidad
            gato.setQr(contenidoQR);
            gatoDao.actualizar(gato);

            // 4) Generar imagen QR con ZXing
            int size = 250;
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(contenidoQR,
                    BarcodeFormat.QR_CODE, size, size);

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // 5) Mostrar en un JOptionPane
            ImageIcon icon = new ImageIcon(qrImage);
            JOptionPane.showMessageDialog(this,
                    null,
                    "QR del gato " + gato.getNombre(),
                    JOptionPane.PLAIN_MESSAGE,
                    icon
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al generar QR: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_botonGenerarQRActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new ReadGato().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonActualizar;
    private javax.swing.JButton botonEliminar;
    private javax.swing.JButton botonGenerarQR;
    private javax.swing.JButton botonSalir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lbltitulo2;
    private javax.swing.JTable tablaGatos;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
