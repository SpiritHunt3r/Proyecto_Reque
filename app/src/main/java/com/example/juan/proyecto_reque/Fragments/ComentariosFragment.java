package com.example.juan.proyecto_reque.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.juan.proyecto_reque.Adapters.listaComentarios;
import com.example.juan.proyecto_reque.Adapters.listaPeliculas;
import com.example.juan.proyecto_reque.Clases.Comentario;
import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Clases.Voto;
import com.example.juan.proyecto_reque.Pantallas.Admin.DescpPeliculaAdminActivity;
import com.example.juan.proyecto_reque.Pantallas.Cliente.ClienteActivity;
import com.example.juan.proyecto_reque.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ComentariosFragment extends android.support.v4.app.Fragment {

    private View rootView;
    private ListView comentarios;
    private DatabaseReference ref;
    private ArrayList<Comentario> arrayListCom;
    private listaComentarios adapter;
    private String peli;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_comentarios, container, false);
        comentarios = rootView.findViewById(R.id.LV_comentarios);
        cargarLista(rootView.getContext());
        arrayListCom = new ArrayList<>();



        return rootView;
    }

    private void cargarLista(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        String Idn = sharedPreferences.getString("Id_Pelicula","");
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Peliculas").child(Idn);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot myCommt = dataSnapshot.child("Comentarios");
                for (DataSnapshot cm: myCommt.getChildren()){
                    Comentario tcm = cm.getValue(Comentario.class);
                    arrayListCom.add(tcm);
                }
                if (arrayListCom.size() != 0) {

                    adapter = new listaComentarios(arrayListCom, getContext());
                    comentarios.setAdapter(adapter);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
