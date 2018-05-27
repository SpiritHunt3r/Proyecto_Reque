package com.example.juan.proyecto_reque.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.juan.proyecto_reque.Clases.Comentario;
import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Dowloaders.ImageDownloadTask;
import com.example.juan.proyecto_reque.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class listaComentarios extends BaseAdapter {
    private ArrayList<Comentario> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;


    public listaComentarios(ArrayList<Comentario> arrayList, Context context) {
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

        View vistaItem = layoutInflater.inflate(R.layout.listacomnt, parent, false);
        TextView tv_username = (TextView) vistaItem.findViewById(R.id.tv_username);
        TextView tv_comentario = (TextView) vistaItem.findViewById(R.id.tv_comentario);

        tv_username.setText(arrayList.get(position).getUsername());
        tv_comentario.setText(arrayList.get(position).getComentario());

        return vistaItem;
    }
}