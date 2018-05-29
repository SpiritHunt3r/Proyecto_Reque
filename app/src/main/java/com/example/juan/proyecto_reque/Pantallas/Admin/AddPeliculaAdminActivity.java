package com.example.juan.proyecto_reque.Pantallas.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddPeliculaAdminActivity extends AppCompatActivity {


    private ImageView imageView;
    private Uri filePath;
    private EditText des,nom,anno,director,acts;


    private final int PICK_IMAGE_REQUEST = 71;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference rootRefence;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pelicula);
        imageView = findViewById(R.id.imgView);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        rootRefence = FirebaseDatabase.getInstance().getReference();

        des = findViewById(R.id.descriptionText);
        nom = findViewById(R.id.nombreText);
        anno = findViewById(R.id.annoText);
        director = findViewById(R.id.directorText);
        acts = findViewById(R.id.actoresText);

        spinner = (Spinner) findViewById(R.id.generoSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Generos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }


    public void selectImg(View v){
        chooseImage();
    }

    public void updatePelicula(View v){
        RegPelicula();
    }

    public void backAdminAddPelicula(View v){ finish(); Intent i = new Intent(getApplicationContext(),AdminActivity.class); startActivity(i);}


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void RegPelicula() {
        final String ID = UUID.randomUUID().toString();
        final String dir = "portadas/"+ ID;
        if (nom.getText().toString().equals("") || director.getText().toString().equals("") || anno.getText().toString().equals("") || spinner.getSelectedItem().toString().equals("Genero")){
            Toast.makeText(getApplicationContext(), "Los campos: Nombre, Director, Año y Genero.Son Obligatorios!", Toast.LENGTH_SHORT).show();
        }
        else if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Picture...");
            progressDialog.show();
            StorageReference ref = storageReference.child(dir);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            String keyW = genKeys(nom.getText().toString(),director.getText().toString(),anno.getText().toString(),spinner.getSelectedItem().toString(),acts.getText().toString());
                            Pelicula p = new Pelicula("https://firebasestorage.googleapis.com/v0/b/cines-35mm.appspot.com/o/portadas%2F"+ID+"?alt=media",nom.getText().toString(),director.getText().toString(),anno.getText().toString(),spinner.getSelectedItem().toString(),des.getText().toString(),acts.getText().toString(),keyW);
                            rootRefence.child("Peliculas").child(p.getNombre()).child("Info").setValue(p)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Pelicula Agregada Correctamente", Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent i = new Intent(getApplicationContext(),AddPeliculaAdminActivity.class);
                                            startActivity(i);
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
        else{
            String keyW = genKeys(nom.getText().toString(),director.getText().toString(),anno.getText().toString(),spinner.getSelectedItem().toString(),acts.getText().toString());
            Pelicula p = new Pelicula("NULL",nom.getText().toString(),director.getText().toString(),anno.getText().toString(),spinner.getSelectedItem().toString(),des.getText().toString(),acts.getText().toString(),keyW);
            rootRefence.child("Peliculas").child(p.getNombre()).child("Info").setValue(p)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Pelicula Agregada Correctamente", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent k = new Intent(getApplicationContext(),AdminActivity.class);
                            startActivity(k);
                        }
                    });
        }
    }

    private String genKeys(String n,String dir, String a,String g, String acts){
            return n+","+dir+","+a+","+g+","+acts;

    }



}
