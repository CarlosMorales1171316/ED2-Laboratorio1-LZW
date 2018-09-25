package com.example.ivana.laboratorio1;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SerieIII extends AppCompatActivity {

     TextView MostrarCompresion,MostrarTablaCompleta,MostrarRuta;
     Button Comprimir,Descomprimir;
     int opcion =0;
     private static final int PERMISSION_REQUEST_STORAGE = 1000;
     private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_iii);
        MostrarCompresion = (TextView) (findViewById(R.id.MostrarArchivoCompreso));
        MostrarTablaCompleta =(TextView) (findViewById(R.id.MostrarTablaCompleta));
        MostrarRuta = (TextView) (findViewById(R.id.MostrarRutaArchivo));
        Comprimir =(Button) (findViewById(R.id.Boton_Comprimir));
        Descomprimir = (Button)(findViewById(R.id.Boton_Descomprimir));

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
        if(opcion ==2) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                if (path.contains("emulated")) {
                    path = path.substring(path.indexOf("0") + 1);
                }
                Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();
                MostrarCompresion.setText(leerTextFile(path));
            }
        }
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
