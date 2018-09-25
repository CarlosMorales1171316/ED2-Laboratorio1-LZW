package com.example.ivana.laboratorio1;


import java.io.BufferedReader;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LZW {

    //Variables
    private String contenidoDelArchivo;
    private String cadenaActual;
    private char  caracterDeEntrada;
    //arreglo ordenado
    private char[] char_sorted;
    //valor inicial por caracter
    private int[] codigo_char ;
    private int[] salidaCodificada;
    private Tabla tabla = new Tabla();
    //arreglo de tablas
    private Diccionario diccionario = new Diccionario();
    //tamaño inicial del diccionario
    private int buffersize;
    public static List<String>listaDiccionario = new ArrayList<String>();
    public static List<String>listaCaracteresUnitarios = new ArrayList<String>();

    static String contenidoCadena=null;
    static String nombreDeRutaSalida=null;
    static ArrayList<Integer> CodigosDeSalida = new ArrayList<Integer>();
    static ArrayList<Character> CaracteresDeEntrada= new ArrayList<Character>();

    static ArrayList<Integer> codigos= new ArrayList<Integer>();
    static ArrayList<String> cadenaDescomp= new ArrayList<String>();
    public static String rutaComprimir="C:/LZW/Compreso/";
    public static String rutaArchivo ="C:/LZW/";
    public static String rutaArchivoDescompreso="C:/LZW/Descompreso/";


    //Constructor
    public LZW() {
    }
    public LZW(String contenido) throws IOException {
        contenidoDelArchivo = contenido;
        cadenaActual = "";
        String removerString = EliminarDuplicados(contenido);
        char_sorted = removerString.toCharArray();
        Arrays.sort(char_sorted);
        buffersize = char_sorted.length;
        codigo_char = new int[buffersize];
        for(int i = 0; i < buffersize; ++i) {
            codigo_char[i] = i;
        }
        for(int i=0; i < buffersize; ++i) {
            String stringTemporal = "";
            stringTemporal += char_sorted[i];
            Tabla temporal = new Tabla(stringTemporal,codigo_char[i]);
            //TABLA DE CARACTERES UNITARIOS
            listaCaracteresUnitarios.add(temporal.ObtenerTabla());
            diccionario.Agregar(temporal);
        }
        salidaCodificada = new int[100];
    }

    public void Compresion(String n) throws IOException{
        try {
            String ruta2 = rutaComprimir+"ArchivoCompreso("+n+").lzw";
            File archivoTexto = new File(ruta2);
            if(!archivoTexto.exists()) {
                archivoTexto.createNewFile();
            }
            FileWriter fw = new FileWriter(archivoTexto.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            int i = 0;
            int pivote = 0;
            while(i < contenidoDelArchivo.length()) {
                caracterDeEntrada = contenidoDelArchivo.charAt(i);
                String temporal = cadenaActual + caracterDeEntrada;
                int j = diccionario.Buscar(temporal);
                if( j > 0){
                    tabla = diccionario.ObtenerTabla(j);
                    cadenaActual += caracterDeEntrada;
                }
                else {
                    Tabla temporal1 = new Tabla(temporal,buffersize);
                    salidaCodificada[pivote] = diccionario.Buscar(cadenaActual);
                    pivote++;
                    diccionario.Agregar(temporal1);
                    cadenaActual = "";
                    cadenaActual += caracterDeEntrada;
                    buffersize++;
                }
                i++;
            }
            for(int j=0; j < pivote; ++j){
                int num = salidaCodificada[j];
                num = (char) salidaCodificada[j];
                bw.write(String.valueOf(num));
                bw.flush();
            }
            bw.close();

            for(int k=0; k < diccionario.Size(); k++) {
                //AQUI ESTA LA TABLA COMPLETA
                listaDiccionario.add(diccionario.ObtenerTabla(k).ObtenerTabla());
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void ObtenerList(String n)throws IOException {

        String ruta2 = rutaComprimir + "TablaCompleta("+n+").txt";
        File archivoTablaFinal = new File(ruta2);
        if(!archivoTablaFinal.exists()) {
            archivoTablaFinal.createNewFile();
        }
        FileWriter fw1 = new FileWriter(archivoTablaFinal.getAbsoluteFile());
        BufferedWriter bw1 = new BufferedWriter(fw1);
        for (int i = 0; i < listaDiccionario.size(); i++) {
            bw1.write("|"+listaDiccionario.get(i)+"| ");
        }
        bw1.close();
    }

    public void ObtenerList2(String n)throws IOException {
        String ruta2 =rutaComprimir+"TablaCaracteresUnitarios("+n +").txt";
        File archivoTabla = new File(ruta2);
        if(!archivoTabla.exists()) {
            archivoTabla.createNewFile();
        }
        FileWriter fw2 = new FileWriter(archivoTabla.getAbsoluteFile());
        BufferedWriter bw2 = new BufferedWriter(fw2);
        for (int i = 0; i < listaCaracteresUnitarios.size(); i++) {
            bw2.write("|"+listaCaracteresUnitarios.get(i)+"| ");
        }
        bw2.close();
    }

    public void Descompresion() throws IOException {
        for(int i=0; i < contenidoDelArchivo.length()-1; ++i) {
            diccionario.BuscarPorIndex(salidaCodificada[i]);
        }
    }

    private static String EliminarDuplicados(String cadena) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cadena.length(); i++) {
            String cadenaString = cadena.substring(i, i + 1);
            if (sb.indexOf(cadenaString) == -1) {
                sb.append(cadenaString);
            }
        }
        return sb.toString();
    }

    public static void compresion2()
    {
        HashMap<String,Integer> diccionario= new HashMap<String,Integer>();
        for(int i=0;i<256;i++){
            diccionario.put(""+ (char) i,i);
        }
        int nextCodigo=256;
        StringBuilder sb= new StringBuilder();
        String simbolo=null;
        sb.setLength(0);
        for(int i = 0; i < CaracteresDeEntrada.size(); i++) {
            simbolo=""+CaracteresDeEntrada.get(i);
            if(diccionario.containsKey(sb.toString()+simbolo)){
                sb=sb.append(simbolo);
            }
            else {
                CodigosDeSalida.add(diccionario.get(sb.toString()));
                diccionario.put(sb.toString()+simbolo, nextCodigo);
                nextCodigo++;
                sb.setLength(0);
                sb.append(simbolo);
            }
        }
        CodigosDeSalida.add(diccionario.get(sb.toString()));
    }

    public static void LeerArchivo(String ruta)
    {
        StringBuilder sb = new StringBuilder();
        try {
            File file= new File(ruta);
            nombreDeRutaSalida=file.getName().replaceFirst("[.][^.]+$", "");
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            int numchar;
            while ((numchar = br.read()) != -1) {
                char caracter = (char) numchar;
                CaracteresDeEntrada.add(caracter);
            }
            br.close();
        } catch (FileNotFoundException e1) {
            System.out.println("Ingrese una ruta valida");
            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void EscribirArchivo(String ruta,String n)
    {
        if(CodigosDeSalida.size()>0){
            try {   String ruta2 =ruta+"CompresionAscii("+n+").txt";
                OutputStream salidaStream  = new FileOutputStream(ruta2);
                Writer salidaStreamWriter = new OutputStreamWriter(salidaStream);
                for(int i=0;i<CodigosDeSalida.size();i++){
                    int codigo=CodigosDeSalida.get(i);
                    salidaStreamWriter.write(codigo);
                }
                salidaStreamWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Archivo vacio");
        }
    }

    public static void LeerArchivoSalida(String ruta)
    {
        File file = new File(ruta);
        nombreDeRutaSalida=file.getName().replaceFirst("[.][^.]+$", "");
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            Reader reader;
            reader = new InputStreamReader(inputStream);
            int leer;
            while ((leer = reader.read()) != -1)
            {
                codigos.add(leer);
            }
            reader.close();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void descompresion2()
    {
        HashMap<Integer,String> diccionario= new HashMap<Integer,String>();
        for(int i=0;i<256;i++){
            diccionario.put(i,""+ (char) i);
        }
        int CodigoSig=256;
        String cadena=null;
        StringBuilder newstring= new StringBuilder();
        newstring.setLength(0);
        if(codigos.size()>0)
            cadena=diccionario.get(codigos.get(0));
        cadenaDescomp.add(cadena);
        for(int i=1;i<codigos.size();i++){
            if(!diccionario.containsKey(codigos.get(i))){
                newstring.setLength(0);
                newstring.append(cadena+cadena.charAt(0));
            }
            else{
                newstring.setLength(0);
                newstring.append(diccionario.get(codigos.get(i)));
            }
            cadenaDescomp.add(newstring.toString());
            diccionario.put(CodigoSig, cadena+newstring.toString().charAt(0));
            CodigoSig++;
            cadena=newstring.toString();
        }
    }

    public String ImprimirSalida()
    {
        String mensaje="";

        if(cadenaDescomp.size()>0){
            for(String x:cadenaDescomp)
            {
                mensaje += x;
            }
        }
        return mensaje;
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


    public void Validararchivo(String path)
    {
        String mensaje = Leer(path);
        int contador = Integer.parseInt(mensaje);
        contador++;
        try
        {
            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contador + "");
            bw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public int ObtenerValorN(String path)
    {
        String mensaje = Leer(path);
        int contador = Integer.parseInt(mensaje);
        return contador;
    }

    public void CrearContador(String ruta)
    {
        try
        {
            if(!new File(ruta).exists())
            {
                new File(ruta).createNewFile();
                FileWriter fw = new FileWriter(ruta);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(0 + "");
                bw.close();
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void CrearArchivo(String ruta,String mensaje)
    {
        try
        {

            new File(ruta).createNewFile();
            FileWriter fw = new FileWriter(ruta);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(mensaje);
            bw.close();


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}