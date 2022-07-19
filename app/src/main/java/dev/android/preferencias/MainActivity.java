package dev.android.preferencias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText nom, cla, edtGuardarUsu;
    TextView txtRegistro;
    Switch swlogin;
    Button btnLogin;
    RequestQueue queue;
    String usuario, clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        usuario = clave = "";
        nom = findViewById(R.id.edtUsername);
        cla = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        swlogin = findViewById(R.id.recordar);
        preferenciaGuardada();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = nom.getText().toString().trim();
                clave = cla.getText().toString().trim();
                if(!usuario.equals("")&&!clave.equals("")) {
                    login("http://proyectofinal.somee.com/proyectoDAS/UsuarioServicio.svc/Login?usuario=" + nom.getText().toString() + "&tipo=A&contrasena=" + cla.getText().toString());
                    //login("http://proyectofinal.somee.com/proyectoDAS/ServicioUsuario.svc/Login?usuario=dayana&tipo=A&contrasena=123");
                }else{
                    Toast.makeText(MainActivity.this,"No se permiten calllllllllmpos vacíos", Toast.LENGTH_SHORT).show();
                }
                }
        });

        /*Button btnlogin=findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), Principal.class);
                startActivityForResult(intent, 0);
            }
        }));*/

    }



    private void login(String URL){
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("true")) {
                        boolean guardarSesion = swlogin.isChecked();
                        if(guardarSesion){
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("name",usuario);
                            editor.commit();
                        }
                        Intent intent = new Intent(MainActivity.this, Principal.class);
                        startActivity(intent);
                    } else if(response.equals("false")){
                        Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this,"No se permiten campos vacíos", Toast.LENGTH_SHORT).show();
                }
            }){
                protected Map<String,String> getParametros() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    /*parametros.put("usuario",nom.getText().toString());
                    parametros.put("contrasena",cla.getText().toString());*/
                    parametros.put("usuario",usuario);
                    parametros.put("contrasena",clave);
                    return parametros;
                }
            };
            queue = Volley.newRequestQueue(this);
            queue.add(request);
        //}
    }


    private void preferenciaGuardada(){
        SharedPreferences preferencesUser = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferencesUser.getString("name",null);
        if (name != null){
            Intent intent = new Intent(MainActivity.this, Principal.class);
            startActivity(intent);
        }
    }

}