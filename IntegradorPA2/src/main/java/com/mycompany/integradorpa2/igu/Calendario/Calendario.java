
package com.mycompany.integradorpa2.igu.Calendario;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;


import com.mycompany.integradorpa2.logica.Tarea;
import com.mycompany.integradorpa2.logica.enums.EstadoTarea;
import com.mycompany.integradorpa2.service.TareaService;

import javax.swing.table.DefaultTableModel; 

public class Calendario extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Calendario.class.getName());

    private final TareaService tareaService = new TareaService();
    private DefaultTableModel modelo;

    public Calendario() {
        initComponents();
        setLocationRelativeTo(null);
        configurarTabla();
        configurarBotones();
        configurarColoresSituacion();
        cargarTabla(EstadoTarea.PENDIENTE); // vista inicial
        jToggleButtonPendientes.setSelected(true);
    }
    
     private void configurarTabla() {
        modelo = new DefaultTableModel(
                new Object[]{"ID", "Fecha", "Tipo", "Estado", "Gato", "Voluntario", "Vence", "Situación"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Long.class; // ID
                    default -> Object.class;
                };
            }
        };

        jTableTareas.setModel(modelo);
        jTableTareas.getTableHeader().setReorderingAllowed(false);
    }
     
    private void cargarTabla(EstadoTarea filtro) {
        modelo.setRowCount(0);

        var tareas = tareaService.listarParaCalendario(filtro);

        for (Tarea t : tareas) {
            var fecha = t.getFecha();
            var tipo = t.getTipoTarea() != null ? t.getTipoTarea().name() : "";
            var estado = t.getEstadoTarea() != null ? t.getEstadoTarea().name() : "";
            var gato = (t.getGato() != null) ? t.getGato().getNombre() : "";
            var vol  = (t.getAsignadaA() != null) ? t.getAsignadaA().getNombre() : "";
            var vto  = tareaService.calcularFechaVencimiento(t);
            var situ = tareaService.situacionSegunVencimiento(t);

            modelo.addRow(new Object[]{
                    t.getId(),
                    fecha,
                    tipo,
                    estado,
                    gato,
                    vol,
                    vto,
                    situ
            });
        }
    } 
    
    private void configurarBotones() {
        jToggleButtonPendientes.addActionListener(e -> {
            jToggleButtonPendientes.setSelected(true);
            jToggleButtonEnProceso1.setSelected(false);
            jToggleButtonEnProceso2.setSelected(false);
            cargarTabla(EstadoTarea.PENDIENTE);
        });

        jToggleButtonEnProceso1.addActionListener(e -> {
            jToggleButtonPendientes.setSelected(false);
            jToggleButtonEnProceso1.setSelected(true);
            jToggleButtonEnProceso2.setSelected(false);
            cargarTabla(EstadoTarea.EN_PROCESO);
        });

        // este lo usamos para COMPLETADA (HECHA)
        jToggleButtonEnProceso2.addActionListener(e -> {
            jToggleButtonPendientes.setSelected(false);
            jToggleButtonEnProceso1.setSelected(false);
            jToggleButtonEnProceso2.setSelected(true);
            cargarTabla(EstadoTarea.HECHA);
        });

        jButtonSalir.addActionListener(e -> dispose());
    }
    
    private void configurarColoresSituacion() {
    // Columna "Situación" = índice 7
    TableColumn colSituacion = jTableTareas.getColumnModel().getColumn(7);

    colSituacion.setCellRenderer(new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                // dejamos el color de selección del sistema
                return c;
            }

            String texto = (value != null) ? value.toString() : "";

            // Colores suaves para el fondo
            switch (texto) {
                case "A TIEMPO" -> c.setBackground(new Color(204, 255, 204)); // verde claro
                case "POR VENCER" -> c.setBackground(new Color(255, 255, 204)); // amarillo claro
                case "VENCIDA" -> c.setBackground(new Color(255, 204, 204)); // rojo claro
                default -> c.setBackground(Color.WHITE);
            }

            c.setForeground(Color.BLACK); // letras negras para todos

            return c;
        }
    });
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lbltitulo2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableTareas = new javax.swing.JTable();
        jToggleButtonPendientes = new javax.swing.JToggleButton();
        jToggleButtonEnProceso1 = new javax.swing.JToggleButton();
        jToggleButtonEnProceso2 = new javax.swing.JToggleButton();
        jButtonSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbltitulo2.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lbltitulo2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbltitulo2.setText("Calendario Tareas");

        jTableTareas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTableTareas);

        jToggleButtonPendientes.setText("Pendientes");
        jToggleButtonPendientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonPendientesActionPerformed(evt);
            }
        });

        jToggleButtonEnProceso1.setText("En Proceso");
        jToggleButtonEnProceso1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonEnProceso1ActionPerformed(evt);
            }
        });

        jToggleButtonEnProceso2.setText("Completada");
        jToggleButtonEnProceso2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonEnProceso2ActionPerformed(evt);
            }
        });

        jButtonSalir.setText("Salir");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbltitulo2)
                .addGap(244, 244, 244))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonSalir)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jToggleButtonPendientes)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jToggleButtonEnProceso1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jToggleButtonEnProceso2))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 685, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(lbltitulo2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButtonPendientes)
                    .addComponent(jToggleButtonEnProceso1)
                    .addComponent(jToggleButtonEnProceso2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonSalir)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButtonPendientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonPendientesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButtonPendientesActionPerformed

    private void jToggleButtonEnProceso1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonEnProceso1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButtonEnProceso1ActionPerformed

    private void jToggleButtonEnProceso2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonEnProceso2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButtonEnProceso2ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Calendario().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableTareas;
    private javax.swing.JToggleButton jToggleButtonEnProceso1;
    private javax.swing.JToggleButton jToggleButtonEnProceso2;
    private javax.swing.JToggleButton jToggleButtonPendientes;
    private javax.swing.JLabel lbltitulo2;
    // End of variables declaration//GEN-END:variables
}
