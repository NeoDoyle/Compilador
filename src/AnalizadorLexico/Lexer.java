/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

import java.util.ArrayList;
import java.util.*;
import java.util.regex.*;


/**
 *
 * @author hp
 */
public class Lexer {
    private  final List<Errors> errores = new ArrayList<>();
    
    public List<Token> lex(String entrada){
        List<Token> tokens = new  ArrayList<>();
        String[] lineas = entrada.split("\n");
        
        int numLinea =1;
        for (String linea : lineas) {
            int col=0;
            
            //ignora los comentarios 
            linea = linea.replaceAll("//.*$", "");
            //ignora los tabuladores de linea 
            linea = linea.replaceAll("  ", "");
            
            //mientras no este vacia
            while (!linea.isEmpty()) {
                boolean bandera = false;
                
                //encontar los tokens 
                for (Tokens tipo : Tokens.values()) {
                    Pattern patron = Pattern.compile("^"+ tipo.getPatron());
                    Matcher conicidencia = patron.matcher(linea);
                    
                    if (conicidencia.find()) {
                        String valor = conicidencia.group();//almacena la infor del token encintrado 
                        Token token = new Token(valor, tipo, numLinea, col);
                        tokens.add(token);
                        
                        
                        linea = linea.substring(conicidencia.end()).stripLeading();
                        col += conicidencia.end(); // actualizar la columna después de encontrar el token
                        bandera = true;
                        break; // salir del bucle después de encontrar un token válido
                    }
                    
                }
                
                //token no valido 
                if (!bandera) {
                    String msj = "Token no reconocido";
                    errores.add(new Errors(linea,msj,numLinea,col));
                    break; // pasa a siguiente liensa 
                }                
            }
            numLinea++;
        }
        return tokens;
        
    }//entrada

    
    //getters 
    public List<Errors> getErrores() {
        return errores;
    }
    
}//clase lexer 
