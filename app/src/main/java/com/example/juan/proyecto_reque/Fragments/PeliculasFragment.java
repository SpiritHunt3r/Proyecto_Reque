package com.example.juan.proyecto_reque.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.juan.proyecto_reque.Adapters.listaPeliculas;
import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PeliculasFragment extends android.support.v4.app.Fragment {

    private View rootView;
    private ListView peliculas;
    private listaPeliculas adapter;
    private ArrayList<Pelicula> arrayList = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_peliculas,container,false);
        peliculas = rootView.findViewById(R.id.LV_peliculas);
        arrayList = new ArrayList<>();

        cargarLista(rootView.getContext());
        return rootView;
    }


    public void cargarLista(Context context){

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Peliculas");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Pelicula pr = ds.getValue(Pelicula.class);
                    arrayList.add(pr);
                }

                peliculas.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }

}

