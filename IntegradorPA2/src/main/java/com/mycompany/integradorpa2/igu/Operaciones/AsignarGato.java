
package com.mycompany.integradorpa2.igu.Operaciones;


        
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import java.util.*;
import java.util.stream.*;

import com.mycompany.integradorpa2.logica.*;
import com.mycompany.integradorpa2.logica.enums.*;
import com.mycompany.integradorpa2.service.AdopcionService;
import javax.swing.JOptionPane;

public class AsignarGato extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AsignarGato.class.getName());
    
    private final AdopcionService adopcionService = new AdopcionService();
    private java.util.List<Adopcion> adopcionesDelGatoActual;
    private DefaultTableModel modeloGatos;
    private DefaultTableModel modeloFamilias;
    private TableRowSorter<DefaultTableModel> sorterGatos;
    private List<Gato> gatosConSolicitudes = List.of();
    private Map<Long, List<Adopcion>> adopcionesPorGato = Map.of();
    private final Voluntario voluntario;
    
    public AsignarGato(Voluntario voluntario) {
        initComponents();
        setLocationRelativeTo(null);    
        this.voluntario = voluntario;
        
        configurarTablas();
        configurarBusqueda();
        cargarGatosConSolicitudes();

        jTableGatos.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            int viewRow = jTableGatos.getSelectedRow();

            if (viewRow >= 0) {
                int modelRow = jTableGatos.convertRowIndexToModel(viewRow);
                Long gatoId = (Long) modeloGatos.getValueAt(modelRow, 0); // col 0 = ID gato

                cargarFamiliasParaGato(gatoId);
            } else {
                // si no hay selección, limpio la tabla de familias
                modeloFamilias.setRowCount(0);
                jTextFieldFamiliaSeleccionada.setText("");
                adopcionesDelGatoActual = java.util.List.of();
            }
        });
        
        
        jButtonActualizarTablaGatos.addActionListener(e -> cargarGatosConSolicitudes());      
        jButtonSalir.addActionListener(e -> {
            // volver al menú de voluntario
            new OperacionesVoluntario(voluntario).setVisible(true);
            dispose();
        });
        
    }
    
    private void configurarTablaFamilias() {
    modeloFamilias = new DefaultTableModel(
        new Object[]{"ID", "Nombre", "Reputación", "Dirección", "Tipo adopción"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    jTableFamiliasInteresadas.setModel(modeloFamilias);
}

    
    private void configurarTablas() {
        // Tabla de gatos
        modeloGatos = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Zona", "Solicitudes"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) {
                return switch (c) {
                    case 0 -> Long.class;
                    case 3 -> Integer.class;
                    default -> String.class;
                };
            }
        };
        jTableGatos.setModel(modeloGatos);
        jTableGatos.getTableHeader().setReorderingAllowed(false);

        // Tabla de familias (AGREGAMOS Tipo adopción)
        modeloFamilias = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Reputación", "Dirección", "Tipo adopción"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) {
                return switch (c) {
                    case 0 -> Integer.class;  // ID
                    case 2 -> Integer.class;  // Reputación
                    default -> String.class;  // nombre, dirección, tipo adopción
                };
            }
        };
        jTableFamiliasInteresadas.setModel(modeloFamilias);
        jTableFamiliasInteresadas.getTableHeader().setReorderingAllowed(false);
    }

    private void configurarBusqueda() {
        sorterGatos = new TableRowSorter<>(modeloGatos);
        jTableGatos.setRowSorter(sorterGatos);
        jTextFieldBuscarGato.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarGatos(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarGatos(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarGatos(); }
        });
    }
    private void filtrarGatos() {
        String q = jTextFieldBuscarGato.getText().trim();
        sorterGatos.setRowFilter(q.isEmpty() ? null : RowFilter.regexFilter("(?i)"+q));
    }
    
    
    
    private void cargarGatosConSolicitudes() {
        // 1) todas adopciones EN_PROCESO
        List<Adopcion> enProceso = adopcionService.listarAdopcionesEnProceso();

        // 2) agrupo por gatoId
        adopcionesPorGato = enProceso.stream()
            .filter(a -> a.getGato() != null)
            .collect(Collectors.groupingBy(a -> a.getGato().getId()));

        // 3) lista de gatos distintos
        gatosConSolicitudes = enProceso.stream()
            .map(Adopcion::getGato)
            .filter(Objects::nonNull)
            .collect(Collectors.collectingAndThen(
                Collectors.toMap(Gato::getId, g -> g, (a,b)->a), // uniq por id
                m -> new ArrayList<>(m.values())
            ));

        // 4) pinto tabla
        modeloGatos.setRowCount(0);
        for (Gato g : gatosConSolicitudes) {
            String zona = (g.getZona()!=null ? g.getZona().getNombreZona() : "");
            int cant = adopcionesPorGato.getOrDefault(g.getId(), List.of()).size();
            modeloGatos.addRow(new Object[]{ g.getId(), g.getNombre(), zona, cant });
        }

        // limpio tabla de familias y texto
        modeloFamilias.setRowCount(0);
        jTextFieldFamiliaSeleccionada.setText("");
    }
    
    
    
    private void asignarGatoAFamiliaSeleccionada() {
        try {
            // gato
            int vg = jTableGatos.getSelectedRow();
            if (vg < 0) { javax.swing.JOptionPane.showMessageDialog(this, "Seleccioná un gato."); return; }
            int mg = jTableGatos.convertRowIndexToModel(vg);
            Long gatoId = (Long) modeloGatos.getValueAt(mg, 0);

            // familia
            int vf = jTableFamiliasInteresadas.getSelectedRow();
            if (vf < 0) { javax.swing.JOptionPane.showMessageDialog(this, "Seleccioná una familia."); return; }
            int mf = jTableFamiliasInteresadas.convertRowIndexToModel(vf);
            Integer familiaId = (Integer) modeloFamilias.getValueAt(mf, 0);         
        
            javax.swing.JOptionPane.showMessageDialog(this, "Solicitud creada / actualizada en EN_PROCESO.");
            // refresco tablas (por si cambió el estado más adelante)
            cargarGatosConSolicitudes();
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error al asignar: " + ex.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFamiliasParaGato(Long gatoId) {
        modeloFamilias.setRowCount(0);

        adopcionesDelGatoActual = adopcionService
                .listarAdopcionesDeGato(gatoId).stream()
                .filter(a -> a.getEstado() == EstadoAdopcion.EN_PROCESO)
                .sorted(java.util.Comparator.comparingInt(
                        a -> a.getFamilia() != null && a.getFamilia().getReputacion() != null
                                ? -a.getFamilia().getReputacion()   // orden DESC
                                : 0
                ))
                .toList();

        for (Adopcion a : adopcionesDelGatoActual) {
            Familia f = a.getFamilia();
            if (f == null) continue;

            modeloFamilias.addRow(new Object[]{
                    f.getId(),
                    f.getNombre(),
                    f.getReputacion(),
                    f.getDireccion(),
                    a.getTipoAdopcion() != null ? a.getTipoAdopcion().name() : ""
            });
        }
    }
    
    private void recargarTablaGatos() {
    // Limpio la tabla
    modeloGatos.setRowCount(0);

    // Gatos que tienen adopciones EN_PROCESO
    java.util.List<Gato> gatos = adopcionService.listarGatosConAdopcionEnProceso();

    for (Gato g : gatos) {
        long solicitudes = adopcionService.listarAdopcionesDeGato(g.getId()).stream()
                .filter(a -> a.getEstado() == EstadoAdopcion.EN_PROCESO)
                .count();

        modeloGatos.addRow(new Object[]{
                g.getId(),
                g.getNombre(),
                g.getZona() != null ? g.getZona().getNombreZona() : "",
                solicitudes
        });
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

        jDialog1 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lbltitulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableGatos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldBuscarGato = new javax.swing.JTextField();
        jButtonActualizarTablaGatos = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableFamiliasInteresadas = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldFamiliaSeleccionada = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButtonSalir = new javax.swing.JButton();
        jButtonAsignarGato = new javax.swing.JButton();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbltitulo.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lbltitulo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbltitulo.setText("Gatos Solicitados");

        jTableGatos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTableGatos);

        jLabel1.setText("Buscar Gato");

        jButtonActualizarTablaGatos.setText("Actualizar");
        jButtonActualizarTablaGatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActualizarTablaGatosActionPerformed(evt);
            }
        });

        jTableFamiliasInteresadas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTableFamiliasInteresadas);

        jLabel2.setText("Familias Interesadas");

        jTextFieldFamiliaSeleccionada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFamiliaSeleccionadaActionPerformed(evt);
            }
        });

        jLabel3.setText("Familia Seleccionada");

        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        jButtonAsignarGato.setText("Asignar");
        jButtonAsignarGato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAsignarGatoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(381, 381, 381)
                        .addComponent(lbltitulo))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel3))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jTextFieldFamiliaSeleccionada, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonAsignarGato))
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(59, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldBuscarGato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(178, 178, 178)
                .addComponent(jButtonActualizarTablaGatos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(189, 189, 189))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSalir)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbltitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTextFieldBuscarGato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonActualizarTablaGatos)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldFamiliaSeleccionada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonAsignarGato))))
                .addGap(2, 2, 2)
                .addComponent(jButtonSalir)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void jTextFieldFamiliaSeleccionadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFamiliaSeleccionadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFamiliaSeleccionadaActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonAsignarGatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAsignarGatoActionPerformed
        try {
            int filaGato = jTableGatos.getSelectedRow();
            if (filaGato < 0) {
                JOptionPane.showMessageDialog(this, "Seleccioná un gato de la lista.");
                return;
            }

            int filaFam = jTableFamiliasInteresadas.getSelectedRow();
            if (filaFam < 0) {
                JOptionPane.showMessageDialog(this, "Seleccioná una familia interesada.");
                return;
            }

            // Convertir a índice de modelo por si tenés sorter
            int modelRowFam = jTableFamiliasInteresadas.convertRowIndexToModel(filaFam);

            if (adopcionesDelGatoActual == null || modelRowFam >= adopcionesDelGatoActual.size()) {
                JOptionPane.showMessageDialog(this, "No se encontró la adopción seleccionada.");
                return;
            }

            Adopcion adopcionSeleccionada = adopcionesDelGatoActual.get(modelRowFam);

            // APROBAR adopción (esto marca al gato como adoptado en el service)
            Adopcion aprobada = adopcionService.aprobarAdopcion(adopcionSeleccionada.getId());

            Gato gato = aprobada.getGato();
            Familia familia = aprobada.getFamilia();
            String tipoStr = aprobada.getTipoAdopcion() != null ? aprobada.getTipoAdopcion().name() : "";

            JOptionPane.showMessageDialog(this,
                    "Adopción APROBADA.\n\n" +
                    "Gato: " + (gato != null ? gato.getNombre() : "") + "\n" +
                    "Familia: " + (familia != null ? familia.getNombre() : "") + "\n" +
                    "Tipo de adopción: " + tipoStr);

            // El gato ya no debería aparecer como disponible porque:
            // aprobarAdopcion() hace: g.setAdoptado(true);
            // Volvemos a recargar la tabla de gatos y limpiamos familias.
            recargarTablaGatos();      // método que ya tenías para jButtonActualizarTablaGatos
            modeloFamilias.setRowCount(0);
            adopcionesDelGatoActual = java.util.List.of();
            jTextFieldFamiliaSeleccionada.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al aprobar la adopción: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        recargarTablaGatos();        // recargo la tabla de gatos
        modeloFamilias.setRowCount(0);
        jTextFieldFamiliaSeleccionada.setText("");
    }//GEN-LAST:event_jButtonAsignarGatoActionPerformed

    private void jButtonActualizarTablaGatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActualizarTablaGatosActionPerformed
        recargarTablaGatos();
    }//GEN-LAST:event_jButtonActualizarTablaGatosActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonActualizarTablaGatos;
    private javax.swing.JButton jButtonAsignarGato;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableFamiliasInteresadas;
    private javax.swing.JTable jTableGatos;
    private javax.swing.JTextField jTextFieldBuscarGato;
    private javax.swing.JTextField jTextFieldFamiliaSeleccionada;
    private javax.swing.JLabel lbltitulo;
    // End of variables declaration//GEN-END:variables
}
