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
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.juan.proyecto_reque.Adapters.listaPeliculas;
import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Clases.Voto;
import com.example.juan.proyecto_reque.Pantallas.DescpPeliculaActivity;
import com.example.juan.proyecto_reque.Pantallas.MainActivity;
import com.example.juan.proyecto_reque.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PeliculasFragment extends android.support.v4.app.Fragment {

    private View rootView;
    private ListView peliculas;
    private listaPeliculas adapter;
    private ArrayList<Pelicula> arrayList = null;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Voto[] votos;
    private int pvoto;
    private Pelicula pr;


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
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();




        peliculas.setOnItemClickListener(new AdapterView.OnItemClickListener(){
          @Override
          public void onItemClick(AdapterView<?> adapterView,View view, int i, long l){
              Pelicula temp = (Pelicula) arrayList.get(i);
              sharedPreferences = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
              SharedPreferences.Editor editor = sharedPreferences.edit();
              editor.putString("Id_Pelicula",temp.getNombre()).commit();
              Intent n = new Intent(getContext(),DescpPeliculaActivity.class);
              startActivity(n);
          }
        });

        peliculas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String IdN = arrayList.get(position).getNombre();
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_menu_add)
                        .setTitle("Agregando a Favoritos")
                        .setMessage("Desea agregar " + IdN + " sus favoritos?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid()).child("Peliculas").child(IdN);
                                Pelicula t = arrayList.get(position);
                                t.setVotos(null);
                                myRef.setValue(t);
                                Toast.makeText(getContext(),"Se ha agregado "+ IdN +" a Favoritos",Toast.LENGTH_SHORT).show();
            }
        })
                .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

        cargarLista(rootView.getContext());
        return rootView;
    }


    public void cargarLista(final Context context){

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Peliculas");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    pvoto = 0;
                    pr = ds.child("Info").getValue(Pelicula.class);
                    DataSnapshot myVoto = ds.child("Votos");
                    votos = new Voto[(int) myVoto.getChildrenCount()];

                    for (DataSnapshot tm: myVoto.getChildren()){
                        Voto vt = tm.getValue(Voto.class);
                        votos[pvoto] = vt;
                        pvoto++;
                    }
                    pr.setVotos(votos);

                    arrayList.add(pr);
                }
                adapter = new listaPeliculas(arrayList,context);
                peliculas.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }

}

