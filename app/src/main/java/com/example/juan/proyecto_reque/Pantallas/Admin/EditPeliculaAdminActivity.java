package com.example.juan.proyecto_reque.Pantallas.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.juan.proyecto_reque.Adapters.listaComentarios;
import com.example.juan.proyecto_reque.Clases.Comentario;
import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Clases.Voto;
import com.example.juan.proyecto_reque.Dowloaders.ImageDownloadTask;
import com.example.juan.proyecto_reque.R;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pelicula_admin);

        portada = findViewById(R.id.imgView);
        des = findViewById(R.id.descriptionText);
        nom = findViewById(R.id.nombreText);
        dir = findViewById(R.id.directorText);
        anno = findViewById(R.id.annoText);
        acts = findViewById(R.id.actoresText);

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


    public void backadmin (View v){
        Intent i = new Intent(getApplicationContext(),DescpPeliculaAdminActivity.class);
        startActivity(i);
    }
}
