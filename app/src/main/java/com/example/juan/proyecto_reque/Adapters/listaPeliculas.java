package com.example.juan.proyecto_reque.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Dowloaders.ImageDownloadTask;
import com.example.juan.proyecto_reque.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class listaPeliculas extends  BaseAdapter{
    private ArrayList<Pelicula> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;


    public listaPeliculas(ArrayList<Pelicula> arrayList, Context context) {
        this.arrayList = arrayList;
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
        if (arrayList.get(position).getFoto().equals("NULL")){
            tv_imagen.setImageResource(R.drawable.proyector);
        }
        else{
            ImageDownloadTask downloadTask = new ImageDownloadTask();
            try {
                Bitmap result = downloadTask.execute(arrayList.get(position).getFoto()).get();
                tv_imagen.setImageBitmap(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        tv_titulo.setText(arrayList.get(position).getNombre());
        tv_director.setText(arrayList.get(position).getDirector());
        tv_calificacion.setText("Calificacion: " + String.valueOf(arrayList.get(position).genCalification()));
        return vistaItem;
    }
}
