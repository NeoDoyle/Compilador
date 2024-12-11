/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

/**
 *
 * @author hp
 */
public class Errores {
    //cacateristicas: 
    private final String valor; // el token probelma
    private final String mensaje; //error
    private final int line;
    private final int column;

    public Errores(String valor, String mensaje, int line, int column) {
        this.valor = valor;
        this.mensaje = mensaje;
        this.line = line;
        this.column = column;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getValor() {
        return valor;
    }

    

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
    @Override
    public String toString() {
        return "Errores: " + "valor -> " + valor + ", Mensaje:" + mensaje + " (line-> " + line + " column->" + column+')' ;
    }
   
}