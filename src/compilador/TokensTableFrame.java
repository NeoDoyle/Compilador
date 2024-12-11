/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package compilador;

import AnalizadorLexico.Lexer;
import AnalizadorLexico.Token;
import AnalizadorLexico.Errores;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 *
 * @author otvam
 */
public class TokensTableFrame extends javax.swing.JFrame {

private JTable tokensTable;

    public TokensTableFrame(List<Token> tokens) {
        setTitle("Tokens Encontrados");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear modelo de tabla
        String[] columnNames = {"Tipo", "Lexema", "[Línea, Columna]"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        tokensTable = new JTable(tableModel);

        // Llenar la tabla con los datos de los tokens
        for (Token token : tokens) {
            String tipo = token.getTipo().name();
            String lexema = token.getValor();
            String posicion = "[" + token.getLine() + ", " + token.getColumn() + "]";
            tableModel.addRow(new Object[]{tipo, lexema, posicion});
        }

        // Configurar scroll y agregar a la ventana
        JScrollPane scrollPane = new JScrollPane(tokensTable);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void updateTokens(List<Token> tokens) {
    DefaultTableModel tableModel = (DefaultTableModel) tokensTable.getModel();
    tableModel.setRowCount(0); // Limpiar las filas actuales

    for (Token token : tokens) {
        String tipo = token.getTipo().name();
        String lexema = token.getValor();
        String posicion = "[" + token.getLine() + ", " + token.getColumn() + "]";
        tableModel.addRow(new Object[]{tipo, lexema, posicion});
    }
}
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
