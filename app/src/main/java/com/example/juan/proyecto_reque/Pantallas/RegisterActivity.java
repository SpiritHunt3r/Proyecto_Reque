package com.example.juan.proyecto_reque.Pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.juan.proyecto_reque.Clases.Pelicula;
import com.example.juan.proyecto_reque.Clases.Usuario;
import com.example.juan.proyecto_reque.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    EditText email, pass, pass2, name;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.emailText);
        pass = findViewById(R.id.passwordText);
        pass2 = findViewById(R.id.passwordText2);
        name = findViewById(R.id.nombreText);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

    }

    public void createUser(View v){
        final String ActualEmail = email.getText().toString();
        String ActualPass = pass.getText().toString();
        final String Nombre = name.getText().toString();
        String PassCheck = pass2.getText().toString();

        if (ActualEmail.equals("") || ActualPass.equals("") || Nombre.equals("") || PassCheck.equals("")){
            Toast.makeText(getApplicationContext(),"No se permite espacios en blanco",Toast.LENGTH_SHORT).show();
        }
        else if (!ActualPass.equals(PassCheck)){
            Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
        }
        else{
            auth.createUserWithEmailAndPassword(ActualEmail,ActualPass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                firebaseUser = auth.getCurrentUser();

                                Usuario u = new Usuario(ActualEmail,Nombre,true,true);
                                ref.child("Usuarios").child(firebaseUser.getUid()).setValue(u);
                                ref.child("Usuarios").child(firebaseUser.getUid()).child("Peliculas");
                                Toast.makeText(getApplicationContext(),"Usuario creado correctamente",Toast.LENGTH_SHORT).show();
                                finish();
                                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"No es posible crear el usuario",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }

    }

    public void backMain(View v){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

}
