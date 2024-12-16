/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package compilador;

import AnalizadorLexico.Lexer;
import AnalizadorLexico.Token;
import AnalizadorLexico.Errores;
import AnalizadorSintactico.Parser;
import compilador.TablaSimbolosFrame;


import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author otvam
 */
public class IDE extends javax.swing.JFrame {
    
    NumeroLinea numerolinea;
    Directorio dir;
    TokensTableFrame tokensFrame; // Ventana de la tabla de tokens
    TablaSimbolosFrame tablaSimbolos;
    /**
     * Creates new form IDE
     */
    public IDE() {
        initComponents();
        inicializar();
        colors();
    }
    
        //METODO PARA ENCONTRAR LAS ULTIMAS CADENAS
    private int findLastNonWordChar(String text, int index) {
        while (--index >= 0) {
            //  \\W = [A-Za-Z0-9]
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    //METODO PARA ENCONTRAR LAS PRIMERAS CADENAS 
    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    //MÉTODO PARA PINTAR LAS PALABRAS RESERVADAS
private void colors() {
    final StyleContext cont = StyleContext.getDefaultStyleContext();

    // COLORES 
    final AttributeSet attred = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(255, 0, 35));
    final AttributeSet attgreen = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(8, 161, 115));
    final AttributeSet attblue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0, 147, 255));
    final AttributeSet attpurple = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(102, 0, 102));
    final AttributeSet attblack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0, 0, 0));

    // Documento estilizado
    DefaultStyledDocument doc = new DefaultStyledDocument() {
        @Override
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
            super.insertString(offset, str, a);
            updateStyles();
        }

        @Override
        public void remove(int offs, int len) throws BadLocationException {
            super.remove(offs, len);
            updateStyles();
        }

        private void updateStyles() throws BadLocationException {
            String text = getText(0, getLength());

            // Resetear estilos
            setCharacterAttributes(0, text.length(), attblack, true);

            // Buscar y pintar comentarios
            Pattern commentPattern = Pattern.compile("//.*");
            Matcher commentMatcher = commentPattern.matcher(text);
            while (commentMatcher.find()) {
                setCharacterAttributes(commentMatcher.start(), commentMatcher.end() - commentMatcher.start(), attgreen, false);
            }

            // Pintar palabras reservadas
            int before = 0;
            int after = text.length();
            int wordL = before;
            int wordR = before;

            while (wordR <= after) {
                if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                    String word = text.substring(wordL, wordR);
                    if (word.matches("(\\W)*(SI|HAZ|ENTONCES|LOOP|A)")) {
                        setCharacterAttributes(wordL, wordR - wordL, attblue, false);
                    } else if (word.matches("(\\W)*(INT|DEC|CAD)")) {
                        setCharacterAttributes(wordL, wordR - wordL, attgreen, false);
                    } else if (word.matches("(\\W)*(RET|ETD|SLD)")) {
                        setCharacterAttributes(wordL, wordR - wordL, attred, false);
                    } else if (word.matches("(\\W)*(HUNGARO|VOGEL|ESQNOROESTE|CRUCEARROYO|RECURSOS|TAREAS|COSTOS|MINIMIZAR|MAXIMIZAR|FUENTES|DESTINOS|OFERTA|DEMANDA|RESOLVER)")) {
                        setCharacterAttributes(wordL, wordR - wordL, attpurple, false);
                    }
                    wordL = wordR;
                }
                wordR++;
            }
        }
    };

    // Configuración del JTextPane
    JTextPane txt = new JTextPane(doc);
    String temp = jtpCode.getText();
    jtpCode.setStyledDocument(txt.getStyledDocument());
    jtpCode.setText(temp);
}


    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGuardar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnAbrir = new javax.swing.JButton();
        btnReservadas = new javax.swing.JButton();
        btnIdentifiers = new javax.swing.JButton();
        btnTokens = new javax.swing.JButton();
        btnCompilar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaCompile = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtpCode = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/Icon/icons8_save_48px.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setToolTipText("Guardar documento");
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/pressed/icons8_save_48px_p.png"))); // NOI18N
        btnGuardar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/On Layer/icons8_save_48px_on.png"))); // NOI18N
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/Icon/icons8_code_file_48px.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setToolTipText("Nuevo documento");
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/pressed/icons8_code_file_48px_p.png"))); // NOI18N
        btnNuevo.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/On Layer/icons8_code_file_48px_on.png"))); // NOI18N
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/Icon/icons8_opened_folder_48px.png"))); // NOI18N
        btnAbrir.setText("Abrir");
        btnAbrir.setToolTipText("Abrir documento");
        btnAbrir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAbrir.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/pressed/icons8_opened_folder_48px_P.png"))); // NOI18N
        btnAbrir.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/On Layer/icons8_opened_folder_48px_ON.png"))); // NOI18N
        btnAbrir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirActionPerformed(evt);
            }
        });

        btnReservadas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/Icon/icons8-text-color-48.png"))); // NOI18N
        btnReservadas.setText("Reservadas");
        btnReservadas.setToolTipText("Palabras reservadas");
        btnReservadas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReservadas.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/pressed/icons8-text-color-48.png"))); // NOI18N
        btnReservadas.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/On Layer/icons8-text-color-48.png"))); // NOI18N
        btnReservadas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReservadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReservadasActionPerformed(evt);
            }
        });

        btnIdentifiers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/Icon/icons8-text-cursor-48.png"))); // NOI18N
        btnIdentifiers.setText("Simbolos");
        btnIdentifiers.setToolTipText("Identación");
        btnIdentifiers.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIdentifiers.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/pressed/icons8-text-cursor-48.png"))); // NOI18N
        btnIdentifiers.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/On Layer/icons8-text-cursor-48.png"))); // NOI18N
        btnIdentifiers.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnIdentifiers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIdentifiersActionPerformed(evt);
            }
        });

        btnTokens.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/Icon/icons8-index-48.png"))); // NOI18N
        btnTokens.setText("Tokens");
        btnTokens.setToolTipText("Ver tokens");
        btnTokens.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTokens.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/pressed/icons8-index-48.png"))); // NOI18N
        btnTokens.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/On Layer/icons8-index-48.png"))); // NOI18N
        btnTokens.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTokens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTokensActionPerformed(evt);
            }
        });

        btnCompilar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/Icon/icons8_code_48px.png"))); // NOI18N
        btnCompilar.setText("Compilar");
        btnCompilar.setToolTipText("Compilar código");
        btnCompilar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCompilar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/pressed/icons8_code_48px_p.png"))); // NOI18N
        btnCompilar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/On Layer/icons8_code_48px_on.png"))); // NOI18N
        btnCompilar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCompilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompilarActionPerformed(evt);
            }
        });

        jtaCompile.setColumns(20);
        jtaCompile.setRows(5);
        jScrollPane2.setViewportView(jtaCompile);

        jtpCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtpCodeKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jtpCode);

        jLabel1.setFont(new java.awt.Font("Courier New", 0, 68)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 204));
        jLabel1.setText("Pathos");

        jLabel3.setFont(new java.awt.Font("Courier New", 1, 48)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 102, 51));
        jLabel3.setText("Equipo Caimanes");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/Icon/Equipo_logo4.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compilador/iconos/Iconos/Icon/Pathos_logo2.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 994, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(btnNuevo)
                                .addGap(18, 18, 18)
                                .addComponent(btnGuardar)
                                .addGap(44, 44, 44)
                                .addComponent(btnAbrir)
                                .addGap(44, 44, 44)
                                .addComponent(btnReservadas)
                                .addGap(55, 55, 55)
                                .addComponent(btnTokens)
                                .addGap(40, 40, 40)
                                .addComponent(btnIdentifiers)
                                .addGap(51, 51, 51)
                                .addComponent(btnCompilar))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(112, 112, 112)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnNuevo)
                            .addComponent(btnGuardar)
                            .addComponent(btnAbrir)
                            .addComponent(btnReservadas)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnCompilar, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnTokens, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(btnIdentifiers))
                        .addGap(42, 42, 42)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel5))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        jtaCompile.setText("");
        dir.Nuevo(this);
        clearAllComp();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        dir.Guardar(this);
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirActionPerformed
        dir.Abrir(this);
    }//GEN-LAST:event_btnAbrirActionPerformed

    private void btnReservadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservadasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnReservadasActionPerformed

    private void btnIdentifiersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIdentifiersActionPerformed
        if (tablaSimbolos != null) {
        if (tablaSimbolos.isVisible()) {
            tablaSimbolos.dispose();
        } else {
            tablaSimbolos.setVisible(true); // Abrir la ventana si está cerrada
        }
    } else {
        JOptionPane.showMessageDialog(this, "Debe compilar el código para generar la tabla de simbolos.", 
                                      "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    }//GEN-LAST:event_btnIdentifiersActionPerformed

    private void btnTokensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTokensActionPerformed
            if (tokensFrame != null) {
        if (tokensFrame.isVisible()) {
            tokensFrame.setVisible(false); // Cerrar la ventana si está abierta
        } else {
            tokensFrame.setVisible(true); // Abrir la ventana si está cerrada
        }
    } else {
        JOptionPane.showMessageDialog(this, "Debe compilar el código para generar la tabla de tokens.", 
                                      "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    }//GEN-LAST:event_btnTokensActionPerformed

    private void btnCompilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompilarActionPerformed
            // Obtener el texto del JTextPane
    String codigo = jtpCode.getText();

    // Instanciar el Lexer y analizar el código
    Lexer lexer = new Lexer();
    List<Token> tokens = lexer.lex(codigo);

    // Construir el resultado del análisis
    StringBuilder resultado = new StringBuilder();

    // Imprimir los errores léxicos si los hay
    List<Errores> erroresLexicos = lexer.getErrores();
    if (!erroresLexicos.isEmpty()) {
        resultado.append("\nErrores léxicos encontrados:\n");
        for (Errores error : erroresLexicos) {
            resultado.append(error.toString()).append("\n");
        }
        resultado.append("\nSe ha detenido el analisis sintáctico.");
    } else {
        resultado.append("\nNo se encontraron errores léxicos.\n");
        
        // Crear instancia de la tabla de símbolos antes de ejecutar el parser
        if (tablaSimbolos != null) {
            tablaSimbolos.dispose(); // Cerrar ventana previa si existe
        }
        tablaSimbolos = new TablaSimbolosFrame();
        
        // Ejecutar el Parser si no hay errores léxicos
        List<Errores> erroresSintacticos = new ArrayList<>();
        Parser parser = new Parser(tokens, erroresSintacticos, tablaSimbolos);
        parser.parse();

        if (!erroresSintacticos.isEmpty()) {
            resultado.append("\nErrores sintácticos encontrados:\n");
            for (Errores error : erroresSintacticos) {
                resultado.append(error.toString()).append("\n");
            }
        } else {
            resultado.append("\nEl análisis sintáctico fue exitoso. No se encontraron errores.\n");
        }
    }

    // Mostrar el resultado en el JTextArea
    jtaCompile.setText(resultado.toString());

    // Actualizar la tabla de tokens
    if (tokensFrame == null) {
        tokensFrame = new TokensTableFrame(tokens); // Crear la ventana si no existe
    } else {
        tokensFrame.updateTokens(tokens); // Actualizar la tabla si ya existe
    }

    }//GEN-LAST:event_btnCompilarActionPerformed

    private void jtpCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtpCodeKeyReleased
        int keyCode = evt.getKeyCode();
        if ((keyCode >= 65 && keyCode <= 90) || (keyCode >= 48 && keyCode <=57)
                || (keyCode >= 97 && keyCode <= 122) || (keyCode != 27 && !(keyCode >=37
                && keyCode <= 40) && !(keyCode >= 16
                && keyCode <= 18) && keyCode != 524
                && keyCode != 20)) {
            if (!getTitle().contains("*")) {
                setTitle(getTitle() + "*");
            }
        }
    }//GEN-LAST:event_jtpCodeKeyReleased

    /**
     * @param args the command line arguments
     */
    
    
    private void inicializar(){
        dir = new Directorio();
        
        setTitle("[#Pathos]");
        String[] options = new String[]{"Guardar y continuar","Descargar"};
        numerolinea = new NumeroLinea(jtpCode);
        
        jScrollPane3.setRowHeaderView(numerolinea);
    }
    
    
    
    
    
    
    
    
    
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IDE.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IDE.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IDE.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IDE.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
       
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IDE().setVisible(true);
            }
        });
    }

    
    public void clearAllComp() {
        jtaCompile.setText("");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrir;
    private javax.swing.JButton btnCompilar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnIdentifiers;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnReservadas;
    private javax.swing.JButton btnTokens;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jtaCompile;
    public javax.swing.JTextPane jtpCode;
    // End of variables declaration//GEN-END:variables
}
