
/** Esta es la clase servidor.
 *
 * @author Sara Chamseddine, Juan Palomino
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
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Serverpc {

    /**
     * Puerto
     */
    private final static int PORT = 5000;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        /*
        String archivo = "";
        String directorio = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-a")) {
                archivo = args[i + 1];
            } else {
                if (args[i].equals("-d")) {
                    directorio = args[i + 1];
                }
            }
        }
        
         System.out.println("Archivo: " + archivo);
        System.out.println("Directorio: " + directorio);
         */
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
                int contador = 0;

                String request = "incio";
                String strOutput = "";

                while (!request.equals("")) {
                    request = input.readLine();
                    String test = request.substring(0, 3);
                    if (test.equals("GET")) {
                        strOutput = process(request, "archivoDePalabras.txt");
                    }

                    System.out.println("" + request + "");
                }
                //se procesa la peticion y se espera resultado
//                String strOutput = process(request);  
//                //String strOutput = "HTTP/1.1 200 OK\n"
//                //System.out.println("Servidor> Resultado de petición");
//                //System.out.println("Servidor> \"" + strOutput + "\"");
//                //se imprime en cliente
//                output.flush();//vacia contenido
//                output.println("HTTP/1.1 200 OK\n");
//                output.println("");
//                output.println("<html>\n"
//                        + "<body>\n"
//                        + "<h1>Hello, World!</h1>\n"
//                        + "</body>\n"
//                        + "</html>");

                //cierra conexion
                clientSocket.close();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * procesa peticion del cliente y retorna resultado
     *
     * @param request peticion del cliente
     * @return String
     */
    public static String process(String request, String fileName) {
        
        StringBuffer result = new StringBuffer("");

        //String[] palabrasClave = ((request.split(" "))[1]).substring(1).split("%20");
        
        // Añadir primera linea
        request = ((request.split(" "))[1]).substring(1);
        
        result.append("HTTP/1.1 200 OK\n\n");

        ArrayList<String> palabrasProhibidas = readFile(fileName);

        for (String palabraProhibida : palabrasProhibidas) {
            if (request.contains(palabraProhibida)) {
                return displayError(palabraProhibida);
            }
        }
        return displayWebsite(request);
    }

    public static String displayError(String palabraProhibida) {
        return "error";
    }

    public static String displayWebsite(String palabraProhibida) {
        return "";
    }

    public static ArrayList<String> readFile(String fileName) {
        try {
            File file = new File("\\src\\archivoDePalabras.txt");
            Scanner sc = new Scanner(file);
            ArrayList<String> palabras = new ArrayList<String>();
            while (sc.hasNextLine()) {
                palabras.add(sc.nextLine());
            }
            return palabras;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Serverpc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
