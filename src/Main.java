/*
 /*
UNED Informática Compiladores 3307
Estudiante: Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase principal donde se inicia


ASCII art basado en colecciones anónimas de:
ASCII Art Archive. (n.d.). ASCII Art Gallery. https://www.asciiart.eu/gallery
Modificado para uso en el Proyecto 1 de Compiladores (UNED).

 */
public class Main {
    
    //obtenemos el directorio actual
    static String directorio = System.getProperty("user.dir");

    public static void main(String[] args) {

        // Mensaje de bienvenida
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        System.out.println("\t Analizador Léxico   Proyecto 1 UNED Estudiante: Elmer Salazar (3-426-158)");
        System.out.println("--------------------------------------------------------------------------------------------------------------\n");

        //si no se recibe ningún documento
        if (args.length == 0) {
            System.out.println("No se indicó ningún archivo como argumento.");
            return;
        }

        String archivo = args[0];

        //si el archivo no tiene la extensión vb la correcta
        if (!archivo.toLowerCase().endsWith(".vb")) {
            System.out.println("El archivo debe tener extensión .vb");
            return;
        }

        System.out.println("Ubicación actual: " + directorio);
        System.out.println("Archivo recibido: " + archivo + "\n");

        // ASCII ART
        System.out.println("\033[32m                          .-\"\"\"-.");
        System.out.println("\033[32m                         / .===. \\");
        System.out.println("\033[32m                         \\/ 6 6 \\/");
        System.out.println("\033[32m                         ( \\___/ )");
        System.out.println("\033[32m    _________________ooo__\\_____/_____________________");
        System.out.println("\033[32m   /                                                  \\");
        System.out.println("\033[36m   |ANALIZADOR LÉXICO   Proyecto 1 Compiladores 03307  |");
        System.out.println("\033[32m   \\______________________________ooo_________________/");
        System.out.println("\033[32m                         |  |  |");
        System.out.println("\033[32m                         |_ | _|");
        System.out.println("\033[32m                         |  |  |");
        System.out.println("\033[32m                         |__|__|");
        System.out.println("\033[32m                         /-'Y'-\\");
        System.out.println("\033[32m                        (__/ \\__)\n");
        System.out.println("\033[0m");
    
    
    
    }
    
}
