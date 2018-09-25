package com.example.ivana.laboratorio1;

public class MostrarCompresiones {

    private String NombreArchivo,Ruta,RazonDeCompresion,FactorDeCompresion,PorcentajedeReduccion;

    //constructor
    public MostrarCompresiones(String nombre,String ruta, String razon, String factor,String reduccion)
    {
        this.NombreArchivo = nombre;
        this.Ruta = ruta;
        this.RazonDeCompresion = razon;
        this.FactorDeCompresion = factor;
        this.PorcentajedeReduccion = reduccion;
    }

    public String getNombre() {
        return NombreArchivo;
    }

    public String getRuta() {
        return Ruta;
    }

    public String getRazonDeCompresion() {
        return RazonDeCompresion;
    }

    public String getFactorDeCompresion() {
        return FactorDeCompresion;
    }

    public String getPorcentajedeReduccion() {
        return PorcentajedeReduccion;
    }
}
