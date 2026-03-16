package com.example.storeperu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnRegistrar, btnListar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnListar = findViewById(R.id.btnListar);

        btnRegistrar.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), RegistrarProducto.class));
        });

        btnListar.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ListarProducto.class));
        });
    }
}