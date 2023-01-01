package com.spotters.dispatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    ImageView BtnBk;
    TextView FN,LN,PH,EM;
    Button BtnEdit;
    String FNN,LNN,PHH,EMM, fid;
    SessionManager sessionManager;

    private static String url_profile ="https://iufmp.spotters.tech/android/profile_read.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BtnBk = findViewById(R.id.back);
        FN = findViewById(R.id.firstname);
        LN = findViewById(R.id.lastname);
        PH = findViewById(R.id.phone);
        EM = findViewById(R.id.email);
        BtnEdit = findViewById(R.id.btn_edit);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String,String> user = sessionManager.getUserInfo();
        fid = user.get(sessionManager.ID);
        final String fn = user.get(sessionManager.FIRSTNAME);
        final String ln = user.get(sessionManager.LASTNAME);
        String mPhone = user.get(sessionManager.PHONE);
        String mEMAIL = user.get(sessionManager.EMAIL);

        FN.setText(fn);
        LN.setText(ln);
        PH.setText(mPhone);
        EM.setText(mEMAIL);

        BtnBk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        BtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(Profile.this,EditProfile.class);
                startActivity(edit);
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_profile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i =0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        FNN = object.getString("firstname").trim();
                        LNN = object.getString("lastname").trim();
                        PHH = object.getString("phone").trim();
                        EMM = object.getString("email").trim();

                        //String firstname = FNN;
                    }
                    FN.setText(FNN);
                    LN.setText(LNN);
                    PH.setText(PHH);
                    EM.setText(EMM);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> test = new HashMap<>();
                test.put("id", fid);
                return test;
            }
        };
    }
}