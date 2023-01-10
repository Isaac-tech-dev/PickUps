package com.spotters.dispatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class forgot_password extends AppCompatActivity {
    SessionManage sessionManage;
    private String url_password = "https://spotters.tech/dispatch-it/android/update_password.php";
    Button Btn;
    EditText ph;
    ProgressBar pg;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Btn = findViewById(R.id.btnforgot);
        ph = findViewById(R.id.phone);
        pg = findViewById(R.id.loading);

        sessionManage = new SessionManage(this);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Phone = ph.getText().toString().trim();

                if(Phone.isEmpty()){
                    ph.setError("Type Phone number");
                }else{
                    Proceed(Phone);
                }
            }

            private void Proceed(final String Phone) {
                pg.setVisibility(View.VISIBLE);
                Btn.setVisibility(View.GONE);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_password, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("update");

                            if(success.equals("1")){
                                for(int i = 0; i< jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String firstname = object.getString("firstname").trim();
                                    String lastname = object.getString("lastname").trim();
                                    String phone = object.getString("phone").trim();
                                    String email = object.getString("email").trim();
                                    String id = object.getString("id").trim();
                                    String sign_up_date = object.getString("sign_up_date").trim();

                                    sessionManage.createSession(firstname, lastname, phone, email, id, sign_up_date);

                                    Toast.makeText(forgot_password.this, "Phone number Correct", Toast.LENGTH_SHORT).show();
                                    Intent ShowPassword = new Intent(forgot_password.this, Show_Password.class);
                                    ShowPassword.putExtra("firstname", firstname);
                                    ShowPassword.putExtra("lastname", lastname);
                                    ShowPassword.putExtra("phone", phone);
                                    ShowPassword.putExtra("email", email);
                                    ShowPassword.putExtra("id", id);
                                    startActivity(ShowPassword);
                                    pg.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(forgot_password.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                            pg.setVisibility(View.GONE);
                            Btn.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(forgot_password.this, "Error" +error.toString(), Toast.LENGTH_SHORT).show();
                        pg.setVisibility(View.GONE);
                        Btn.setVisibility(View.VISIBLE);
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> pass = new HashMap<>();
                        pass.put("phone", Phone);
                        return pass;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(forgot_password.this);
                requestQueue.add(stringRequest);
            }

        });


//        DisplayMetrics dn = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dn);
//
//        int width = dn.widthPixels;
//        int height = dn.heightPixels;
//
//
//        getWindow().setLayout((int)(width*.8), (int)(height*.5));
//
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.gravity = Gravity.CENTER;
//        params.x = 0;
//        params.y = -10;
//
//        getWindow().setAttributes(params);
    }

}