package com.example.juan.proyecto_reque.Pantallas.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juan.proyecto_reque.Adapters.listaComentarios;
import com.example.juan.proyecto_reque.Clases.Comentario;
import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Clases.Voto;
import com.example.juan.proyecto_reque.Dowloaders.ImageDownloadTask;
import com.example.juan.proyecto_reque.Fragments.ComentariosFragment;
import com.example.juan.proyecto_reque.Fragments.PeliculasAdminFragment;
import com.example.juan.proyecto_reque.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class EditPeliculaAdminActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference ref;
    private ImageView portada;
    private EditText des,nom,dir,anno,gen,acts;
    Pelicula u;
    String Idn;
    private Spinner spinner;
    private DatabaseReference rootRefence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pelicula_admin);




        rootRefence = FirebaseDatabase.getInstance().getReference();

        portada = findViewById(R.id.imgView);
        des = findViewById(R.id.descriptionText);
        nom = findViewById(R.id.nombreText);
        dir = findViewById(R.id.directorText);
        anno = findViewById(R.id.annoText);
        acts = findViewById(R.id.actoresText);


        des.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        nom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        dir.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        anno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        acts.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });




        spinner = (Spinner) findViewById(R.id.generoSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Generos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Idn = sharedPreferences.getString("Id_Pelicula","");
        ref = FirebaseDatabase.getInstance().getReference().child("Peliculas").child(Idn);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                u = dataSnapshot.child("Info").getValue(Pelicula.class);
                if (!(u.getDescripcion() == null))
                    des.setText(u.getDescripcion());
                nom.setText(u.getNombre());
                dir.setText(u.getDirector());
                anno.setText(u.getAnno());
                spinner.setSelection(getIndex(spinner, u.getGenero()));
                if (!(u.getActores()== null))
                    acts.setText(u.getActores());
                if (u.getFoto().equals("NULL")){
                    portada.setImageResource(R.drawable.proyector);
                }
                else{
                    ImageDownloadTask downloadTask = new ImageDownloadTask();
                    try {
                        Bitmap result = downloadTask.execute(u.getFoto()).get();
                        portada.setImageBitmap(result);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }


    public void backdescadmin (View v){
        finish();
        Intent i = new Intent(getApplicationContext(),DescpPeliculaAdminActivity.class);
        startActivity(i);
    }


    public void guardarCambios (View v){
        if (nom.getText().toString().equals("") || dir.getText().toString().equals("") || anno.getText().toString().equals("") || spinner.getSelectedItem().toString().equals("Genero")){
            Toast.makeText(getApplicationContext(), "Los campos: Nombre, Director, Año y Genero.Son Obligatorios!", Toast.LENGTH_SHORT).show();
        }
        else if (nom.getText().toString().equals(u.getNombre()) && dir.getText().toString().equals(u.getDirector()) && anno.getText().toString().equals(u.getAnno()) && des.getText().toString().equals(u.getDescripcion()) && acts.getText().toString().equals(u.getActores())){
            Toast.makeText(getApplicationContext(), "No se detectaron cambios.", Toast.LENGTH_SHORT).show();
        }
        else{
            String keyW = genKeys(nom.getText().toString(),dir.getText().toString(),anno.getText().toString(),spinner.getSelectedItem().toString(),acts.getText().toString());
            Pelicula p = new Pelicula(u.getFoto(),nom.getText().toString(),dir.getText().toString(),anno.getText().toString(),spinner.getSelectedItem().toString(),des.getText().toString(),acts.getText().toString(),keyW);
            rootRefence.child("Peliculas").child(p.getNombre()).child("Info").setValue(p)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Pelicula Editada Correctamente", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent k = new Intent(getApplicationContext(),DescpPeliculaAdminActivity.class);
                            startActivity(k);
                        }
                    });
        }
    }


    private String genKeys(String n,String dir, String a,String g, String acts){
        return n+","+dir+","+a+","+g+","+acts;

    }

}
