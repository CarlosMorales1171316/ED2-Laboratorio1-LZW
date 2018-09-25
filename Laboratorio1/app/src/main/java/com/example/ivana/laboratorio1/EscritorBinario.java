package com.example.ivana.laboratorio1;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import static android.provider.Telephony.Mms.Part.FILENAME;


public class EscritorBinario {

    private byte buffer;
    private int numBitsOcupados;
    private BufferedOutputStream out;

    public EscritorBinario(String pathOutputFile){
        buffer = 0;
        numBitsOcupados = 0;
        try {
            out = new BufferedOutputStream(new FileOutputStream(pathOutputFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("La ruta del archivo no es correcta o no existe");
        }
    }

    public void escribirBit(boolean bit) {
        buffer <<= 1;
        if (bit) buffer |= 1;
        numBitsOcupados ++;
        if (numBitsOcupados == 8)
            vaciarBuffer();
    }

    public void escribirPalabra(int palabra) {
        palabra &= 0xff;
        if(numBitsOcupados == 0) {
            try {
                out.write(palabra);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        for (int i = 0; i < 8; i++) {
            boolean bit = ((palabra >>> (8 - i - 1)) & 1) == 1;
            escribirBit(bit);
        }
    }

    public void escribirEntero(int entero) {
        escribirPalabra(entero >>> 24);
        escribirPalabra(entero >>> 16);
        escribirPalabra(entero >>>  8);
        escribirPalabra(entero);
    }

    private void vaciarBuffer() {
        if (numBitsOcupados == 0)
            return;
        if (numBitsOcupados > 0)
            buffer <<= (8 - numBitsOcupados);
        try {
            out.write(buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        numBitsOcupados = 0;
        buffer = 0;
    }

    public void cerrarFlujo() {
        vaciarBuffer();
        try {
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}