package com.spotters.dispatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText fn, ln, em, ph, p;
    Button Btn;
    ProgressBar loading;

    private static String URL_SIGNUP = "https://iufmp.spotters.tech/android/userreg.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Intiate Variables
        fn = findViewById(R.id.firstname);
        ln = findViewById(R.id.lastname);
        em = findViewById(R.id.email);
        ph = findViewById(R.id.phone);
        p = findViewById(R.id.password);
        Btn = findViewById(R.id.button);
        loading = findViewById(R.id.progressBar);

        Btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String mFirstname = fn.getText().toString().trim();
        String mLastname = ln.getText().toString().trim();
        String mEmail = em.getText().toString().trim();
        String mPhone = ph.getText().toString().trim();
        String mPassword = p.getText().toString().trim();

        if(!mFirstname.isEmpty() && !mLastname.isEmpty() && !mEmail.isEmpty() && !mPhone.isEmpty() && !mPassword.isEmpty()){
            Proceed();
        }else{
            fn.setError("Please type First Name");
            ln.setError("Please type Last Name");
            em.setError("Please type Email");
            ph.setError("Please type Phone Number");
            p.setError("Please type Password");
        }
    }

    private void Proceed() {
        Btn.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        final String firstname =this.fn.getText().toString().trim();
        final String lastname =this.ln.getText().toString().trim();
        final String phone =this.ph.getText().toString().trim();
        final String email =this.em.getText().toString().trim();
        final String password =this.p.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if(success.equals("1")){
                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Register.this, "REGISTRATION ERROR!" + e.toString(), Toast.LENGTH_SHORT).show();
                    Btn.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, "REGISTRATION ERROR" + error.toString(), Toast.LENGTH_SHORT).show();
                Btn.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> register = new HashMap<>();
                register.put("firstname", firstname);
                register.put("lastname", lastname);
                register.put("email", email);
                register.put("phone", phone);
                register.put("password", password);
                return register;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}