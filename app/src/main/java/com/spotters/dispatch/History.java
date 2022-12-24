package com.spotters.dispatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class History extends AppCompatActivity implements history_message.ItemClickListener {
    ImageView bk;
    RecyclerView recyclerView;
    history_message adapter;
    ArrayList<history_show> historymessage = new ArrayList<>();
    SessionManager sessionManager;
    String fid;

    String Pickup_location, Destination;

    private static String url_history = "https://spotters.tech/dispatch_app/android/dispatch_history.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        sessionManager = new SessionManager(History.this);
        sessionManager.checkLogin();



        HashMap<String,String> user = sessionManager.getUserInfo();
        fid = user.get(sessionManager.ID);


        recyclerView = findViewById(R.id.history_recycle);
        bk = findViewById(R.id.back);

        bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setHasFixedSize(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_history, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        Pickup_location = object.getString("sender_address").trim();
                        Destination = object.getString("receiver_address").trim();
                        String ID = object.getString("order_id").trim();

                        history_show show = new history_show(Pickup_location,Destination,ID);
                        historymessage.add(0,show);
                    }
                    adapter = new history_message(historymessage,History.this);
                    recyclerView.setAdapter(adapter);

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
                HashMap<String, String> hist = new HashMap<>();
                hist.put("sender_id",fid);
                hist.put("status", "Completed");
                return hist;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClick(history_show history_show) {

    }
}