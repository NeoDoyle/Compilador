/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

import java.util.List;


/**
 *
 * @author hp
 */
public class Pruebas {
    public static void main(String[] args) {
        // Código de ejemplo para ser analizado
        String codigo = """
            ASIGNACION { 
                            RECURSOS = [Trabajador1, Trabajador2, Trabajador3];
                            TAREAS = [TareaA, TareaB, TareaC];
                            COSTOS = [
                                [4, 8, 6], // Costos para Trabajador1
                                [5, 12,            7], // Costos para Trabajador2
                                [9,3, 10.24] // Costos para Trabajador3
                            ];
                            MINIMIZAR; // Objetivo del problema
                            SOLVE; // Indica que el problema se debe resolver
                        }
                        
                        """;

        // Crear una instancia del lexer y analizar el código de entrada
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.lex(codigo);

        // Imprimir los tokens encontrados
        System.out.println("Tokens encontrados:");
        for (Token token : tokens) {
            System.out.println(token);
        }

        // Imprimir los errores léxicos encontrados
        System.out.println("\nErrores encontrados:");
        List<Errors> errores = lexer.getErrores();
        for (Errors error : errores) {
            System.out.println(error);
        }
    }
    
}
