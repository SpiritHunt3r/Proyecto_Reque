package com.example.juan.proyecto_reque.Pantallas.Admin;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.juan.proyecto_reque.Fragments.FavoritosFragment;
import com.example.juan.proyecto_reque.Fragments.PeliculasAdminFragment;
import com.example.juan.proyecto_reque.Fragments.PeliculasFragment;
import com.example.juan.proyecto_reque.Fragments.RecomendadosFragment;
import com.example.juan.proyecto_reque.Fragments.UsuariosFragment;
import com.example.juan.proyecto_reque.R;

public class AdminActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_peliculas_admin:
                    transaction.replace(R.id.frame_contenedor, new PeliculasAdminFragment()).commit();
                    return true;
                case R.id.navigation_usuarios:
                    transaction.replace(R.id.frame_contenedor, new UsuariosFragment()).commit();
                    return true;
                case R.id.navigation_peliculas:
                    transaction.replace(R.id.frame_contenedor, new PeliculasFragment()).commit();
                    return true;
                case R.id.navigation_favoritos:
                    transaction.replace(R.id.frame_contenedor, new FavoritosFragment()).commit();
                    return true;
                case R.id.navigation_recomendadas:
                    transaction.replace(R.id.frame_contenedor, new RecomendadosFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_contenedor, new PeliculasAdminFragment()).commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
