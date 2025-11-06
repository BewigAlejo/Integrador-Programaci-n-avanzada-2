/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.integradorpa2.igu.Usuarios;

import com.mycompany.integradorpa2.dao.FamiliaDAO;
import com.mycompany.integradorpa2.dao.FamiliaDAOJpa;
import com.mycompany.integradorpa2.dao.VeterinarioDAO;
import com.mycompany.integradorpa2.dao.VeterinarioDAOJpa;
import com.mycompany.integradorpa2.dao.VoluntarioDAO;
import com.mycompany.integradorpa2.dao.VoluntarioDAOJpa;
import com.mycompany.integradorpa2.igu.Main.Navigator;
import com.mycompany.integradorpa2.logica.Familia;
import com.mycompany.integradorpa2.logica.Usuario;
import com.mycompany.integradorpa2.logica.Veterinario;
import com.mycompany.integradorpa2.logica.Voluntario;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Usuario
 */
public class UsuarioRead extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(UsuarioRead.class.getName());
    
    /**
     * Creates new form UsuarioRead
     */
    
    
    
        // DAOs concretos
    private final VeterinarioDAO vetDao = new VeterinarioDAOJpa();
    private final VoluntarioDAO volDao = new VoluntarioDAOJpa();
    private final FamiliaDAO    famDao = new FamiliaDAOJpa();
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    // filtro actual (qué rol mostrar)
    private enum RolFiltro { TODOS, VETERINARIO, VOLUNTARIO, FAMILIA }
    private RolFiltro filtroActual = RolFiltro.TODOS;
    
    
        private void configurarTablaUsuarios() {
            modelo = new DefaultTableModel(
                new Object[]{"ID", "Tipo", "Nombre", "Email", "Teléfono", "Usuario", "Rol"}, 0
            ) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
                @Override public Class<?> getColumnClass(int c) {
                    return switch (c) {
                        case 0 -> Long.class;     // ID
                        default -> String.class;
                    };
                }
            };
            tablaUsuarios.setModel(modelo);
            tablaUsuarios.getTableHeader().setReorderingAllowed(false);

            // (Opcional) Ocultar visualmente la columna "Tipo"
            TableColumn tc = tablaUsuarios.getColumnModel().getColumn(1);
            tc.setMinWidth(0); tc.setMaxWidth(0); tc.setPreferredWidth(0);
        }
        
        private static String nvl(String s) { return s == null ? "" : s; }

        private void cargarTablaUsuarios() {
            modelo.setRowCount(0);

            // helper para agregar filas
            java.util.function.BiConsumer<String, Usuario> add = (tipo, u) -> {
                Integer id        = (Integer) u.getId();
                String nombre  = nvl(u.getNombre());
                String email   = nvl(u.getEmail());
                String tel     = nvl(u.getTelefono());
                String user    = nvl(u.getUsuario());
                String rol     = nvl(u.getRol());   // si tu subclase guarda "rol" (por herencia)
                modelo.addRow(new Object[]{ id, tipo, nombre, email, tel, user, rol });
            };

            try {
                if (filtroActual == RolFiltro.TODOS || filtroActual == RolFiltro.VETERINARIO) {
                    for (Veterinario v : vetDao.listarTodos()) add.accept("VETERINARIO", v);
                }
                if (filtroActual == RolFiltro.TODOS || filtroActual == RolFiltro.VOLUNTARIO) {
                    for (Voluntario v : volDao.listarTodos()) add.accept("VOLUNTARIO", v);
                }
                if (filtroActual == RolFiltro.TODOS || filtroActual == RolFiltro.FAMILIA) {
                    for (Familia f : famDao.listarTodos()) add.accept("FAMILIA", f);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudieron cargar usuarios: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
        
        private void filtrarTexto() {
            String texto = txtBuscar.getText().trim();
            if (texto.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto)); // case-insensitive
            }
        }
        
        private void abrirUpdateSeleccionado() {
            int viewRow = tablaUsuarios.getSelectedRow();
            if (viewRow < 0) return;

            int modelRow = tablaUsuarios.convertRowIndexToModel(viewRow);
            Integer id   = (Integer)   modelo.getValueAt(modelRow, 0);
            String tp = (String) modelo.getValueAt(modelRow, 1); // Tipo

            switch (tp) {
                case "VETERINARIO" -> {
                    // Ej: UpdateVeterinario(id)
                    UpdateVeterinario up = new UpdateVeterinario(id);
                    up.setLocationRelativeTo(this);
                    up.setVisible(true);
                }
                case "VOLUNTARIO" -> {
                    UpdateVoluntario up = new UpdateVoluntario(id);
                    up.setLocationRelativeTo(this);
                    up.setVisible(true);
                }
                case "FAMILIA" -> {
                    UpdateFamilia up = new UpdateFamilia(id);
                    up.setLocationRelativeTo(this);
                    up.setVisible(true);
                }
            }
        }
        
        
    public UsuarioRead() {
        initComponents();
         setLocationRelativeTo(null);

        configurarTablaUsuarios();
        cargarTablaUsuarios(); // por defecto TODOS

        // Filtro de búsqueda (por cualquier columna)
        sorter = new TableRowSorter<>(modelo);
        tablaUsuarios.setRowSorter(sorter);
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarTexto(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarTexto(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarTexto(); }
        });

        // Doble click -> abrir edición según tipo
        tablaUsuarios.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    abrirUpdateSeleccionado();
                }
            }
        });

        // Radio buttons -> cambiar filtro y recargar
        botonVeterinarios.addActionListener(e -> { filtroActual = RolFiltro.VETERINARIO; cargarTablaUsuarios(); });
        botonVoluntarios.addActionListener(e ->  { filtroActual = RolFiltro.VOLUNTARIO;  cargarTablaUsuarios(); });
        botonFamilias.addActionListener(e ->     { filtroActual = RolFiltro.FAMILIA;     cargarTablaUsuarios(); });

        // Botón actualizar -> refrescar
        botonActualizar.addActionListener(e -> cargarTablaUsuarios());
    }
    
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoRol = new javax.swing.ButtonGroup();
        lblTitulo = new javax.swing.JLabel();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        botonActualizar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaUsuarios = new javax.swing.JTable();
        botonEliminar = new javax.swing.JButton();
        botonSalir = new javax.swing.JButton();
        botonVeterinarios = new javax.swing.JRadioButton();
        botonVoluntarios = new javax.swing.JRadioButton();
        botonFamilias = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTitulo.setText("Leer Usuarios");

        lblBuscar.setText("Buscar Usuario:");

        txtBuscar.setMinimumSize(new java.awt.Dimension(200, 22));

        botonActualizar.setText("Actualizar");

        tablaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaUsuarios);

        botonEliminar.setText("Eliminar");
        botonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarActionPerformed(evt);
            }
        });

        botonSalir.setText("Salir");
        botonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSalirActionPerformed(evt);
            }
        });

        grupoRol.add(botonVeterinarios);
        botonVeterinarios.setText("Veterinarios");

        grupoRol.add(botonVoluntarios);
        botonVoluntarios.setText("Voluntarios");

        grupoRol.add(botonFamilias);
        botonFamilias.setText("Familias");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botonEliminar)
                        .addGap(18, 18, 18)
                        .addComponent(botonSalir)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonFamilias)
                    .addComponent(botonVoluntarios)
                    .addComponent(botonVeterinarios)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTitulo)
                                .addGap(43, 43, 43))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblBuscar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)))
                        .addComponent(botonActualizar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscar)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonActualizar))
                .addGap(18, 18, 18)
                .addComponent(botonVeterinarios)
                .addGap(18, 18, 18)
                .addComponent(botonVoluntarios)
                .addGap(18, 18, 18)
                .addComponent(botonFamilias)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 37, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonEliminar)
                    .addComponent(botonSalir))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarActionPerformed
        // TODO add your handling code here:
                int viewRow = tablaUsuarios.getSelectedRow();
            if (viewRow < 0) {
                JOptionPane.showMessageDialog(this, "Seleccioná un usuario en la tabla.");
                return;
            }

            int modelRow = tablaUsuarios.convertRowIndexToModel(viewRow);
            Integer id       = (Integer)   modelo.getValueAt(modelRow, 0);
            String tipo   = (String) modelo.getValueAt(modelRow, 1);
            String nombre = (String) modelo.getValueAt(modelRow, 2);

            int r = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar el " + tipo.toLowerCase() + " ID " + id + " (“" + nombre + "”)?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (r != JOptionPane.YES_OPTION) return;

            try {
                switch (tipo) {
                    case "VETERINARIO" -> vetDao.eliminar(id);
                    case "VOLUNTARIO"  -> volDao.eliminar(id);
                    case "FAMILIA"     -> famDao.eliminar(id);
                }
                JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                cargarTablaUsuarios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
    }//GEN-LAST:event_botonEliminarActionPerformed

    private void botonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSalirActionPerformed
        // TODO add your handling code here:
        Navigator.go(this, new UsuarioMenu());
    }//GEN-LAST:event_botonSalirActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new UsuarioRead().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonActualizar;
    private javax.swing.JButton botonEliminar;
    private javax.swing.JRadioButton botonFamilias;
    private javax.swing.JButton botonSalir;
    private javax.swing.JRadioButton botonVeterinarios;
    private javax.swing.JRadioButton botonVoluntarios;
    private javax.swing.ButtonGroup grupoRol;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tablaUsuarios;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
