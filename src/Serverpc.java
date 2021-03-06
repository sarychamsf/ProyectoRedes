
/** Esta es la clase servidor.
 *
 * @author Sara Chamseddine, Juan Palomino.
 * @since 20/09/2017
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Serverpc {

    private final static int PORT = 5000;
    private static String archivo = "";
    private static String directorio = "";

    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-a")) {
                archivo = args[i + 1];
            } else {
                if (args[i].equals("-d")) {
                    directorio = args[i + 1];
                }
            }
        }

        try {

            //Socket de servidor para esperar peticiones de la red
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor> Servidor iniciado");
            System.out.println("Servidor> En espera de cliente...");
            //Socket de cliente
            Socket clientSocket;
            while (true) {

                //en espera de conexion, si existe la acepta
                clientSocket = serverSocket.accept();
                //Para leer lo que envie el cliente
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //para imprimir datos de salida                
                PrintStream output = new PrintStream(clientSocket.getOutputStream());

                //se lee peticion del cliente
                String request = "incio";
                String strOutput = "";

                while (!request.equals("")) {
                    request = input.readLine();
                    try {
                        String test = request.substring(0, 3);
                        if (test.equals("GET")) {
                            strOutput = process(request);
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error.");
                    }

                    System.out.println("" + request + "");
                }

                output.flush();
                output.println(strOutput);

                clientSocket.close();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Procesa petición del cliente y retorna un resultado.
     *
     * @param request petición del cliente.
     * @return String
     */
    public static String process(String request) {

        StringBuffer result = new StringBuffer("");

        request = ((request.split(" "))[1]).substring(1);
        result.append("HTTP/1.1 200 OK\n\n");

        ArrayList<String> palabrasProhibidas = readFile();

        for (String palabraProhibida : palabrasProhibidas) {
            if (request.contains(palabraProhibida)) {
                return result.append(displayError(palabraProhibida)).toString();
            }
        }
        return result.append(displayWebsite(request)).toString();
    }

    /**
     * Este método muestra un mensaje diciendo que la página está prohibida si
     * el request contiene alguna palabra escrita en el documento .txt
     *
     * @param palabraProhibida
     * @return página HTML que muestra un mensaje especificando que la página
     * está prohibida por contener la palabra.
     */
    public static String displayError(String palabraProhibida) {
        return ("<!DOCTYPE html>\n"
                + "<html lang=\"es\" dir=\"ltr\">\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "</head>\n"
                + "<body>\n"
                + "<header> <h1>La página solicitada está prohibida por contener la palabra: " + palabraProhibida + ".</h1> </header>\n"
                + "Por: Sara Chamseddine y Juan Palomino."
                + "</body>\n"
                + "</html>");
    }

    /**
     * Muestra la página web solicitada por el cliente (Google Chrome).
     *
     * @param address dirección que se escribe en la URL (request)
     * @return
     */
    public static String displayWebsite(String address) {
        try {
            File file = new File(directorio + "/" + address + ".html");
            Scanner sc = new Scanner(file);
            StringBuffer page = new StringBuffer("");
            while (sc.hasNextLine()) {
                page.append(sc.nextLine()).append("\n");
            }
            return page.toString();
        } catch (FileNotFoundException ex) {
            return ("<!DOCTYPE html>\n"
                    + "<html lang=\"es\" dir=\"ltr\">\n"
                    + "<head>\n"
                    + "<meta charset=\"UTF-8\">\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "<header> <h1>404. Página no encontrada.</h1> </header>\n"
                    + "Por: Sara Chamseddine y Juan Palomino."
                    + "</body>\n"
                    + "</html>");
        }
    }

    /**
     * Lee el archivo de texto que contiene las palabras prohibidas.
     *
     * @return arreglo que contiene las palabras prohibidas.
     */
    public static ArrayList<String> readFile() {
        ArrayList<String> palabras = new ArrayList<>();
        try {
            File file = new File(archivo);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                if (!linea.equals("fin")) {
                    palabras.add(linea);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("El archivo no ha sido encontrado.");
        }
        return palabras;
    }

}
