package com.example.juan.proyecto_reque.Pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.juan.proyecto_reque.Clases.Usuario;
import com.example.juan.proyecto_reque.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText e1, e2;

    private FirebaseAuth auth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = findViewById(R.id.emailText);
        e2 = findViewById(R.id.passwordText);
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
    }


    public void ORegister(View v){
        Intent i = new Intent(this,RegisterActivity.class);
        startActivity(i);
    }

    public void login(View v){


        if (e1.getText().toString().equals("")  || e2.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"No se permite espacios en blanco",Toast.LENGTH_SHORT).show();
        }
        else{
            String email = e1.getText().toString();
            String pass = e2.getText().toString();
            auth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                finish();

                                ref = ref.child("Usuarios").child(auth.getCurrentUser().getUid()).child("Personal Info");
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Usuario u = dataSnapshot.getValue(Usuario.class);
                                        if (u.getIs_admin()){
                                            Intent i = new Intent(getApplicationContext(),AddPeliculaActivity.class);
                                            startActivity(i);
                                        }
                                        else{
                                            Intent i = new Intent(getApplicationContext(),ClienteActivity.class);
                                            startActivity(i);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Credenciales incorrectas\nIntente nuevamente",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}
