package com.example.juan.proyecto_reque.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.juan.proyecto_reque.Adapters.listaPeliculas;
import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Clases.Voto;
import com.example.juan.proyecto_reque.Pantallas.Cliente.DescpPeliculaActivity;
import com.example.juan.proyecto_reque.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public class RecomendadosFragment extends android.support.v4.app.Fragment {

    private View rootView;
    private ListView peliculas;
    private listaPeliculas adapter;
    private ArrayList<Pelicula> arrayList = null;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Hashtable<String,Integer> PRecom;
    private ArrayList<String> pelis;
    private ArrayList<String> kpelis;
    private Voto[] votos;
    private int pvoto;

    private EditText filterText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_peliculas,container,false);
        peliculas = rootView.findViewById(R.id.LV_peliculas);
        peliculas.setTextFilterEnabled(true);
        arrayList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        PRecom = new Hashtable<String, Integer>();



        pelis = new ArrayList<>();
        kpelis = new ArrayList<>();



        filterText = rootView.findViewById(R.id.filter);
        filterText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = filterText.getText().toString().toLowerCase();
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
        filterText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });



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


    private void cargarLista(final Context context){
        user = auth.getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String MComon = "";
                DataSnapshot KusrWords = dataSnapshot.child("Usuarios").child(user.getUid()).child("Peliculas");
                for (DataSnapshot Ks:KusrWords.getChildren()){
                    Pelicula p = Ks.getValue(Pelicula.class);
                    pelis.add(p.getNombre());
                    kpelis.add(p.getKeywords());
                }


                for (int i=0;i<kpelis.size();i++) {
                    for (String s : kpelis.get(i).split(",")) {
                        if (PRecom.containsKey(s)) {
                            PRecom.put(s, PRecom.get(s) + 1);
                        } else {
                            PRecom.put(s, 1);
                        }
                    }
                }

                if (!PRecom.isEmpty()) {
                    int maxValueInMap = (Collections.max(PRecom.values()));
                    for (Map.Entry<String, Integer> entry : PRecom.entrySet()) {
                        if (entry.getValue() == maxValueInMap) {
                            MComon = entry.getKey();
                        }
                    }


                    DataSnapshot AllPelis = dataSnapshot.child("Peliculas");
                    for (DataSnapshot ds : AllPelis.getChildren()) {
                        Pelicula t = ds.child("Info").getValue(Pelicula.class);
                        if (t.getKeywords().contains(MComon) && !pelis.contains(t.getNombre())) {
                            pvoto = 0;
                            DataSnapshot myVoto = ds.child("Votos");
                            votos = new Voto[(int) myVoto.getChildrenCount()];
                            for (DataSnapshot tm: myVoto.getChildren()){
                                Voto vt = tm.getValue(Voto.class);
                                votos[pvoto] = vt;
                                pvoto++;
                            }
                            t.setVotos(votos);
                            arrayList.add(t);

                        }
                    }
                }
                else{
                    DataSnapshot AllPelis = dataSnapshot.child("Peliculas");
                    for (DataSnapshot ds : AllPelis.getChildren()) {
                        Pelicula t = ds.child("Info").getValue(Pelicula.class);
                            pvoto = 0;
                            DataSnapshot myVoto = ds.child("Votos");
                            votos = new Voto[(int) myVoto.getChildrenCount()];
                            for (DataSnapshot tm: myVoto.getChildren()){
                                Voto vt = tm.getValue(Voto.class);
                                votos[pvoto] = vt;
                                pvoto++;
                            }
                            t.setVotos(votos);
                            if (t.genCalification()>3.5f){
                                arrayList.add(t);
                            }
                        }

                }

                adapter = new listaPeliculas(arrayList,context);
                peliculas.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Formula para recomendados
    }

}

