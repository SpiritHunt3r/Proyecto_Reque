package com.example.juan.proyecto_reque.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.juan.proyecto_reque.Adapters.listaPeliculas;
import com.example.juan.proyecto_reque.Adapters.listaUsuarios;
import com.example.juan.proyecto_reque.Clases.Comentario;
import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Clases.Usuario;
import com.example.juan.proyecto_reque.Clases.Voto;
import com.example.juan.proyecto_reque.Pantallas.Admin.DescpUsuarioActivity;
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

public class UsuariosFragment extends android.support.v4.app.Fragment{

    private View rootView;
    private ListView usuarios;
    private ArrayList<Usuario> arrayList;
    private ImageButton exit;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private listaUsuarios adapter;
    private Usuario pr;
    private EditText filterText;
    private SharedPreferences sharedPreferences;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_usuarios, container, false);
        usuarios = rootView.findViewById(R.id.LV_usuarios);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        arrayList = new ArrayList<>();

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



        usuarios.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view, int i, long l){
                Usuario temp = (Usuario) arrayList.get(i);
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Username",temp.getEmail()).commit();
                Intent k = new Intent(getContext(), DescpUsuarioActivity.class);
                startActivity(k);
            }
        });



        cargarLista(rootView.getContext());
        return rootView;
    }


    private void cargarLista(final Context context){

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    pr = ds.child("Personal Info").getValue(Usuario.class);
                    if(!pr.getEmail().equals(user.getEmail()))
                        arrayList.add(pr);
                }
                adapter = new listaUsuarios(arrayList,context);
                usuarios.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

}
