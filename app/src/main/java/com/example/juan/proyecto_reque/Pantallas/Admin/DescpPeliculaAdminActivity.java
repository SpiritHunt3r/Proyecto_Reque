package com.example.juan.proyecto_reque.Pantallas.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juan.proyecto_reque.Adapters.listaComentarios;
import com.example.juan.proyecto_reque.Clases.Comentario;
import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Clases.Voto;
import com.example.juan.proyecto_reque.Dowloaders.ImageDownloadTask;
import com.example.juan.proyecto_reque.Fragments.ComentariosAdminFragment;
import com.example.juan.proyecto_reque.Fragments.ComentariosFragment;
import com.example.juan.proyecto_reque.Pantallas.Cliente.ClienteActivity;
import com.example.juan.proyecto_reque.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DescpPeliculaAdminActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference ref;
    private ImageView portada;
    private TextView des,nom,dir,anno,gen,acts,cal;
    Pelicula u;
    String Idn;
    private listaComentarios adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descp_pelicula_admin);

        portada = findViewById(R.id.imgView);
        des = findViewById(R.id.descriptionText);
        nom = findViewById(R.id.nombreText);
        dir = findViewById(R.id.directorText);
        anno = findViewById(R.id.annoText);
        gen = findViewById(R.id.generoText);
        acts = findViewById(R.id.actoresText);
        cal = findViewById(R.id.calificacionText);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.comentarios_frame, new ComentariosAdminFragment()).commit();




        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();

        des.setMovementMethod(new ScrollingMovementMethod());

        acts.setMovementMethod(new ScrollingMovementMethod());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Idn = sharedPreferences.getString("Id_Pelicula","");
        ref = FirebaseDatabase.getInstance().getReference().child("Peliculas").child(Idn);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Voto[] votos;
                int pvoto = 0;
                u = dataSnapshot.child("Info").getValue(Pelicula.class);

                DataSnapshot myVoto = dataSnapshot.child("Votos");
                votos = new Voto[(int) myVoto.getChildrenCount()];

                for (DataSnapshot tm: myVoto.getChildren()){
                    Voto vt = tm.getValue(Voto.class);
                    votos[pvoto] = vt;
                    pvoto++;
                }
                u.setVotos(votos);
                if (!(u.getDescripcion() == null))
                    des.setText(u.getDescripcion());
                nom.setText("Nombre: "+u.getNombre());
                dir.setText("Director: "+u.getDirector());
                anno.setText("Año: " + u.getAnno());
                gen.setText("Genero: "+u.getGenero());
                if (!(u.getActores()== null))
                    acts.setText("Actores: " + u.getActores());
                cal.setText("Calificacion: " + String.format("%.2f",u.genCalification()));
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


    public void backadmin (View v){
        Intent i = new Intent(getApplicationContext(),AdminActivity.class);
        startActivity(i);
    }

    public void editadmin (View v){
        Intent i = new Intent(getApplicationContext(),EditPeliculaAdminActivity.class);
        startActivity(i);
    }






}
