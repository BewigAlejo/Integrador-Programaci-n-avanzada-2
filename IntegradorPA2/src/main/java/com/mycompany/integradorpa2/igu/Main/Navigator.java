package com.mycompany.integradorpa2.igu.Main;

import javax.swing.JFrame;

public final class Navigator {
    private Navigator() {}

    /** Abre 'to' y cierra 'from'. */
    public static void go(JFrame from, JFrame to) {
        to.setLocationRelativeTo(from);
        to.setVisible(true);
        if (from != null) from.dispose();
    }

    /** Abre 'to' sin cerrar 'from'. */
    public static void open(JFrame to, JFrame relativeTo) {
        to.setLocationRelativeTo(relativeTo);
        to.setVisible(true);
    }
}
