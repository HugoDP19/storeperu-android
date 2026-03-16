package com.example.storeperu;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class RegistrarProducto extends AppCompatActivity {

    Spinner spnMarcas;
    EditText edtDescripcion, edtPrecio, edtStock, edtGarantia;
    Button btnRegistrarProducto;

    RequestQueue requestQueue;

    ArrayList<String> listaMarcas = new ArrayList<>();
    ArrayList<Integer> listaIdMarcas = new ArrayList<>();

    private final String URL_MARCAS = "http://10.0.2.2:3000/marcas";
    private final String URL_PRODUCTOS = "http://10.0.2.2:3000/productos";

    private void loadUI(){
        spnMarcas = findViewById(R.id.spnMarcas);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtPrecio = findViewById(R.id.edtPrecio);
        edtStock = findViewById(R.id.edtStock);
        edtGarantia = findViewById(R.id.edtGarantia);
        btnRegistrarProducto = findViewById(R.id.btnRegistrarProducto);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);

        loadUI();

        requestQueue = Volley.newRequestQueue(this);

        cargarMarcas();

        btnRegistrarProducto.setOnClickListener(v -> registrarProducto());
    }

    private void cargarMarcas(){

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_MARCAS,
                null,

                response -> {

                    try {

                        listaMarcas.clear();
                        listaIdMarcas.clear();

                        for(int i = 0; i < response.length(); i++){

                            JSONObject obj = response.getJSONObject(i);

                            listaMarcas.add(obj.getString("marca"));
                            listaIdMarcas.add(obj.getInt("id"));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                listaMarcas
                        );

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spnMarcas.setAdapter(adapter);

                    } catch (Exception e){
                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                    }

                },

                error -> Toast.makeText(this,"Error al cargar marcas",Toast.LENGTH_LONG).show()
        );

        requestQueue.add(request);
    }

    private void registrarProducto(){

        String descripcion = edtDescripcion.getText().toString().trim();
        String precioTxt = edtPrecio.getText().toString().trim();
        String stockTxt = edtStock.getText().toString().trim();
        String garantiaTxt = edtGarantia.getText().toString().trim();

        if(descripcion.isEmpty() || precioTxt.isEmpty() || stockTxt.isEmpty() || garantiaTxt.isEmpty()){
            Toast.makeText(this,"Complete todos los campos",Toast.LENGTH_LONG).show();
            return;
        }

        if(listaIdMarcas.size() == 0){
            Toast.makeText(this,"Las marcas aún no cargan",Toast.LENGTH_LONG).show();
            return;
        }

        try {

            int posicion = spnMarcas.getSelectedItemPosition();
            int idmarca = listaIdMarcas.get(posicion);

            double precio = Double.parseDouble(precioTxt);
            int stock = Integer.parseInt(stockTxt);
            int garantia = Integer.parseInt(garantiaTxt);

            JSONObject json = new JSONObject();

            json.put("idmarca", idmarca);
            json.put("descripcion", descripcion);
            json.put("precio", precio);
            json.put("stock", stock);
            json.put("garantia", garantia);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    URL_PRODUCTOS,
                    json,

                    response -> {

                        try {

                            int id = response.getInt("id");

                            Toast.makeText(
                                    this,
                                    "Producto registrado. ID: " + id,
                                    Toast.LENGTH_LONG
                            ).show();

                            limpiarCampos();

                        } catch (Exception e){
                            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
                        }

                    },

                    error -> Toast.makeText(this,"Error al registrar producto",Toast.LENGTH_LONG).show()
            );

            requestQueue.add(request);

        } catch (Exception e){
            Toast.makeText(this,"Error: " + e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private void limpiarCampos(){
        edtDescripcion.setText("");
        edtPrecio.setText("");
        edtStock.setText("");
        edtGarantia.setText("");
    }
}