/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.integradorpa2.igu.Operaciones;

import com.mycompany.integradorpa2.logica.Tarea;
import com.mycompany.integradorpa2.logica.Voluntario;
import com.mycompany.integradorpa2.logica.enums.EstadoTarea;
import com.mycompany.integradorpa2.service.TareaService;
import java.util.List;
import java.util.Objects;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author pokem
 */
public class ActualizarTarea extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ActualizarTarea.class.getName());

    private final Voluntario voluntario;
    private final Long tareaInicialId;              // puede venir pre-seleccionada
    private final TareaService tareaService = new TareaService();

    private DefaultTableModel modeloTareas;
    private TableRowSorter<DefaultTableModel> sorter;
    private Long tareaSeleccionadaId = null;

    public ActualizarTarea(Voluntario voluntario, Long tareaInicialId) {
        initComponents();
        setLocationRelativeTo(null);

        this.voluntario = voluntario;
        this.tareaInicialId = tareaInicialId;

        configurarComboEstados();
        configurarTabla();
        cargarTareas();
        

        if (tareaInicialId != null) {
            seleccionarTareaEnTabla(tareaInicialId);
            mostrarDetalleTareaSeleccionada();
        }

        // listeners de botones hechos a mano
        botonSeleccionar5.addActionListener(e -> mostrarDetalleTareaSeleccionada());
        botonActualizar4.addActionListener(e -> actualizarTarea());
        botonSalir.addActionListener(e -> volverAlMenu());
    }

    private void configurarComboEstados() {
        jComboBoxEstadoTarea.removeAllItems();
        for (EstadoTarea et : EstadoTarea.values()) {
            jComboBoxEstadoTarea.addItem(et.name());
        }
    }
    private void configurarTabla() {
        modeloTareas = new DefaultTableModel(
                new Object[]{"ID", "Fecha", "Tipo", "Estado", "Gato"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Long.class;
                    default -> String.class;
                };
            }
        };
        jTableTareas.setModel(modeloTareas);
        jTableTareas.getTableHeader().setReorderingAllowed(false);
    }
    
    private void cargarTareas() {
        modeloTareas.setRowCount(0);

        if (tareaInicialId == null) {
            JOptionPane.showMessageDialog(this,
                    "No se especificó una tarea inicial.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Tarea t = tareaService.listarTodas().stream()
                .filter(tt -> Objects.equals(tt.getId(), tareaInicialId))
                .findFirst()
                .orElse(null);

        if (t == null) {
            JOptionPane.showMessageDialog(this,
                    "La tarea seleccionada no existe.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipo = t.getTipoTarea() != null ? t.getTipoTarea().name() : "";
        String estado = t.getEstadoTarea() != null ? t.getEstadoTarea().name() : "";
        String gato = (t.getGato() != null) ? t.getGato().getNombre() : "";

        modeloTareas.addRow(new Object[]{
                t.getId(),
                t.getFecha(),
                tipo,
                estado,
                gato
        });

        // seleccionar automáticamente la fila
        jTableTareas.setRowSelectionInterval(0, 0);
        mostrarDetalleTareaSeleccionada();
    }
    
    
    
    private void seleccionarTareaEnTabla(Long tareaId) {
        for (int i = 0; i < jTableTareas.getRowCount(); i++) {
            int modelRow = jTableTareas.convertRowIndexToModel(i);
            Object val = modeloTareas.getValueAt(modelRow, 0);
            if (val instanceof Long id && Objects.equals(id, tareaId)) {
                jTableTareas.setRowSelectionInterval(i, i);
                jTableTareas.scrollRectToVisible(jTableTareas.getCellRect(i, 0, true));
                break;
            }
        }
    }
    
    private void mostrarDetalleTareaSeleccionada() {
        int viewRow = jTableTareas.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una tarea de la tabla.");
            return;
        }

        int modelRow = jTableTareas.convertRowIndexToModel(viewRow);
        Long id = (Long) modeloTareas.getValueAt(modelRow, 0);

        try {
            Tarea t = tareaService.listarTodas().stream()
                    .filter(tt -> Objects.equals(tt.getId(), id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada: " + id));

            tareaSeleccionadaId = id;

            // Descripción
            jTextAreaDescripcion1.setText(
                    (t.getDescripcion() != null ? t.getDescripcion() : "") +
                    (t.getObservacion() != null && !t.getObservacion().isBlank()
                            ? "\n\nObs. previas: " + t.getObservacion()
                            : "")
            );
            jTextAreaDescripcion1.setCaretPosition(0);

            // limpiar campo de nueva observación
            jTextAreaObservacion.setText("");

            // Estado actual en el combo
            if (t.getEstadoTarea() != null) {
                jComboBoxEstadoTarea.setSelectedItem(t.getEstadoTarea().name());
            } else {
                jComboBoxEstadoTarea.setSelectedIndex(0);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar la tarea: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void actualizarTarea() {
        if (tareaSeleccionadaId == null) {
            JOptionPane.showMessageDialog(this,
                    "Primero seleccioná una tarea de la tabla.");
            return;
        }

        String estadoSel = (String) jComboBoxEstadoTarea.getSelectedItem();
        if (estadoSel == null) {
            JOptionPane.showMessageDialog(this, "Seleccioná un estado.");
            return;
        }

        EstadoTarea nuevoEstado = EstadoTarea.valueOf(estadoSel);
        String obs = jTextAreaObservacion.getText().trim();

        try {
            tareaService.actualizarEstadoYObservacion(tareaSeleccionadaId, nuevoEstado, obs);

            JOptionPane.showMessageDialog(this, "Tarea actualizada correctamente.");

            // refrescar tabla y detalle
            cargarTareas();
            seleccionarTareaEnTabla(tareaSeleccionadaId);
            mostrarDetalleTareaSeleccionada();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar la tarea: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void volverAlMenu() {
        // Volver al menú del voluntario con su contexto
        if (voluntario != null) {
            new OperacionesVoluntario(voluntario).setVisible(true);
        }
        dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        lbltitulo6 = new javax.swing.JLabel();
        lblBuscar4 = new javax.swing.JLabel();
        botonActualizar4 = new javax.swing.JButton();
        botonSeleccionar5 = new javax.swing.JButton();
        botonSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableTareas = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaObservacion = new javax.swing.JTextArea();
        jComboBoxEstadoTarea = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaDescripcion1 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbltitulo6.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lbltitulo6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbltitulo6.setText("Actualizar Tarea");

        lblBuscar4.setText("Tarea Selecionada");

        botonActualizar4.setText("Actualizar");
        botonActualizar4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonActualizar4ActionPerformed(evt);
            }
        });

        botonSeleccionar5.setText("Seleccionar");
        botonSeleccionar5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSeleccionar5ActionPerformed(evt);
            }
        });

        botonSalir.setText("Salir");
        botonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSalirActionPerformed(evt);
            }
        });

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

        jTextAreaObservacion.setColumns(20);
        jTextAreaObservacion.setRows(5);
        jScrollPane2.setViewportView(jTextAreaObservacion);

        jComboBoxEstadoTarea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Descripcion");

        jTextAreaDescripcion1.setColumns(20);
        jTextAreaDescripcion1.setRows(5);
        jScrollPane3.setViewportView(jTextAreaDescripcion1);

        jLabel2.setText("Observacion (a agregar)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(botonSeleccionar5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 123, Short.MAX_VALUE)
                                .addComponent(lblBuscar4)
                                .addGap(123, 123, 123)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxEstadoTarea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(143, 143, 143))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botonActualizar4)
                        .addGap(148, 148, 148))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(botonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lbltitulo6)
                        .addGap(261, 261, 261))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbltitulo6)
                .addGap(45, 45, 45)
                .addComponent(botonActualizar4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblBuscar4))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxEstadoTarea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonSeleccionar5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(botonSalir)
                .addContainerGap())
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

    private void botonActualizar4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonActualizar4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonActualizar4ActionPerformed

    private void botonSeleccionar5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSeleccionar5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonSeleccionar5ActionPerformed

    private void botonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSalirActionPerformed

    }//GEN-LAST:event_botonSalirActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonActualizar4;
    private javax.swing.JButton botonSalir;
    private javax.swing.JButton botonSeleccionar5;
    private javax.swing.JComboBox<String> jComboBoxEstadoTarea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableTareas;
    private javax.swing.JTextArea jTextAreaDescripcion1;
    private javax.swing.JTextArea jTextAreaObservacion;
    private javax.swing.JLabel lblBuscar4;
    private javax.swing.JLabel lbltitulo6;
    // End of variables declaration//GEN-END:variables
}
