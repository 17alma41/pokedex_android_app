package com.example.pokeapi.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pokeapi.R;

public class LaunchActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // Temporizador para mostrar el splash screen por unos segundos
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ir a la pantalla principal (MainActivity)
                Intent intent = new Intent(LaunchActivity.this, MenuActivity.class);
                startActivity(intent);
                finish(); // Cierra la actividad para que no pueda volver atrás
            }
        }, SPLASH_TIME_OUT);
    }
}