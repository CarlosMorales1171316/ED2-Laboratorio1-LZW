package com.example.ivana.laboratorio1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tabla {

    //Variables
    private String cadena;
    private int index;

    //Constructor
    public Tabla() {
    }
    public Tabla(String Cadena, int Index) {
        cadena = Cadena;
        index = Index;
    }

    //Metodos
    public String ObtenerTabla() throws IOException {
        String strTemporal = (cadena + " , " + index);
        return strTemporal;
    }


    public int ObtenerIndex() {
        return index;
    }

    public String ObtenerCadena(){
        return cadena;

    }

}
