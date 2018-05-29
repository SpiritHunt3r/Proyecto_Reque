package com.example.juan.proyecto_reque.Pantallas.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juan.proyecto_reque.Clases.Comentario;
import com.example.juan.proyecto_reque.Clases.Usuario;
import com.example.juan.proyecto_reque.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DescpUsuarioActivity extends AppCompatActivity {

    private String Idn;
    private DatabaseReference ref;
    private Usuario u;
    private String key;
    private EditText nom;
    private TextView username;
    private Switch admin,active;
    private Usuario p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descp_usuario);

        nom = findViewById(R.id.nombreText);
        username = findViewById(R.id.usernameText);
        admin = findViewById(R.id.switchAdmin);
        active = findViewById(R.id.switchActive);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Idn = sharedPreferences.getString("Username","");
        ref = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    Usuario t = ds.child("Personal Info").getValue(Usuario.class);
                    if (t.getEmail().equals(Idn)){
                        key = ds.getKey();
                        u = new Usuario(t.getEmail(),t.getNombre(),t.getIs_active(),t.getIs_admin());
                        active.setChecked(t.getIs_active());
                        admin.setChecked(t.getIs_admin());
                        nom.setText(t.getNombre());
                        username.setText(t.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public void backadminusers (View v){
        finish();
        Intent i = new Intent(getApplicationContext(),AdminActivity.class);
        startActivity(i);
    }

    public void updateUser (View v){
        p = new Usuario(u.getEmail(),nom.getText().toString(),active.isChecked(),admin.isChecked());
        if (u.getEmail().equals(p.getEmail()) && u.getNombre().equals(p.getNombre()) && u.getIs_active().equals(p.getIs_active()) && u.getIs_admin().equals(p.getIs_admin())){
            Toast.makeText(getApplicationContext(),"No se detectaron cambios",Toast.LENGTH_SHORT).show();
        }
        else{
            DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(key).child("Personal Info");
            myref.setValue(p);
            DatabaseReference mynames = FirebaseDatabase.getInstance().getReference().child("Peliculas");
            mynames.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        for (DataSnapshot dd: ds.child("Comentarios").getChildren()){
                            Comentario cm = dd.getValue(Comentario.class);
                            if (cm.getEmail().equals(p.getEmail())){
                                cm.setUsername(p.getNombre());
                                DatabaseReference cnN = dd.getRef();
                                cnN.setValue(cm);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Toast.makeText(getApplicationContext(),"Cambios realizados correctamente",Toast.LENGTH_SHORT).show();
            finish();
            Intent k = new Intent(getApplicationContext(),DescpUsuarioActivity.class);
            startActivity(k);
        }
    }
}
