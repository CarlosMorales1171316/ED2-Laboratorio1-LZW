package com.example.ivana.laboratorio1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MostrarCompresiones> ListaCompresiones;

    public ListAdapter(Context mContext,List<MostrarCompresiones>Lista)
    {
        this.ListaCompresiones = Lista;
        this.mContext = mContext;
    }


    @Override

    public int getCount() {
        return ListaCompresiones.size();
    }

    @Override
    public Object getItem(int position) {
        return ListaCompresiones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.list_item, null);
        TextView tvNombre = (TextView) v.findViewById(R.id.text1);
        TextView tvRuta = (TextView) v.findViewById(R.id.texto2);
        TextView tvRazon = (TextView) v.findViewById(R.id.texto3);
        TextView tvFactor = (TextView) v.findViewById(R.id.texto4);
        TextView tvReduccion = (TextView) v.findViewById(R.id.texto5);

        tvNombre.setText(ListaCompresiones.get(position).getNombre());
        tvRuta.setText(ListaCompresiones.get(position).getRuta());
        tvRazon.setText(ListaCompresiones.get(position).getRazonDeCompresion());
        tvFactor.setText(ListaCompresiones.get(position).getFactorDeCompresion());
        tvReduccion.setText(ListaCompresiones.get(position).getPorcentajedeReduccion());

        return v    ;
    }
}

