/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.integradorpa2.igu.Operaciones;

import com.mycompany.integradorpa2.igu.Gatos.CreateGato;
import com.mycompany.integradorpa2.igu.Main.Navigator;
import com.mycompany.integradorpa2.logica.Tarea;
import com.mycompany.integradorpa2.logica.Voluntario;
import com.mycompany.integradorpa2.logica.enums.EstadoTarea;
import com.mycompany.integradorpa2.service.TareaService;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Usuario
 */
public class OperacionesVoluntario extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(OperacionesVoluntario.class.getName());

    private final Voluntario voluntario;
    private final TareaService tareaService = new TareaService();

    private DefaultTableModel modeloTareas;
    private TableRowSorter<DefaultTableModel> sorter;

    public OperacionesVoluntario(Voluntario voluntario) {
        initComponents();
        setLocationRelativeTo(null);
        this.voluntario = voluntario;

        configurarTablaTareas();
        cargarTareasPendientes();
        configurarFiltroTareas();
         configurarDobleClickTareas();
    }

    private void configurarDobleClickTareas() {
        jTableTareasPendientes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 
                        && evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                    abrirActualizarTareaSeleccionada();
                }
            }
        });
    }
    
    private void abrirActualizarTareaSeleccionada() {
        int viewRow = jTableTareasPendientes.getSelectedRow();
        if (viewRow < 0) {
            return; // nada seleccionado
        }

        int modelRow = jTableTareasPendientes.convertRowIndexToModel(viewRow);

        Object value = modeloTareas.getValueAt(modelRow, 0); // col 0 = ID tarea
        if (value == null) {
            return;
        }

        Long tareaId;
        if (value instanceof Long) {
            tareaId = (Long) value;
        } else {
            tareaId = Long.valueOf(value.toString());
        }

        // Abrir la pantalla de actualizar SOLO con esa tarea
        new ActualizarTarea(voluntario, tareaId).setVisible(true);
        dispose();  // si querés cerrar el menú; si no, comentá esta línea
    }

    private void configurarTablaTareas() {
    modeloTareas = new DefaultTableModel(
            new Object[]{"ID", "Fecha", "Tipo", "Estado", "Gato", "Zona"}, 0
    ) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
        @Override public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> Long.class; // ID
                default -> Object.class;
            };
        }
    };

    jTableTareasPendientes.setModel(modeloTareas);
    jTableTareasPendientes.getTableHeader().setReorderingAllowed(false);
}
    
    private void cargarTareasPendientes() {
    modeloTareas.setRowCount(0);

    if (voluntario == null) {   
        return;
    }
    var tareas = tareaService.listarPorVoluntario(voluntario.getId());

    for (Tarea t : tareas) {
        if (t.getEstadoTarea() == EstadoTarea.PENDIENTE
                || t.getEstadoTarea() == EstadoTarea.EN_PROCESO) {

            String gatoNombre = (t.getGato() != null) ? t.getGato().getNombre() : "";

            // Zona viene del gato (si existe relación gato->zona)
            String zonaNombre = "";
            if (t.getGato() != null
                    && t.getGato().getZona() != null
                    && t.getGato().getZona().getNombreZona() != null) {
                zonaNombre = t.getGato().getZona().getNombreZona();
            }

            modeloTareas.addRow(new Object[]{
                    t.getId(),
                    t.getFecha(),
                    t.getTipoTarea(),
                    t.getEstadoTarea(),
                    gatoNombre,
                    zonaNombre
            });
        }
    }
}
    
    private void configurarFiltroTareas() {
    sorter = new TableRowSorter<>(modeloTareas);
    jTableTareasPendientes.setRowSorter(sorter);

    jTextFieldBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarTareas(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarTareas(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarTareas(); }
    });
}

private void filtrarTareas() {
    String texto = jTextFieldBuscar.getText().trim();
    if (texto.isEmpty()) {
        sorter.setRowFilter(null);
    } else {
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
    }
}
    

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lbltitulo = new javax.swing.JLabel();
        botonGestionarTareas = new javax.swing.JButton();
        BotonSalir = new javax.swing.JButton();
        botonRegistrarGatos = new javax.swing.JButton();
        botonAsignarGatos = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableTareasPendientes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldBuscar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbltitulo.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lbltitulo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbltitulo.setText("Menu Voluntario");

        botonGestionarTareas.setText("Gestionar Tareas");
        botonGestionarTareas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGestionarTareasActionPerformed(evt);
            }
        });

        BotonSalir.setText("Salir");
        BotonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonSalirActionPerformed(evt);
            }
        });

        botonRegistrarGatos.setText("Registrar Gatos");
        botonRegistrarGatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRegistrarGatosActionPerformed(evt);
            }
        });

        botonAsignarGatos.setText("Asignar Gatos");
        botonAsignarGatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAsignarGatosActionPerformed(evt);
            }
        });

        jTableTareasPendientes.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTableTareasPendientes);

        jLabel1.setText("Tareas Pendientes");

        jTextFieldBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBuscarActionPerformed(evt);
            }
        });

        jLabel2.setText("Buscar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(239, 239, 239)
                        .addComponent(lbltitulo))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(209, 209, 209)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BotonSalir)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonAsignarGatos, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonRegistrarGatos, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonGestionarTareas, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(78, 78, 78))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbltitulo)
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(botonGestionarTareas, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonRegistrarGatos, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(botonAsignarGatos, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BotonSalir)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(16, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonGestionarTareasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGestionarTareasActionPerformed
        new VoluntarioGestionarTareas(voluntario).setVisible(true);
        dispose(); // opcional
    }//GEN-LAST:event_botonGestionarTareasActionPerformed

    private void BotonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonSalirActionPerformed
        // TODO add your handling code here:
        Navigator.go(this, new InicioSesion());
    }//GEN-LAST:event_BotonSalirActionPerformed

    private void botonRegistrarGatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRegistrarGatosActionPerformed
        Navigator.go(this, new CreateGato());        
    }//GEN-LAST:event_botonRegistrarGatosActionPerformed

    private void botonAsignarGatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAsignarGatosActionPerformed
        
    }//GEN-LAST:event_botonAsignarGatosActionPerformed

    private void jTextFieldBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBuscarActionPerformed

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
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonSalir;
    private javax.swing.JButton botonAsignarGatos;
    private javax.swing.JButton botonGestionarTareas;
    private javax.swing.JButton botonRegistrarGatos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableTareasPendientes;
    private javax.swing.JTextField jTextFieldBuscar;
    private javax.swing.JLabel lbltitulo;
    // End of variables declaration//GEN-END:variables
}
