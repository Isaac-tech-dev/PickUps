package com.spotters.dispatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Complaint extends AppCompatActivity {
    EditText comp;
    ImageView bc;
    ProgressBar loading;
    Button btn;
    Spinner spinnercomplain;
    ArrayList<String> complainlist = new ArrayList<>();
    ArrayAdapter<String> complainadapter;
    RequestQueue requestQueue;
    SessionManager sessionManager;
    String fid;

    private static String url_comp = "https://iufmp.spotters.tech/android/complaint.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        spinnercomplain = findViewById(R.id.complain_spinner);
        bc = findViewById(R.id.back);

        bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Endpoints
        String url_complain = "https://iufmp.spotters.tech/android/spinner_options_comp_category.php";

        //Spinner Begins
        //Complain Spinner
        JsonObjectRequest complainjsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_complain, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("complaint_cat");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String complainname = jsonObject.optString("comp_category");
                        complainlist.add(complainname);
                        complainadapter = new ArrayAdapter<>(Complaint.this, android.R.layout.simple_spinner_item, complainlist);
                        complainadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnercomplain.setAdapter(complainadapter);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Complaint.this, "Error!!!" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Complaint.this, "Error!!!" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(complainjsonObjectRequest);
        //Complain Spinner


        sessionManager = new SessionManager(this);

        comp = findViewById(R.id.complain);
        btn = findViewById(R.id.button3);
        loading = findViewById(R.id.loading);


        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserInfo();
        fid = user.get(sessionManager.ID);
    }

    private void complain() {
        loading.setVisibility(View.VISIBLE);
        btn.setVisibility(View.GONE);

        final String complaint = this.comp.getText().toString().trim();
        String id = fid;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_comp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        Intent complain = new Intent(Complaint.this, Track.class);
                        startActivity(complain);
                        finish();
                        Toast.makeText(Complaint.this, "Complain Successful", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn.setVisibility(View.VISIBLE);

                        //Spinner transfer
                        //Intent move = new Intent(ComplaintLog.this, TrackComplaint.class);
                        //move.putExtra("comp_category", category.toString());
                        //move.putExtra("issue_name", issuetype.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Complaint.this, "Errorr!!!!!" + e.toString(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btn.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Complaint.this, "Error!!!!!!!!" + error.toString(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                btn.setVisibility(View.VISIBLE);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> complain = new HashMap<>();
                complain.put("c_message", complaint);
                complain.put("sent_by", id);
                return complain;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void buttonsender(View v) {
        Intent send = new Intent(this, Track.class);
        send.putExtra("KEY_SENDER", complainadapter.getContext().toString());
        startActivity(send);
    }
}