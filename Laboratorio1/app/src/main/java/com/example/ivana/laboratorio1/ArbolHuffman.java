package com.example.ivana.laboratorio1;


public class ArbolHuffman implements Comparable<ArbolHuffman> {

    private final char simbolo;
    private final int frecuencia;
    private final ArbolHuffman izquierdo, derecho;

    public ArbolHuffman(char simbolo, int frecuencia, ArbolHuffman izquierdo, ArbolHuffman derecho) {
        this.simbolo    = simbolo;
        this.frecuencia  = frecuencia;
        this.izquierdo  = izquierdo;
        this.derecho = derecho;
    }

    public ArbolHuffman(){ // Árbol vacío
        this('\0',0,null,null);
    }

    public boolean esHoja() {
        return (izquierdo == null) && (derecho == null);
    }

    public int compareTo(ArbolHuffman otro) {
        return this.frecuencia - otro.frecuencia;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public ArbolHuffman getIzquierdo() {
        return izquierdo;
    }

    public ArbolHuffman getDerecho() {
        return derecho;
    }

    @Override
    public String toString() {
        return toStringArbol(this, 0);
    }

    private String toStringArbol(ArbolHuffman arbol, int sangria) {
        if (arbol == null) {
            return "";
        }
        String indentacion = "";
        for (int i = 0; i < sangria; i++) {
            indentacion += " ";
        }
        if (arbol.esHoja()) {
            String simbolo = (arbol.simbolo=='\n') ? "\\n" : ""+arbol.simbolo;
            return "[" + simbolo + "]\n";
        }else {
            String result = "(*)\n";
            result += indentacion + "Izq: " +
                    toStringArbol(arbol.izquierdo, sangria + 4);
            result += indentacion + "Der: " +
                    toStringArbol(arbol.derecho, sangria + 4);
            return result;
        }
    }
}