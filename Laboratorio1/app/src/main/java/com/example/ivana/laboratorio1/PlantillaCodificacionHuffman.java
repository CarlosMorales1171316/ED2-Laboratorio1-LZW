package com.example.ivana.laboratorio1;

import java.util.Map;
import java.util.PriorityQueue;

public class PlantillaCodificacionHuffman {


    // Constructor
    public PlantillaCodificacionHuffman(){}


    public void comprimir(String filePathIn, String filePathOut) {

        LectorBinario lector = new LectorBinario(filePathIn);
        // Leer archivo de entrada y almacenar en una cadena
        StringBuilder sb = new StringBuilder();
        while (!lector.esVacio()) {
            char b = lector.leerPalabra();
            sb.append(b);
        }
        char[] input = sb.toString().toCharArray();
        int[] freq = new int[256]; //256 caracteres en ASCII

        for (int i = 0; i < input.length; i++) {
            freq[input[i]]++;
        }
        System.out.println("El archivo " + filePathIn + " se ha comprimido correctamente.");
        // Construir árbol de Huffman.
        ArbolHuffman arbol = construirArbol(freq);
        // Construir diccionario de búsqueda -> Pares (símbolo,código).
        // diccionarioCodigos será una estructura de tipo Map, Hashtable, String[]
        String [] diccionarioCodigos = new String[256];

        for(int i=0; i< diccionarioCodigos.length;i++) {
            diccionarioCodigos[i] = new String();
        }
        construirCodigos(diccionarioCodigos,arbol,"");
        codificar(input,diccionarioCodigos,filePathOut,arbol);
    }
    //Fin metodo comprimir

    //Construir arbol de Huffman a partir de la tabla de frecuencias.

    private ArbolHuffman construirArbol(int[] freq) {

        PriorityQueue<ArbolHuffman> cola = new PriorityQueue<ArbolHuffman>();

        for (int i = 0; i < freq.length; i++) {
            if (freq[i] != 0) {
                //Se inicializa el arbol con el simbolo, su frecuencia, la rama izquierda y la rama derecha
                ArbolHuffman arbol = new ArbolHuffman((char) i, freq[i], null, null);
                cola.add(arbol);
            }
        }

        ArbolHuffman arbolAux = null;
        while (cola.size() != 1) {
            //Cada arbol retira el par de nodos con menor frecuencia
            ArbolHuffman arbolAux2 = cola.poll();
            ArbolHuffman arbolAux3 = cola.poll();
            if (arbolAux2.getFrecuencia() > arbolAux3.getFrecuencia()) {
                //el arbolAux2 se mete en la derecha porque la frecuencia es mayor
                arbolAux = new ArbolHuffman('\0', arbolAux2.getFrecuencia() + arbolAux3.getFrecuencia(), arbolAux3, arbolAux2);
            } else {
                //El arbolAux3 se mete a la derecha porque la frecuencia es mayor
                arbolAux = new ArbolHuffman('\0', arbolAux2.getFrecuencia() + arbolAux3.getFrecuencia(), arbolAux2, arbolAux3);
            }
            cola.add(arbolAux);
        }

        return cola.poll();
    }

    private void construirCodigos(String [] diccionarioCodigos, ArbolHuffman arbol,String codigoCamino){
    if(arbol.getIzquierdo() == null && arbol.getDerecho() == null) {
            diccionarioCodigos[arbol.getSimbolo()] = codigoCamino;
        } else {
            if (arbol.getIzquierdo() != null) {
                construirCodigos(diccionarioCodigos, arbol.getIzquierdo(), codigoCamino + '0');
            }
            if (arbol.getDerecho() != null) {
                construirCodigos(diccionarioCodigos, arbol.getDerecho(), codigoCamino + '1');
            }
        }
    }

    private void codificar(char[] input, String [] diccionarioCodigos, String filePathOut, ArbolHuffman arbol){

        EscritorBinario escritor = new EscritorBinario(filePathOut);
        serializarArbol(arbol,escritor);
        escritor.escribirEntero(input.length);

        for (int i = 0; i < input.length; i++) {
            String codificado = diccionarioCodigos[input[i]];
            //Para cada elemento de input vamos escribiendo en el fichero un valor u otro dpendiendo de si el valor leido corresponde con un 0(false) o un 1(true)
            for (int j = 0; j < codificado.length(); j++) {
                if (codificado.charAt(j) == '1') {
                    escritor.escribirBit(true);
                } else {
                    escritor.escribirBit(false);
                }
            }
        }
        escritor.cerrarFlujo();
    }

    //serealizarArbol
    private void serializarArbol(ArbolHuffman arbol, EscritorBinario escritor){

        if (arbol.esHoja()) {
            escritor.escribirBit(true);
            //Escribir palabra de 8bits
            escritor.escribirPalabra(arbol.getSimbolo());
            return;
        }
        escritor.escribirBit(false);
        serializarArbol(arbol.getIzquierdo(),escritor);
        serializarArbol(arbol.getDerecho(),escritor);
    }

    public void descomprimir(String filePathIn, String filePathOut) {

        LectorBinario lector = new LectorBinario(filePathIn);
        EscritorBinario escritor = new EscritorBinario(filePathOut);
        ArbolHuffman arbol = leerArbol(lector);
        int length = lector.leerEntero();

        for (int i = 0; i < length; i++) {
            ArbolHuffman x = arbol;
            while (!x.esHoja()) {
                boolean bit = lector.leerBit();
                if (bit) x = x.getDerecho();
                else     x = x.getIzquierdo();
            }
            escritor.escribirPalabra(x.getSimbolo());
        }
        escritor.cerrarFlujo();
        System.out.println("El archivo " + filePathIn + " se ha descomprimido correctamente.");
    }

    private ArbolHuffman leerArbol(LectorBinario lector) {

        boolean esHoja = lector.leerBit();
        if (esHoja) {
            char simbolo = lector.leerPalabra();
            return new ArbolHuffman(simbolo, -1, null, null);
        }
        else {
            return new ArbolHuffman('\0', -1, leerArbol(lector), leerArbol(lector));
        }
    }
}