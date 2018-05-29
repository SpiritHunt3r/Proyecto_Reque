package com.example.juan.proyecto_reque.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Clases.Usuario;
import com.example.juan.proyecto_reque.R;

import java.util.ArrayList;

public class listaUsuarios extends  BaseAdapter implements Filterable{
    private ArrayList<Usuario> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Usuario> listUsuarios = null;


    public listaUsuarios(ArrayList<Usuario> arrayList, Context context) {
        this.arrayList = arrayList;
        this.listUsuarios = new ArrayList<>();
        this.listUsuarios.addAll(arrayList);
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

        View vistaItem = layoutInflater.inflate(R.layout.listausuarios, parent, false);
        TextView tv_nombre = (TextView) vistaItem.findViewById(R.id.tvu_nombre);
        TextView tv_username = (TextView) vistaItem.findViewById(R.id.tvu_usuario);
        TextView tv_activo = (TextView) vistaItem.findViewById(R.id.tvu_active);


        tv_nombre.setText("Nombre: " + arrayList.get(position).getNombre());
        tv_username.setText("Username: " +arrayList.get(position).getEmail());
        if (arrayList.get(position).getIs_active())
            tv_activo.setText("Activo: SI");
        else
            tv_activo.setText("Activo: NO");
        return vistaItem;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase();
        arrayList.clear();
        if (charText.length() == 0) {
            arrayList.addAll(listUsuarios);
        }
        else
        {
            for (Usuario wp : listUsuarios)
            {
                if (wp.getNombre().toLowerCase().contains(charText) || wp.getEmail().toLowerCase().contains(charText))
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
