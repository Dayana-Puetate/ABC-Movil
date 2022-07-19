package dev.android.preferencias;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Prestamo extends AppCompatActivity {

    Button button, button2, btnGuardar;
    EditText id_libro, ced_est;
    TextView nuevo, txtfecha, nom, dir, correo, matricula, cel, info, titulo;
    RequestQueue queue;
    private int dia, mes, anio;
    String estadoInicial, idInicial, bibliotecario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestamo);

        button= findViewById(R.id.button_dev);
        button2 = findViewById(R.id.btn_verificar);
        btnGuardar = findViewById(R.id.btn_guardar);
        info = findViewById(R.id.info_libro);

        id_libro=findViewById(R.id.id_libro_dev);
        ced_est =findViewById(R.id.id_libro_dev2);
        txtfecha = findViewById(R.id.fechaTXT);

        //nuevo = findViewById(R.id.info_estudiante);
        nom = findViewById(R.id.nombre);
        dir = findViewById(R.id.direccion);
        correo = findViewById(R.id.Email);
        matricula = findViewById(R.id.matricula);
        cel = findViewById(R.id.cel);

        titulo = findViewById(R.id.titulo);

        estadoInicial="A";
        idInicial="1";
        bibliotecario="040";

        mostrarFecha();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator= new IntentIntegrator(Prestamo.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector - CDP");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //buscarEstudiante();
                //buscarEstudiante("http://proyectofinal.somee.com/proyectoDAS/EstudianteServicio.svc/DevolverEstudiante?cedula=" + ced_est.getText().toString());
                buscarEstudiante("http://proyectofinal.somee.com/proyectoDAS/EstudianteServicio.svc/DevolverUsuarioPorCedula?cedula="+ced_est.getText().toString());
                activar();


            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarPrestamo("http://proyectofinal.somee.com/proyectoDAS/PrestamoServicio.svc/GuardarPrestamo?bib=040&est="+ced_est.getText().toString()+"&fecha="+txtfecha.getText().toString()+"&estado=A");
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarLibro("http://proyectofinal.somee.com/proyectoDAS/LibroServicio.svc/DevolverLibroID?id="+id_libro.getText().toString());
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null) {
            if(result.getContents()==null){
                Toast.makeText(this,"Lectora cancelada", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,result.getContents(), Toast.LENGTH_LONG).show();
                id_libro.setText(result.getContents());

            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void mostrarFecha(){
        Calendar fecha = Calendar.getInstance();
        dia = fecha.get(Calendar.DAY_OF_MONTH);
        mes = fecha.get(Calendar.MONTH)+1;
        anio = fecha.get(Calendar.YEAR);
        txtfecha.setText(dia+"/"+mes+"/"+anio);
    }

    public void activar(){
        nom.setVisibility(View.VISIBLE);
        dir.setVisibility(View.VISIBLE);
        correo.setVisibility(View.VISIBLE);
        matricula.setVisibility(View.VISIBLE);
        cel.setVisibility(View.VISIBLE);
    }

    public void buscarEstudiante(String URL) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    try {
                        nom.setText(response.getString("NOM1_EST"));
                        dir.setText(response.getString("DIR_EST"));
                        correo.setText(response.getString("EMAIL_EST"));
                        matricula.setText(response.getString("NUM_MAT_EST"));
                        cel.setText(response.getString("CEL_REPRESENTANTE_EST"));
                        //base.setText(response.getString("TotalPedido"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Prestamo.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    public void buscarLibro(String URL) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Colocamos el name que nos trae el servicio
                    titulo.setText(response.getString("Titulo"));
                    //dir.setText(response.getString("AUTOR_LIB"));
                    //correo.setText(response.getString("EDITORIAL_LIB"));
                    //matricula.setText(response.getString("NUM_EDI_LIB"));
                    //cel.setText(response.getString("CEL_REPRESENTANTE_EST"));
                    //base.setText(response.getString("TotalPedido"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Prestamo.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private void guardarPrestamo(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "Insertado", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Prestamo.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                //parametros.put("USU_PRES", bibliotecario.toString());
                parametros.put("est", ced_est.getText().toString());
                parametros.put("fecha", txtfecha.getText().toString());
                //parametros.put("estado", estadoInicial.toString());
                return parametros;
            }
        };
        queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }












    /*txtCedula = findViewById(R.id.usuEdt);
        SharedPreferences preferencesUser = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String name = preferencesUser.getString("name","");
        txtUsuario.setText(name);

        rev = findViewById(R.id.revDos);
        rev.setHasFixedSize(true);
        rev.setLayoutManager(new LinearLayoutManager(this));

        listaPedidos = new ArrayList<>();
        cargarPedidos("http://proyectofinal.somee.com/proyectoFinal/Pedidos.svc/DevolverListadoPedidosCliente?cliente="+txtUsuario.getText().toString());
    }*/

    /*public void cargarPedidos(String URL){
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray cadena = new JSONArray(response);
                    for (int i = 0; i < cadena.length(); i++) {
                        JSONObject jsonObject = cadena.getJSONObject(i);
                        listaPedidos.add(new Pedido(jsonObject.getInt("IdPedido"),
                                jsonObject.getString("FechaPedido"),
                                jsonObject.getString("Direccion"),
                                jsonObject.getString("EstadoPedido"),
                                jsonObject.getString("NombreR"),
                                jsonObject.getDouble("TotalPedido")));
                    }
                    AdapterPedido adapter = new AdapterPedido(VerPedidos.this, listaPedidos);
                    //adapter.setOnClickListener(new View.OnClickListener() {
                       // @Override
                        //public void onClick(View view) {
                          //  Toast.makeText(getApplicationContext(),"Seleccionó: " + listaPedidos.get(rev.getChildAdapterPosition(view)).getDir(),Toast.LENGTH_SHORT).show();
                            //Intent intent = new Intent(view.getContext(), Pedido.class);
                            //startActivityForResult(intent,0);
                            //DetallePedido detallePedido =new DetallePedido();
                            //detallePedido.show(getSupportFragmentManager(),"DetallePedido");
                        //}
                    //});
                    rev.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VerPedidos.this,"No tiene pedidos pendientes",Toast.LENGTH_SHORT).show();
            }
        });
        queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    */
}