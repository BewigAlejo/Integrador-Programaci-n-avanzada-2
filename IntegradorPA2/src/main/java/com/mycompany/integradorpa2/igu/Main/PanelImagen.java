package com.mycompany.integradorpa2.igu.Main;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PanelImagen extends JPanel {

       private Image imagen;

    public PanelImagen(String ruta) {
        this.imagen = new ImageIcon(getClass().getResource(ruta)).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            // Escala manteniendo calidad:
            Image scaled = imagen.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            int x = (getWidth() - scaled.getWidth(null)) / 2;
            int y = (getHeight() - scaled.getHeight(null)) / 2;
            g.drawImage(scaled, x, y, this);
        }
    }
}
