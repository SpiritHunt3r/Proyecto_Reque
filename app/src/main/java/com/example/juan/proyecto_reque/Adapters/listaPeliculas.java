package com.example.juan.proyecto_reque.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Dowloaders.ImageDownloadTask;
import com.example.juan.proyecto_reque.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class listaPeliculas extends  BaseAdapter implements Filterable{
    private ArrayList<Pelicula> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Pelicula> listPelis = null;


    public listaPeliculas(ArrayList<Pelicula> arrayList, Context context) {
        this.arrayList = arrayList;
        this.listPelis = new ArrayList<>();
        this.listPelis.addAll(arrayList);
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vistaItem = layoutInflater.inflate(R.layout.listapeliculas, parent, false);
        ImageView tv_imagen = (ImageView) vistaItem.findViewById(R.id.tv_imagen);
        TextView tv_titulo = (TextView) vistaItem.findViewById(R.id.tv_titulo);
        TextView tv_director = (TextView) vistaItem.findViewById(R.id.tv_director);
        TextView tv_calificacion = (TextView) vistaItem.findViewById(R.id.tv_calificacion);
        tv_imagen.setImageResource(R.drawable.proyector);

        tv_titulo.setText(arrayList.get(position).getNombre());
        tv_director.setText(arrayList.get(position).getDirector());
        tv_calificacion.setText("Calificacion: " + String.format("%.2f",arrayList.get(position).genCalification()));
        return vistaItem;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase();
        arrayList.clear();
        if (charText.length() == 0) {
            arrayList.addAll(listPelis);
        }
        else
        {
            for (Pelicula wp : listPelis)
            {
                if (wp.getKeywords().toLowerCase().contains(charText))
                {
                    arrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public Filter getFilter() {
        return null;
    }
}
