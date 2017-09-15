
/** Esta es la clase servidor.
 *
 * @author Sara Chamseddine, Juan Palomino
 * @since 20/09/2017
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class Serverpc {

    /**
     * Puerto
     */
    private final static int PORT = 5000;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

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
                    if (((String)request.substring(0, 2)).equals("GET")){
                        strOutput = process(request);
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
    public static String process(String request) {
        String result = "";
        String[] palabrasClave = request.split("%20");
        
        ArrayList<String> palabrasProhibidas = new ArrayList<String>();
        palabrasProhibidas.add("tonto");
        palabrasProhibidas.add("feo");
        palabrasProhibidas.add("malo");
        
        for (String palabrasProhibida : palabrasProhibidas) {
            
        }
        if (request)
        if (request != null) {
            switch (request) {
                case "frase":
                    Collections.shuffle(phrasesList);
                    result = phrasesList.get(0);
                    break;
                case "libro":
                    Collections.shuffle(booksList);
                    result = booksList.get(0);
                    break;
                case "exit":
                    result = "bye";
                    break;
                default:
                    result = "La peticion no se puede resolver.";
                    break;
            }
        }
        return result;
    }

}
