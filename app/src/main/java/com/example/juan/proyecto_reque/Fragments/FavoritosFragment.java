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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.juan.proyecto_reque.Adapters.listaPeliculas;
import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Clases.Voto;
import com.example.juan.proyecto_reque.Pantallas.Admin.AdminActivity;
import com.example.juan.proyecto_reque.Pantallas.Admin.DescpPeliculaAdminActivity;
import com.example.juan.proyecto_reque.Pantallas.Cliente.ClienteActivity;
import com.example.juan.proyecto_reque.Pantallas.Cliente.DescpPeliculaActivity;
import com.example.juan.proyecto_reque.Pantallas.General.MainActivity;
import com.example.juan.proyecto_reque.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoritosFragment extends android.support.v4.app.Fragment {

    private View rootView;
    private ListView peliculas;
    private listaPeliculas adapter;
    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;
    private FirebaseUser user;
    private ArrayList<Pelicula> arrayList = null;
    private Voto[] votos;
    private int pvoto;
    private EditText filterText;
    private ImageButton exit;




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
        exit = rootView.findViewById(R.id.imageButton);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
            }
        });


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
                String isAdm = sharedPreferences.getString("IS_ADMIN","");
                if (Boolean.valueOf(isAdm)){
                    Intent n = new Intent(getContext(),DescpPeliculaAdminActivity.class);
                    startActivity(n);
                }
                else{
                    Intent n = new Intent(getContext(),DescpPeliculaActivity.class);
                    startActivity(n);
                }
            }
        });


        peliculas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String IdN = arrayList.get(position).getNombre();
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Eliminando pelicula")
                        .setMessage("Desea eliminar " + IdN + " sus favoritos?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid()).child("Peliculas").child(IdN);
                                myRef.removeValue();
                                Toast.makeText(rootView.getContext(),"Se ha elimiado "+ IdN +" de Favoritos",Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                String isAdm = sharedPreferences.getString("IS_ADMIN","");
                                if (Boolean.valueOf(isAdm)){
                                    Intent n = new Intent(getContext(),AdminActivity.class);
                                    startActivity(n);
                                }
                                else{
                                    Intent n = new Intent(getContext(),ClienteActivity.class);
                                    startActivity(n);
                                }

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
                DataSnapshot dataSnapshotUrs = dataSnapshot.child("Usuarios").child(user.getUid()).child("Peliculas");
                for(DataSnapshot ds:dataSnapshotUrs.getChildren()){
                    Pelicula pr = ds.getValue(Pelicula.class);
                    DataSnapshot myVoto = dataSnapshot.child("Peliculas").child(pr.getNombre()).child("Votos");
                    pvoto = 0;
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

