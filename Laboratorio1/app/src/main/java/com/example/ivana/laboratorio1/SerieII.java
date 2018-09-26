package com.example.ivana.laboratorio1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class SerieII extends AppCompatActivity {

    ListView listaCompresiones;
    TextView mensaje;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_ii);
        listaCompresiones = (ListView) findViewById(R.id.Lista);
        List<MostrarCompresiones> ListaCompresiones = new ArrayList<MostrarCompresiones>();


        //region Mostrar compresiones huffman
        final File carpeta = new File("/storage/emulated/0/Download/MisArchivos");
        List<String> listaDirectorios = new ArrayList<String>();
        listaDirectorios = listadoDirectoriosCarpeta2(carpeta);
        Collections.sort(listaDirectorios);
        Collections.reverse(listaDirectorios);

        final File carpeta2 = new File("/storage/emulated/0/Download/MisCompresiones");
        List<String> listaDirectorios2 = new ArrayList<String>();
        listaDirectorios2 = listadoDirectoriosCarpeta2(carpeta2);
        Collections.sort(listaDirectorios2);
        Collections.reverse(listaDirectorios2);

        List<String> listaDirectorios3 = new ArrayList<String>();
        listaDirectorios3 = listadoDirectoriosCarpeta(carpeta2);
        Collections.sort(listaDirectorios3);
        Collections.reverse(listaDirectorios3);

        List<String> listaDirectorios4 = new ArrayList<String>();
        listaDirectorios4 = listadoDirectoriosCarpeta(carpeta);
        Collections.sort(listaDirectorios4);
        Collections.reverse(listaDirectorios4);


        List<String> RazonDeCompresion = new ArrayList<String>();
        List<String> FactorDeCompresion = new ArrayList<String>();
        List<String> PorcentajeDeReduccion = new ArrayList<String>();

if(listaDirectorios.size()>0)
{
        for (int i = 0; i < listaDirectorios.size(); i++) {
            RazonDeCompresion.add(RazonDeCompresion(listaDirectorios3.get(i), listaDirectorios4.get(i)) + "");
        }
        for (int i = 0; i < listaDirectorios.size(); i++) {
            PorcentajeDeReduccion.add(PorcentajeDeReduccion(listaDirectorios3.get(i), listaDirectorios4.get(i)) + "");
        }
        for (int i = 0; i < listaDirectorios.size(); i++) {
            FactorDeCompresion.add(FactorDeCompresion(listaDirectorios4.get(i), listaDirectorios3.get(i)) + "");
        }
        for (int i = 0; i < listaDirectorios.size(); i++) {

            if(listaDirectorios2.get(i).endsWith(".LZW"))
            {
                ListaCompresiones.add(new MostrarCompresiones("Algortimo de compresion LZW"+"\n"+"Nombre de Archivo: " + listaDirectorios.get(i), "Nombre Archivo Comprimido: " + listaDirectorios2.get(i) + "\n" + "Ruta Archivo: " + listaDirectorios3.get(i), "Razon de compresion: " + RazonDeCompresion.get(i), "Factor de compresion: " + FactorDeCompresion.get(i), "Porcentaje de reduccion: " + PorcentajeDeReduccion.get(i) + " %"));
            }
            if(listaDirectorios2.get(i).endsWith(".huff"))
            {
                ListaCompresiones.add(new MostrarCompresiones("Algortimo de compresion de huffman"+"\n"+"Nombre de Archivo: " + listaDirectorios.get(i), "Nombre Archivo Comprimido: " + listaDirectorios2.get(i) + "\n" + "Ruta Archivo: " + listaDirectorios3.get(i), "Razon de compresion: " + RazonDeCompresion.get(i), "Factor de compresion: " + FactorDeCompresion.get(i), "Porcentaje de reduccion: " + PorcentajeDeReduccion.get(i) + " %"));
            }

        }
}
//endregion

        adapter = new ListAdapter(getApplicationContext(),ListaCompresiones);
        listaCompresiones.setAdapter(adapter);
    }


    public List<String> listadoDirectoriosCarpeta(final File carpeta) {
        List<String> lista = new ArrayList<String>();

        for (final File ficheroEntrada : carpeta.listFiles()) {
            if (ficheroEntrada.isDirectory()) {
                listadoDirectoriosCarpeta(ficheroEntrada);
            } else {
                lista.add(carpeta +"/"+ ficheroEntrada.getName());
            }
        }
        return lista;
    }

    public List<String> listadoDirectoriosCarpeta2(final File carpeta) {
        List<String> lista = new ArrayList<String>();

        for (final File ficheroEntrada : carpeta.listFiles()) {
            if (ficheroEntrada.isDirectory()) {
                listadoDirectoriosCarpeta(ficheroEntrada);
            } else {
                lista.add(ficheroEntrada.getName());
            }
        }
        return lista;
    }

    public static String RazonDeCompresion(String rutaDescompreso, String rutaCompreso){
        double razonCompresion= 0;
        File fileCompreso = new File (rutaCompreso);
        File fileDescompreso = new File (rutaDescompreso);
        double bytesfileCompreso = fileCompreso.length();
        double bytesfileDescompreso = fileDescompreso.length();
        razonCompresion = (bytesfileCompreso)/(bytesfileDescompreso);
        return String.format("%.3f", razonCompresion);
    }

    public static long CalcularNumBytes(String ruta){
        File file = new File (ruta);
        //numero de Kilobytes
        long Bytes=file.length();
        return Bytes;
    }

    public static String PorcentajeDeReduccion(String rutaDescompreso, String rutaCompreso){
        double porcentajeReduccion= 0;
        double numerador=0;

        File fileCompreso = new File (rutaCompreso);
        File fileDescompreso = new File (rutaDescompreso);
        double bytesfileCompreso = fileCompreso.length();
        double bytesfileDescompreso = fileDescompreso.length();

        porcentajeReduccion = ((bytesfileDescompreso - bytesfileCompreso)/(bytesfileDescompreso + bytesfileCompreso))*100;
        if(porcentajeReduccion <0)
        {
            porcentajeReduccion = porcentajeReduccion*-1;
        }

        return String.format("%.3f", porcentajeReduccion);
    }

    public static String FactorDeCompresion(String rutaDescompreso, String rutaCompreso){
        double razonCompresion= 0;
        File fileCompreso = new File (rutaCompreso);
        File fileDescompreso = new File (rutaDescompreso);
        double bytesfileCompreso = fileCompreso.length();
        double bytesfileDescompreso = fileDescompreso.length();
        razonCompresion = (bytesfileCompreso)/(bytesfileDescompreso);
        return String.format("%.3f", razonCompresion);
    }

}
