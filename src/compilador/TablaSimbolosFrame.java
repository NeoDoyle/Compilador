/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

/**
 *
 * @author otvam
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TablaSimbolosFrame extends JFrame {

    private JTable tablaSimbolos;
    private DefaultTableModel tableModel;

    public TablaSimbolosFrame() {
        setTitle("Tabla de Símbolos");
        setSize(1500, 100);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear modelo de tabla
        String[] columnNames = {"Nombre", "Tipo", "Método", "Contenido"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tablaSimbolos = new JTable(tableModel);

        // Configurar scroll y agregar a la ventana
        JScrollPane scrollPane = new JScrollPane(tablaSimbolos);
        add(scrollPane, BorderLayout.CENTER);
        pack();
    }

    public void registrarSimbolo(String nombre, String tipo, String metodo, String contenido) {
        tableModel.addRow(new Object[]{nombre, tipo, metodo, contenido});
    }

    public void actualizarTabla(List<Object[]> simbolos) {
        tableModel.setRowCount(0); // Limpiar la tabla
        for (Object[] simbolo : simbolos) {
            tableModel.addRow(simbolo);
        }
    }
}
