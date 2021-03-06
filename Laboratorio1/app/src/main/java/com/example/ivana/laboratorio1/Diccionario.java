package com.example.ivana.laboratorio1;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class Diccionario {

    //Variables
    private Tabla diccionario[] = new Tabla[1000];
    public static List<String> listaDescompresa = new ArrayList<String>();
    public int size = 0 ;

    //Constructor
    public Diccionario() {
    }

    //Metodos

    public int Size(){
        return size;
    }
    public void Agregar(Tabla tabla) {
        diccionario[size] = tabla;
        size++;
    }

    public Tabla ObtenerTabla(int index) {
        return diccionario[index];
    }

    public int Buscar(String subString) throws IOException {
        for(int i=0; i < size; ++i) {
            String tabla = diccionario[i].ObtenerTabla();
            if(tabla.contains(subString))
                return i;
        }
        return 0;
    }

    //AYUDA PARA DESCOMPRIMIR

    public String BuscarPorIndex(int index) throws IOException {
        return diccionario[index].ObtenerCadena();
    }
    public void CrearArchivo (String cadena){
        try {
            String ruta = "C:/MEIA/usuario.txt";
            String contenido = cadena;

            // Si el archivo no existe es creado
            if (!new File(ruta).exists()) {
                new File(ruta).createNewFile();
            }
            BufferedWriter out  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ruta),true),"UTF8"));
            out.write(contenido+System.getProperty("line.separator"));
            out.close();
        } catch (Exception e) {
        }
    }

}