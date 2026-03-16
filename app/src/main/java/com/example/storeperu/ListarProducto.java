package com.example.storeperu;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListarProducto extends AppCompatActivity {

    ListView lstProductos;
    RequestQueue requestQueue;
    JSONArray productosJSON;

    private final String URL = "http://192.168.18.48:3000/productos";

    private void loadUI(){
        lstProductos = findViewById(R.id.lstProductos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_producto);

        loadUI();

        requestQueue = Volley.newRequestQueue(this);

        obtenerDatos();
    }

    private void obtenerDatos(){

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,

                response -> {

                    productosJSON = response;
                    renderizarListView(response);

                },

                error -> Toast.makeText(this,"Error al obtener datos",Toast.LENGTH_LONG).show()
        );

        requestQueue.add(request);
    }

    private void renderizarListView(JSONArray jsonProductos){

        try {

            ArrayList<String> listaProductos = new ArrayList<>();

            for(int i = 0; i < jsonProductos.length(); i++){

                JSONObject obj = jsonProductos.getJSONObject(i);

                String marca = obj.getString("marca");
                String descripcion = obj.getString("descripcion");

                listaProductos.add(marca + " - " + descripcion);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    listaProductos
            );

            lstProductos.setAdapter(adapter);

            lstProductos.setOnItemClickListener((parent, view, position, id) -> {

                try {

                    JSONObject producto = productosJSON.getJSONObject(position);

                    String marca = producto.getString("marca");
                    String descripcion = producto.getString("descripcion");
                    String precio = producto.getString("precio");
                    String stock = producto.getString("stock");
                    String garantia = producto.getString("garantia");

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("Detalle del Producto");

                    builder.setMessage(
                            "Marca: " + marca +
                                    "\nDescripción: " + descripcion +
                                    "\nPrecio: S/ " + precio +
                                    "\nStock: " + stock +
                                    "\nGarantía: " + garantia + " meses"
                    );

                    builder.setPositiveButton("Cerrar", null);

                    builder.show();

                } catch (Exception e){
                    Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
                }

            });

        } catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
}