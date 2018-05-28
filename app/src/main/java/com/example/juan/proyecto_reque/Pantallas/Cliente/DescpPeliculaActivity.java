package com.example.juan.proyecto_reque.Pantallas.Cliente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juan.proyecto_reque.Adapters.listaComentarios;
import com.example.juan.proyecto_reque.Clases.Comentario;
import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Clases.Usuario;
import com.example.juan.proyecto_reque.Clases.Voto;
import com.example.juan.proyecto_reque.Dowloaders.ImageDownloadTask;
import com.example.juan.proyecto_reque.Pantallas.Admin.AdminActivity;
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

public class DescpPeliculaActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference ref;
    private ImageView portada;
    private TextView des,nom,dir,anno,gen,acts,cal;
    Pelicula u;
    String Idn;
    private listaComentarios adapter;
    private ArrayList<Comentario> arrayListCom;
    private ListView comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descp_pelicula);

        portada = findViewById(R.id.imgView);
        des = findViewById(R.id.descriptionText);
        nom = findViewById(R.id.nombreText);
        dir = findViewById(R.id.directorText);
        anno = findViewById(R.id.annoText);
        gen = findViewById(R.id.generoText);
        acts = findViewById(R.id.actoresText);
        cal = findViewById(R.id.calificacionText);

        comments = findViewById(R.id.comentariosList);
        arrayListCom = new ArrayList<>();


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
                DataSnapshot myCommt = dataSnapshot.child("Comentarios");
                for (DataSnapshot cm: myCommt.getChildren()){
                    Comentario tcm = cm.getValue(Comentario.class);

                    arrayListCom.add(tcm);
                }
                adapter = new listaComentarios(arrayListCom,getApplicationContext());
                comments.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public void back(View v){
        Intent i = new Intent(getApplicationContext(),ClienteActivity.class);
        startActivity(i);
    }


    public void addCalif(View v){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid()).child("Personal Info");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario u = dataSnapshot.getValue(Usuario.class);
                Log.d("TEST",u.getNombre());
                if (!u.getIs_active()){
                    Toast.makeText(getApplicationContext(),"No puede realizar votaciones debido a que se encuentra bloqueado",Toast.LENGTH_SHORT).show();
                }
                else{
                    showVotaciones();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showVotaciones(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Seleccione calificacion para "+Idn);
        String[] types = {"0.0","0.5","1.0","1.5","2.0","2.5","3.0","3.5","4.0","4.5","5.0"};
        b.setItems(types, new DialogInterface.OnClickListener() {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Peliculas").child(u.getNombre()).child("Votos").child(user.getUid());
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Voto v = new Voto(user.getEmail(),which * 0.5);
                myRef.setValue(v).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Votacion realizada para "+Idn, Toast.LENGTH_SHORT).show();
                        finish();
                        Intent i = new Intent(getApplicationContext(),DescpPeliculaActivity.class);
                        startActivity(i);
                    }
                });
            }

        });
        b.show();
    }


    public void addMovie(View v){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid()).child("Peliculas").child(Idn);
        u.setVotos(null);
        myRef.setValue(u);
        Toast.makeText(getApplicationContext(),"Se ha agregado "+ Idn +" a Favoritos",Toast.LENGTH_SHORT).show();
    }


    public void addComment(View v){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid()).child("Personal Info");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario u = dataSnapshot.getValue(Usuario.class);
                if (!u.getIs_active()){
                    Toast.makeText(getApplicationContext(),"No puede realizar comentarios debido a que se encuentra bloqueado",Toast.LENGTH_SHORT).show();
                }
                else {
                    showComentarios();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void showComentarios(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Comentario");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference myReC = FirebaseDatabase.getInstance().getReference().child("Peliculas").child(u.getNombre()).child("Comentarios").child(user.getUid());
                Comentario c = new Comentario(user.getEmail(),input.getText().toString());
                myReC.setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Cometario agregado para "+Idn, Toast.LENGTH_SHORT).show();
                        finish();
                        Intent i = new Intent(getApplicationContext(),DescpPeliculaActivity.class);
                        startActivity(i);
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


}
