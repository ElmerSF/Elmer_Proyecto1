/*
UNED Informática Compiladores 3307
Estudiante Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para manejo de los archivos 
-Leer el archivo .vb línea por línea
- Crear el archivo .log con numeración de 4 dígitos
-Escribe los errores
*/


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

public String[] leerArchivo(String nombreArchivo) {

        try {
            File archivo = new File(nombreArchivo);

            if (!archivo.exists()) {
                System.out.println("El archivo indicado no existe: " + nombreArchivo);
                return null;
            }
            //areglo de lienas
            ArrayList<String> lineas = new ArrayList<>();

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            // Leemos línea por línea SIN modificar espacios ni formato
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }

            br.close();
                //se retorna un arreglo de líneas
            return lineas.toArray(new String[0]);

        } catch (Exception e) {
            System.out.println("!!!Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }
public String crearArchivoLog(String nombreArchivo, String[] lineas) {

        try {
            // Obtener nombre base sin extensión
            String nombreBase;

            int punto = nombreArchivo.lastIndexOf('.');
            if (punto > 0) {
                nombreBase = nombreArchivo.substring(0, punto);
            } else {
                nombreBase = nombreArchivo;
            }

            String nombreLog = nombreBase + "-errores.log";

            FileWriter fw = new FileWriter(nombreLog, false); // false = sobrescribir
            PrintWriter pw = new PrintWriter(fw);

            // <editor-fold defaultstate="collapsed" desc="Intento para manejar la numeración del archivo">
 /*
                  if (contador < 8) {

                    pendiente = (pendiente + " " + revi.AnalizaTexto(linea)+" "+revi.ada());
                    reglonerror.println("0000" + contador + " " + linea + " ");

                    contador++;
                } else {
                    if (contador == 8) {
                        Respuesta = (Respuesta + revi.AnalizaTexto(linea));
                        reglonerror.println("0000" + contador + " " + linea + " " + pendiente + Respuesta);
                        contador++;
                    } else {

                        if (contador <10) {
                            Respuesta = (Respuesta + revi.AnalizaTexto(linea));
                            reglonerror.println("0000" + contador + " " + linea + " " + Respuesta+" "+revi.ada());
                            contador++;
                        } else {
                            if (contador < 100) {
                                Respuesta = revi.AnalizaTexto(linea);
                                reglonerror.println("000" + contador + " " + linea + " " + Respuesta+" "+revi.ada());
                                contador++;
                            } else {
                                if (contador < 1000) {
                                    Respuesta = revi.AnalizaTexto(linea);
                                    reglonerror.println("00" + contador + " " + linea + " " + Respuesta+" "+revi.ada());
                                    contador++;
                                } else {
                                    if (contador < 10000) {
                                        Respuesta = revi.AnalizaTexto(linea);
                                        reglonerror.println("0" + contador + " " + linea + " " + Respuesta+" "+revi.ada());
                                        contador++;
                                    } else {
                                        Respuesta = revi.AnalizaTexto(linea);
                                        reglonerror.println(contador + " " + linea + " " + Respuesta+" "+revi.ada());
                                        contador++;

                                    }
                                }
                            }
                        }
                    }
                }

            
            */
// </editor-fold>
   
            // Escribimos cada línea con numeración de 4 dígitos
            for (int i = 0; i < lineas.length; i++) {
                int numeroLinea = i + 1;
                String numeroFormateado = String.format("%04d", numeroLinea);
                String contenido = lineas[i];

                // Respetar exactamente el contenido original
                pw.println(numeroFormateado + " " + contenido);
            }

            pw.flush();
            pw.close();
            fw.close();

            return nombreLog;

        } catch (Exception e) {
            System.out.println("Error al crear el archivo .log: " + e.getMessage());
            return null;
        }
    }
 public void escribirErrores(String nombreLog, ErrorManager errorManager) {

        try {
            if (nombreLog == null) {
                System.out.println("No se puede escribir errores: archivo .log nulo.");
                return;
            }

            List<Error> errores = errorManager.getErrores();

            // Si no hay errores, no agregamos nada extra
            if (errores == null || errores.isEmpty()) {
                return;
            }

            // Abrimos el .log en modo append (true)
            FileWriter fw = new FileWriter(nombreLog, true);
            PrintWriter pw = new PrintWriter(fw);

            pw.println();
            pw.println("------------------------------------------------------------");
            pw.println("ERRORES DETECTADOS:");
            pw.println("------------------------------------------------------------");

            for (Error err : errores) {
                // Formato sugerido:
                // Error 200. Línea 0001. Descripción...
                String lineaFormateada = String.format("%04d", err.getLinea());
                pw.println("Error " + err.getNumero() + ". Línea " + lineaFormateada + ". " + err.getDescripcion());
            }

            pw.flush();
            pw.close();
            fw.close();

        } catch (Exception e) {
            System.out.println("Error al escribir errores en el .log: " + e.getMessage());
        }
    }
    
}
