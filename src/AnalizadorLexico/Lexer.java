package AnalizadorLexico;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 
 * @author hp
 */
public class Lexer {
    private final List<Errors> errores = new ArrayList<>();

    public List<Token> lex(String entrada) {
        List<Token> tokens = new ArrayList<>();
        String[] lineas = entrada.split("\n");

        int numLinea = 1; //contador de líneas
        for (String linea : lineas) {
            int col = 0;

            //ignora los comentarios
            linea = linea.replaceAll("//.*$", "");
            // Remueve espacios extras al inicio
            linea = linea.stripLeading();

            // mientras la línea no este vacia
            while (!linea.isEmpty()) {
                boolean bandera = false;

               // validacion previa para patrones no validos nume seguido de letras corrgir  7RECURSOS
                Pattern patronInvalido = Pattern.compile("^\\d+[a-zA-Z_]+");
                Matcher matcherInvalido = patronInvalido.matcher(linea);

                if (matcherInvalido.find()) {
                    String tokenNoValido = matcherInvalido.group(); // captura todo el token problematico
                    String msj = "Token no reconocido: Identificador no válido, comienza con un número: " + tokenNoValido;
                    errores.add(new Errors(tokenNoValido, msj, numLinea, col));

                    // avanza la longitud completa del token no valido
                    int longitudInvalida = tokenNoValido.length();
                    linea = linea.substring(longitudInvalida).stripLeading();
                    col += longitudInvalida;
                    bandera = true;
                    continue;
                }

                // Procesar tokens validos
                for (Tokens tipo : Tokens.values()) {
                    Pattern patron = Pattern.compile("^" + tipo.getPatron());
                    Matcher coincidencia = patron.matcher(linea);

                    if (coincidencia.find()) {
                        String valor = coincidencia.group(); //almacena el token encontrado
                        Token token = new Token(valor, tipo, numLinea, col);
                        tokens.add(token);

                        // avanza en la line
                        linea = linea.substring(coincidencia.end()).stripLeading();
                        col += coincidencia.end(); // Actualiza la columna
                        bandera = true;
                        break; // sale del bucle despues de encontrar un token valido
                    }
                }

                // Si no se encontro ningún token valido
                if (!bandera) {
                    char tnoValido = linea.charAt(0);
                    String msj = "Token no reconocido: " + tnoValido;
                    errores.add(new Errors(String.valueOf(tnoValido), msj, numLinea, col));

                    // Avanza un carácter
                    linea = linea.substring(1);
                    col++;
                }
            }
            numLinea++;
        }
        return tokens;
    }

    // Getter para los errores
    public List<Errors> getErrores() {
        return errores;
    }
}