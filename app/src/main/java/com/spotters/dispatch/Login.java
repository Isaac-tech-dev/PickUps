package com.spotters.dispatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText phone, password;
    ProgressBar loading;
    MaterialButton Btn;

    private static String URL_LOGIN = "https://iufmp.spotters.tech/android/login.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        Btn = findViewById(R.id.button);
        loading = findViewById(R.id.progressBar);

        Btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String mPhone =phone.getText().toString().trim();
        String mPassword =password.getText().toString().trim();

        if(!mPhone.isEmpty() || !mPassword.isEmpty()){
            Welcome(mPhone,mPassword);
        }else{
            phone.setError("Type Phone number");
            password.setError("Type password");
        }
    }

    private void Welcome(final String phone, final String password) {
        Btn.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray =jsonObject.getJSONArray("login");

                    if(success.equals("1")){
                        for(int i = 0; i < jsonObject.length(); i++){
                            JSONObject object =jsonArray.getJSONObject(i);
                            String firstname = object.getString("firstname").trim();
                            String lastname = object.getString("lastname").trim();
                            String phone = object.getString("phone").trim();
                            String email = object.getString("email").trim();
                            String id = object.getString("id").trim();
                            String sign_up_date = object.getString("sign_up_date").trim();

                            sessionManager.createSession(firstname, lastname, phone, email, id, sign_up_date);

                            Toast.makeText(Login.this, "Welcome", Toast.LENGTH_SHORT).show();
                            Intent gotoWelcomeActivity = new Intent(Login.this, Dashboard.class);
                            gotoWelcomeActivity.putExtra("firstname", firstname);
                            gotoWelcomeActivity.putExtra("lastname", lastname);
                            gotoWelcomeActivity.putExtra("phone", phone);
                            gotoWelcomeActivity.putExtra("email", email);
                            gotoWelcomeActivity.putExtra("id", id);
                            startActivity(gotoWelcomeActivity);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Login.this, "Login Failed!!!!!" + e.toString(), Toast.LENGTH_SHORT).show();
                    Btn.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "Login Failed!!!!!" +error.toString(), Toast.LENGTH_SHORT).show();
                Btn.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> login = new HashMap<>();
                login.put("phone", phone);
                login.put("password", password);
                return login;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void Click(View view){
        Intent register = new Intent(Login.this, Register.class);
        startActivity(register);
    }
}