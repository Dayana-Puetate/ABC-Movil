package dev.android.preferencias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Principal extends AppCompatActivity {

    TextView txtUsuario, nuevoDos;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        txtUsuario = findViewById(R.id.textView10);
        SharedPreferences preferencesUser = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String name = preferencesUser.getString("name","");
        name = "Bienvenido "+name;
        txtUsuario.setText(name);
        String name2 = preferencesUser.getString("name","");


        Button btnPrestamo=findViewById(R.id.btn_Prestamo);
        btnPrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent (v.getContext(), Prestamo.class);
                startActivityForResult(a, 0);
            }
        });

        Button btnDevolucion=findViewById(R.id.btn_Devolucion);
        btnDevolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent x = new Intent (vw.getContext(), Devolucion.class);
                startActivityForResult(x, 0);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_superior, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.opCerrarSesion){
            SharedPreferences preferencesUser = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            preferencesUser.edit().clear().apply();
            Intent i = new Intent(Principal.this,MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }



    private void RegistrarDetalle(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(Principal.this, "Insertado", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Principal.this,"Su pedido no se ha registrado", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("IdPedido", nuevoDos.getText().toString());
                return parametros;
            }
        };
        queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }


}