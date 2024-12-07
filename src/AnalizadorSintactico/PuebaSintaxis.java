/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorSintactico;
import AnalizadorLexico.Errors;
import AnalizadorLexico.Lexer;
import AnalizadorLexico.Token;
import AnalizadorSintactico.Parser;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author hp
 */
public class PuebaSintaxis {
 


    public static void main(String[] args) {
        // Entrada de prueba
        String entrada = """
        ASIGNACION { 
            RECURSOS = [Trabajador1, Trabajador2, Trabajador3];
            TAREAS = [TareaA, TareaB, TareaC];
            COSTOS = [
                [4, 8,9],
                [5, 12, 7],
                [9, 3, 10.24]
            ];
            MINIMIZAR;
        }
        """;

        // Paso 1: Análisis léxico
        Lexer lexer = new Lexer();
        List<Errors> erroresLexicos = new ArrayList<>();
        List<Token> tokens = lexer.lex(entrada);

        // Mostrar tokens generados por el analizador léxico
        System.out.println("Tokens generados por el analizador léxico:");
        for (Token token : tokens) {
            System.out.println(token);
        }

        // Mostrar errores léxicos
        if (!erroresLexicos.isEmpty()) {
            System.out.println("\nErrores lexicos encontrados:");
            for (Errors error : erroresLexicos) {
                System.out.println(error);
            }
            return; // Termina si hay errores léxicos
        }

        // Paso 2: Análisis sintáctico
        List<Errors> erroresSintacticos = new ArrayList<>();
        Parser parser = new Parser(tokens, erroresSintacticos);
        parser.parse();

        // Mostrar errores sintácticos
        if (!erroresSintacticos.isEmpty()) {
            System.out.println("\nErrores sintacticos encontrados:");
            for (Errors error : erroresSintacticos) {
                System.out.println(error);
            }
        } else {
            System.out.println("\nAnalisis sintactico completado sin errores.");
        }
    }
    
}
   

