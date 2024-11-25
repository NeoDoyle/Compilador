package AnalizadorLexico;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hp
 */
public class Lexer {
    private final List<Errors> errores = new ArrayList<>();

    public List<Token> lex(String entrada) {
        List<Token> tokens = new ArrayList<>();
        String[] lineas = entrada.split("\n");

        int numLinea = 1; // Contador de líneas
        for (String linea : lineas) {
            int col = 0;

            // Ignora los comentarios
            linea = linea.replaceAll("//.*$", "");
            // Remueve espacios extras al inicio
            linea = linea.stripLeading();

            // Mientras la línea no esté vacía
            while (!linea.isEmpty()) {
                boolean bandera = false;

                // Validación previa para patrones no válidos (número seguido de letras, como "2RECURSOS")
                Pattern patronInvalido = Pattern.compile("^\\d+[a-zA-Z_]");
                Matcher matcherInvalido = patronInvalido.matcher(linea);

                if (matcherInvalido.find()) {
                    String tokenNoValido = matcherInvalido.group(); // Captura el token problemático
                    String msj = "Token no reconocido: Identificador no válido, comienza con un número: " + tokenNoValido;
                    errores.add(new Errors(linea, msj, numLinea, col));

                    // Elimina el token inválido
                    int longitudInvalida = tokenNoValido.length();
                    linea = linea.substring(longitudInvalida).stripLeading();
                    col += longitudInvalida;
                    bandera = true;
                    continue;
                }

                // Procesar tokens válidos
                for (Tokens tipo : Tokens.values()) {
                    Pattern patron = Pattern.compile("^" + tipo.getPatron());
                    Matcher coincidencia = patron.matcher(linea);

                    if (coincidencia.find()) {
                        String valor = coincidencia.group(); // Almacena el token encontrado
                        Token token = new Token(valor, tipo, numLinea, col);
                        tokens.add(token);

                        // Avanza en la línea
                        linea = linea.substring(coincidencia.end()).stripLeading();
                        col += coincidencia.end(); // Actualiza la columna
                        bandera = true;
                        break; // Sale del bucle después de encontrar un token válido
                    }
                }

                // Si no se encontró ningún token válido
                if (!bandera) {
                    char tnoValido = linea.charAt(0);
                    String msj = "Token no reconocido: " + tnoValido;
                    errores.add(new Errors(linea, msj, numLinea, col));

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
