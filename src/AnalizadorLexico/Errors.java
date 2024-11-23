/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

/**
 *
 * @author hp
 */
public class Errors {
    //cacateristicas: 
    private final String valor; //variable 1 
    private final String tipe;
    private final Tokens tipo; //identificador
    private final int line;
    private final int column;

    public Errors(String valor, String tipe, Tokens tipo, int line, int column) {
        this.valor = valor;
        this.tipe = tipe;
        this.tipo = tipo;
        this.line = line;
        this.column = column;
    }

    public String getTipe() {
        return tipe;
    }

    public String getValor() {
        return valor;
    }

    public Tokens getTipo() {
        return tipo;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Tokenizer{" + "valor=" + valor + ", tipo=" + tipo + ", line=" + line + ", column=" + column + '}';
    }
    
    
    

  
    
}
