package com.spotters.dispatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    EditText fn,ln,ph,em;
    Button btn;
    ImageView BtnBack;
    ProgressBar load;
    SessionManager sessionManager;
    String fid;

    private static String URL_UPDATE = "https://iufmp.spotters.tech/android/edit_profile_data_sam.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fn = findViewById(R.id.firstname);
        ln = findViewById(R.id.lastname);
        ph = findViewById(R.id.phone);
        em = findViewById(R.id.email);
        btn = findViewById(R.id.edit);
        load = findViewById(R.id.loading);
        BtnBack = findViewById(R.id.back);

        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = fn.getText().toString().trim();
                String lastname = ln.getText().toString().trim();
                String email = em.getText().toString().trim();
                String phone = ph.getText().toString().trim();

                if(firstname.isEmpty() && lastname.isEmpty() && email.isEmpty() && phone.isEmpty()){
                    fn.setError("Put in Firstname");
                    ln.setError("Put in Lastname");
                    em.setError("Put in Email");
                    ph.setError("Put in Phone Number");
                }else{
                    Edit();
                }
            }
        });
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String,String> user = sessionManager.getUserInfo();
        String Firstname = user.get(sessionManager.FIRSTNAME);
        String Lastname = user.get(sessionManager.LASTNAME);
        String Email = user.get(sessionManager.EMAIL);
        String Phone = user.get(sessionManager.PHONE);
        fid = user.get(sessionManager.ID);

        fn.setText(Firstname);
        ln.setText(Lastname);
        em.setText(Email);
        ph.setText(Phone);


    }

    private void Edit() {
        load.setVisibility(View.VISIBLE);
        btn.setVisibility(View.GONE);

        final String firstname = this.fn.getText().toString().trim();
        final String lastname = this.ln.getText().toString().trim();
        final String phone = this.ph.getText().toString().trim();
        final String email = this.em.getText().toString().trim();
        String id = fid;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(EditProfile.this, "PROFILE UPDATE SUCCESSFUL", Toast.LENGTH_SHORT).show();
                                load.setVisibility(View.GONE);
                                btn.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditProfile.this, "UPDATE ERROR!" + e.toString(), Toast.LENGTH_SHORT).show();
                            btn.setVisibility(View.VISIBLE);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfile.this,"UPDATE ERROR" + error.toString(), Toast.LENGTH_SHORT).show();
                        btn.setVisibility(View.VISIBLE);
                    }
                })

        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("email", email);
                params.put("phone", phone);
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}