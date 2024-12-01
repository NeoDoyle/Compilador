/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

/**
 *
 * @author hp
 */

public class Token {
    //cacateristicas: 
    private final String valor; //variable 1 
    private final Tokens tipo; //identificador
    private final int line;
    private final int column;

    public Token(String valor, Tokens tipo, int line, int column) {
        this.valor = valor;
        this.tipo = tipo;
        this.line = line;
        this.column = column;
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
        return "Tokenizer{" + "valor= " + valor + ", tipo=" + tipo + ", line=" + line + ", column=" + column + '}';
    }
    
}