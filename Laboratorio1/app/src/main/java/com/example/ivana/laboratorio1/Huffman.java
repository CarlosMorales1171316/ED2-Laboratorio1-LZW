package com.example.ivana.laboratorio1;

import java.io.*;
import java.util.*;

public class Huffman {

    public static String[] huffMan = new String[256];
    public static String[][] huffMan2 = new String[256][256];

    public static String crearMensajeHuffman1(StringBuffer mensaje)
    {   String mensajeHuffman = "";
        for(int i=0;i<=mensaje.length()-1;i++)
        {
            mensajeHuffman = mensajeHuffman + huffMan[mensaje.charAt(i)];
        }
        return(mensajeHuffman);
    }


    public static String[] processFile(String fileContents,int[] frequency)
    {
        TreeSet<NodoHF1> arboles = new TreeSet<NodoHF1>();
        for (int i=0; i<fileContents.length(); i++)
        {
            char ch = fileContents.charAt(i);
            if(ch != '\n')
                ++frequency[ch];
        }
        for (int i=0; i<255; i++)
        {
            if (frequency[i] > 0)
            {
                NodoHF1 n = new NodoHF1((char)(i), frequency[i]);
                arboles.add(n);
            }
        }

        while (arboles.size() > 1)
        {
            NodoHF1 tree1 = (NodoHF1) arboles.first();
            arboles.remove(tree1);
            NodoHF1 tree2 = (NodoHF1) arboles.first();
            arboles.remove(tree2);
            NodoHF1 merged = new NodoHF1(tree1, tree2);
            arboles.add(merged);
        }
        if (arboles.size() > 0)
        {
            NodoHF1 theTree = (NodoHF1) arboles.first();
            NodoHF1.printTree(theTree,huffMan);
        }
        else
            System.out.println("El archivo no contenia carácteres útiles");
        return(huffMan);
    }

}

class NodoHF1 implements Comparable
{
    private int valor;
    private char contenido;
    private NodoHF1 left;
    private NodoHF1 right;

    public NodoHF1(char contenido, int valor)
    {
        this.contenido = contenido;
        this.valor = valor;
    }

    public NodoHF1(NodoHF1 left, NodoHF1 right)
    {
        this.contenido = (left.contenido < right.contenido) ? left.contenido : right.contenido;
        this.valor = left.valor + right.valor;
        this.left = left;
        this.right = right;
    }

    public int compareTo(Object arg)
    {
        NodoHF1 other = (NodoHF1) arg;
        if (this.valor == other.valor)
            return this.contenido-other.contenido;
        else
            return this.valor-other.valor;
    }

    private void printNodoHF1(String path,String[] hf)
    {
        if ((left==null) && (right==null))
        {
            hf[(int)contenido] = path;
        }

        if (left != null)
            left.printNodoHF1(path + '0',hf);
        if (right != null)
            right.printNodoHF1(path + '1',hf);
    }

    public static void printTree(NodoHF1 tree,String[] hf)
    {
        tree.printNodoHF1("",hf);
    }
}

class NodoHF2 implements Comparable
{
    private int     valor;
    private String contenido;
    private NodoHF2    left;
    private NodoHF2    right;

    public NodoHF2(String contenido, int valor)
    {
        this.contenido  =contenido;
        this.valor    = valor;
    }

    public NodoHF2(NodoHF2 left, NodoHF2 right)
    {
        if(left.contenido.charAt(0) < right.contenido.charAt(0))
        {
            this.contenido = left.contenido;
        }
        else if (left.contenido.charAt(0) == right.contenido.charAt(0))
        {
            if (left.contenido.charAt(1) < right.contenido.charAt(1))
                this.contenido = left.contenido;
            else this.contenido = right.contenido;
        }
        else if (left.contenido.charAt(0) > right.contenido.charAt(0)) this.contenido = right.contenido;
        this.valor    = left.valor + right.valor;
        // System.out.println(valor);
        this.left     = left;
        this.right    = right;
    }

    public int compareTo(Object arg)
    {
        NodoHF2 other = (NodoHF2) arg;
        if (this.valor == other.valor)
        {	if(this.contenido.charAt(0) == other.contenido.charAt(0))
            return this.contenido.charAt(1)-other.contenido.charAt(1);
        else return this.contenido.charAt(0)-other.contenido.charAt(0);
        }
        else
            return this.valor-other.valor;
    }

    private void printNodoHF2(String path,String[][] hf)
    {
        if ((left==null) && (right==null))
        {
            hf[(int)contenido.charAt(0)][(int)contenido.charAt(1)] = path;
        }

        if (left != null)
            left.printNodoHF2(path + '0',hf);
        if (right != null)
            right.printNodoHF2(path + '1',hf);
    }

    public static void printTree(NodoHF2 tree,String[][] hf)
    {
        tree.printNodoHF2("",hf);
    }
}
