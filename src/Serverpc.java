
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
                for (int num = 1; num < 30; num++) {
                    String request = input.readLine();
                    System.out.println("" + request + "");
                }
                //se procesa la peticion y se espera resultado
                // String strOutput = process(request);  
                //String strOutput = "HTTP/1.1 200 OK\n"
                //System.out.println("Servidor> Resultado de petición");
                //System.out.println("Servidor> \"" + strOutput + "\"");
                //se imprime en cliente
                output.flush();//vacia contenido
                output.println("HTTP/1.1 200 OK\n");
                output.println("");
                output.println("<html>\n"
                        + "<body>\n"
                        + "<h1>Hello, World!</h1>\n"
                        + "</body>\n"
                        + "</html>");

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
        //frases
        String[] phrases = {
            "La tecnología se alimenta a si misma. La tecnología hace posible más tecnología.-Alvin Toffler.",
            "La tecnología es sólo una herramienta. En términos de llevar a los niños a trabajar juntos y motivarlos, el profesor es el más importante.-Bill Gates.",
            "La máquina tecnológicamente más eficiente que el hombre ha inventado es el libro.-Northrop Frye.",
            "Ya no hacen más los bugs como bunny - Olav Mjelde",
            "Un lenguaje de programación es de bajo nivel cuando requiere que prestes atencion a lo irrelevante.-Alan J. Perlis.",
            "Hablar es barato. Enséñame el código.-Linus Torvalds ",
            "No me importa si funciona en su máquina! No me envían su máquina!.-Vidiu Platon",
            "Siempre codifica como si la persona que finalmente mantendrá tu código fuera un psicópata violento que sabe dónde vives.-Martin Golding"};
        ArrayList<String> phrasesList = new ArrayList<>();
        Collections.addAll(phrasesList, phrases);
        //libros
        String[] books = {
            "Divina Comedia - Dante Alighieri",
            "Don Quijote de la Mancha - Miguel de Cervantes",
            "Cien años de soledad - Gabriel García Márquez",
            "Moby Dick - Herman Melville",
            "Ana Karenina - Lev Tolstói",
            "Eneida - Virgilio",
            "Otelo - William Shakespeare",
            "El viejo y el mar - Ernest Hemingway",
            "Orgullo y prejuicio - Jane Austen"};
        ArrayList<String> booksList = new ArrayList<>();
        Collections.addAll(booksList, books);

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
