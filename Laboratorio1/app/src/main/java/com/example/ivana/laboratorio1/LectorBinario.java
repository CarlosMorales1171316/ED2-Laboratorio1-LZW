package com.example.ivana.laboratorio1;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public final class LectorBinario {

    private BufferedInputStream in;
    private final int EOF = -1;
    private int buffer;

    private int numBitsOcupados;

    public LectorBinario(String pathName){
        try {
            in = new BufferedInputStream(new FileInputStream(pathName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("La ruta del archivo no es correcta o no existe");		}
        llenarBuffer();
    }

    private void llenarBuffer() {
        try {
            buffer = in.read();
            numBitsOcupados = 8;
        }
        catch (IOException e) {
            System.out.println("EOF");
            buffer = EOF;
            numBitsOcupados = -1;
        }
    }

    public boolean esVacio() {
        return buffer == EOF;
    }

    public char leerPalabra() {
        if (esVacio()) throw new RuntimeException("Leyendo de un flujo de entrada vacio");
        // Caso especial de alineado:
        if (numBitsOcupados == 8) {
            int aux = buffer;
            llenarBuffer();
            return (char) (aux & 0xff);
        }
        int x = buffer;
        x <<= (8 - numBitsOcupados);
        int oldN = numBitsOcupados;
        llenarBuffer();
        if (esVacio()) throw new RuntimeException("Leyendo de un flujo de entrada vacio");
        numBitsOcupados = oldN;
        x |= (buffer >>> numBitsOcupados);
        return (char) (x & 0xff);
    }

    public boolean leerBit() {
        if (esVacio()) throw new RuntimeException("Leyendo de un flujo de entrada vacio");
        numBitsOcupados--;
        boolean bit = ((buffer >> numBitsOcupados) & 1) == 1;
        if (numBitsOcupados == 0)
            llenarBuffer();
        return bit;
    }

    public int leerEntero() {
        int x = 0;
        for (int i = 0; i < 4; i++) {
            char c = leerPalabra();
            x <<= 8;
            x |= c;
        }
        return x;
    }
}