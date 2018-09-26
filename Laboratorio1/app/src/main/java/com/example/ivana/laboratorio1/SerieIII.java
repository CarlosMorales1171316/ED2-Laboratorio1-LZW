package com.example.ivana.laboratorio1;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class SerieIII extends AppCompatActivity {

     TextView MostrarCompresion,MostrarTablaCompleta,MostrarRuta;
     Button Comprimir,Descomprimir,EscogerRuta;
     int opcion =0,validar=0;
     private static final int PERMISSION_REQUEST_STORAGE = 1000;
     private static final int READ_REQUEST_CODE = 42;
     boolean condicion= false;
     private String TAG;
     public String rut;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_iii);
        MostrarCompresion = (TextView) (findViewById(R.id.MostrarArchivoCompreso));
        MostrarTablaCompleta =(TextView) (findViewById(R.id.MostrarTablaCompleta));
        MostrarRuta = (TextView) (findViewById(R.id.MostrarRutaArchivo));
        Comprimir =(Button) (findViewById(R.id.Boton_Comprimir));
        EscogerRuta =(Button) (findViewById(R.id.Boton_Ruta));
        Descomprimir = (Button)(findViewById(R.id.Boton_Descomprimir));
        LZW _lzw = new LZW();

            EscogerRuta.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MostrarCompresion.setText("");
                    MostrarTablaCompleta.setText("");
                    MostrarRuta.setText("");
                    opcion=0;
                    validar = 1;
                    performFileSearch();
                }
            });
            Comprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    MostrarCompresion.setText("");
                    MostrarTablaCompleta.setText("");
                    MostrarRuta.setText("");
                    opcion=1;
                    performFileSearch();
                }
        });
        Descomprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    MostrarCompresion.setText("");
                    MostrarTablaCompleta.setText("");
                    MostrarRuta.setText("");
                    opcion=2;
                    performFileSearch();
                }
        });

    }

    private void performFileSearch() {
        if(opcion==1)
        {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/*");
            startActivityForResult(intent, READ_REQUEST_CODE);
        }
        if(opcion ==2 || opcion ==0) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, READ_REQUEST_CODE);
        }
    }
    //leer el txt File
    private String leerTextFile (String input){
        File file = new File(Environment.getExternalStorageDirectory(), input);
        StringBuilder text = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String linea;
            while((linea = br.readLine())!=null){
                text.append(linea);
                text.append("\n");
            }
            br.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return text.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                if (path.contains("emulated")) {
                    path = path.substring(path.indexOf("0") + 1);
                }
                Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();
                String actual ="";
                if(opcion == 0)
                {
                    //region Obtener ruta para almacenar archivo
                    String ruta="/storage/emulated/0/"+path;
                    String[] nuevaRuta =ruta.split("/");
                    for(int i=0;i<nuevaRuta.length;i++)
                    {

                        int tempo = nuevaRuta.length-1;
                        if(tempo!=i)
                        {
                            actual += nuevaRuta[i]+"/";
                        }
                    }
                    LZW lzw = new LZW();
                    lzw.CrearArchivo( "/storage/emulated/0/Documents/ContadoresLaboratorio1/RutaArchivo.txt",actual);
                    MostrarCompresion.setText(actual);
                    //endregion
                }
                if (opcion == 1)
                {
                    if(validar==1)
                    {
                        //region Comprimir
                        String mensaje = Leer("/storage/emulated/0/" + path);
                        FileInputStream rutaEntrada = null;
                        String contenido = "";
                        try {
                            rutaEntrada = new FileInputStream("/storage/emulated/0/" + path);
                            char caracter;
                            while ((rutaEntrada.available()) > 0) {
                                caracter = (char) rutaEntrada.read();
                                contenido += caracter;
                            }
                            String nombre ="";
                            String[] pathNombre = path.split("/");
                            for(int i=0;i<pathNombre.length;i++)
                            {
                                int tempo = pathNombre.length-1;
                                if(tempo ==i)
                                {
                                    String[] NombreReal = pathNombre[i].split(".txt");
                                    nombre = NombreReal[0];
                                }
                            }
                            LZW lzw = new LZW(contenido);
                            String MostrarContenidoCompresion = lzw.Compresion(nombre);
                            lzw.ObtenerList(nombre);
                            lzw.ObtenerList2(nombre);
                            lzw.Descompresion();
                            String rutaArchivo = "/storage/emulated/0/Download/MisArchivos/";
                            lzw.CrearArchivo(rutaArchivo + "ArchivoCompreso(" + nombre + ").txt", mensaje);
                            String rutaComprimir = "/storage/emulated/0/Download/MisArchivos/";

                            LZW _lzw = new LZW();

                            String DevolverRuta = "";
                            _lzw.LeerArchivo("/storage/emulated/0/" + path);


                            String ruta2="/storage/emulated/0/"+path;
                            String[] nuevaRuta =ruta2.split("/");
                            for(int i=0;i<nuevaRuta.length;i++)
                            {

                                int tempo = nuevaRuta.length-1;
                                if(tempo!=i)
                                {
                                    actual += nuevaRuta[i]+"/";
                                }
                            }
                            if (_lzw.CaracteresDeEntrada.size() > 0) {
                                _lzw.compresion2();
                                String Final = _lzw.Leer("/storage/emulated/0/Documents/ContadoresLaboratorio1/RutaArchivo.txt");
                                String rutaFinal = _lzw.EscribirArchivo(nombre);
                                //region
                                try {
                                    new File(Final + "CompresionArchivoUsuario(" + nombre + ").lzw").createNewFile();
                                    FileWriter fw = new FileWriter(Final + "CompresionArchivoUsuario(" + nombre + ").lzw");
                                    BufferedWriter bw = new BufferedWriter(fw);
                                    bw.write(Leer(rutaFinal));
                                    bw.close();
                                }
                                catch(IOException e)
                                {
                                    e.printStackTrace();
                                }
                                //endregion
                                 MostrarRuta.setText(Final + "CompresionArchivoUsuario(" + nombre + ").lzw");
                            }

                            String rutaTablas = "/storage/emulated/0/Documents/TablasCompresionesLZW/";
                            String rutaMostrarTabla = rutaTablas + "TablaCompleta(" + nombre + ").txt";
                            String ruta3 = rutaComprimir + "ArchivoCompresoLZW(" + nombre + ").lzw";
                            MostrarCompresion.setText(Leer(MostrarContenidoCompresion));
                            MostrarTablaCompleta.setText(Leer(rutaMostrarTabla));
                        } catch (IOException e) {
                            if (rutaEntrada != null) {
                                try {
                                    rutaEntrada.close();
                                } catch (Exception f) {
                                    f.printStackTrace();
                                }
                            }
                        }
                        //endregion
                    }
                    if(validar ==0)
                    {
                        MostrarCompresion.setText("Debe de escojer una ruta para almacenar el archivo compreso.");
                    }
                }
                if (opcion == 2)
                {
                    if(validar ==1)
                    {
                        if(path.endsWith(".lzw"))
                        {

                            //region descomprimir

                            String rutaCompresa ="/storage/emulated/0/"+path;

                            //region
                            String nombre ="";
                            String[] pathNombre = path.split("/");
                            for(int i=0;i<pathNombre.length;i++)
                            {
                                int tempo = pathNombre.length-1;
                                if(tempo ==i)
                                {
                                    String[] NombreReal = pathNombre[i].split(".lzw");
                                    nombre = NombreReal[0];
                                }
                            }
                            String s="";
                            String nuevoNombre="";
                            for(int i=0;i<nombre.length();i++)
                            {
                                s = nombre.substring(i,i+1);
                                if(s.equals("("))
                                {
                                    for(int j=i+1;j<nombre.length();j++)
                                    {
                                        if(j+1!= nombre.length())
                                        {
                                            nuevoNombre+= nombre.substring(j,j+1);
                                        }
                                    }
                                    break;
                                }
                            }
                            //endregion
                            LZW _lzw = new LZW();
                            _lzw.LeerArchivoSalida(rutaCompresa);
                            _lzw.descompresion2();
                            String mensaje = _lzw.ImprimirSalida();
                            String rutaArchivoDescompreso="/storage/emulated/0/Download/MisDescompresiones/";
                            String rutaDescompresa=rutaArchivoDescompreso+"ArchivoDescompreso("+nuevoNombre+").txt";
                            String[] dividir = mensaje.split("null");
                            _lzw.CrearArchivo(rutaDescompresa, dividir[0]);
                            String rutaArchivoUsuario = Leer("/storage/emulated/0/Documents/ContadoresLaboratorio1/RutaArchivo.txt");
                            _lzw.CrearArchivo(rutaArchivoUsuario+"ArchivoDescompreso("+nuevoNombre+").lzw",dividir[0]);
                            MostrarCompresion.setText(dividir[0]);
                            MostrarRuta.setText(rutaArchivoUsuario+"ArchivoDescompreso("+nuevoNombre+").lzw");
                            String rutaTablas = "/storage/emulated/0/Documents/TablasCompresionesLZW/";
                            String ruta2 =rutaTablas+"TablaCaracteresUnitarios("+nuevoNombre+").txt";
                            MostrarTablaCompleta.setText(Leer(ruta2));
                            //endregion
                        }
                        else
                        {
                            MostrarCompresion.setText("Formato de archivo no aceptado");
                        }
                    }
                    if(validar ==0)
                    {
                        MostrarCompresion.setText("Debe de escojer una ruta para almacenar el archivo descompreso.");
                    }
                }


            }
        }
    }
    public String Leer(String archivo)
    {
        String textoArchivo = "";
        try
        {
            String cadenaArchivo;
            String temp="";
            FileReader filereader = new FileReader(archivo);
            BufferedReader bufferedreader = new BufferedReader(filereader);
            while((cadenaArchivo = bufferedreader.readLine())!=null) {
                temp = temp + cadenaArchivo;
            }
            bufferedreader.close();
            textoArchivo = temp;
        }catch(Exception e){
            e.printStackTrace();
        }
        return textoArchivo;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso No Concedido", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
