package com.spotters.dispatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Track extends AppCompatActivity implements track_message.ItemClickListener {
    RecyclerView recyclerView;
    track_message adapter;
    //dispatch_message adapter2;
    ArrayList<track_show> trackmessage = new ArrayList<>();
    TextView RNA, RNU, ID;
    ImageView BACK;
    ImageView CALL, gif1,gif2;
    Button BTN;
    Button ts,ds;
    SessionManager sessionManager;
    String  Rider_name,Rider_number, fid, Sender_address, Receiver_address, Amount;

    private static String url_track = "https://spotters.tech/dispatch-it/android/track_dispatch.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        recyclerView = findViewById(R.id.track_recycle);
        //recyclerView2 = findViewById(R.id.dispatch_recycle);
        //swipeRefreshLayout = findViewById(R.id.refresh);



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setHasFixedSize(true);
        //recyclerView2.setHasFixedSize(true);

        //RNA = findViewById(R.id.name);
        //RNU = findViewById(R.id.number);

        CALL = findViewById(R.id.call);
        //gif1 = findViewById(R.id.gif1);
        //gif2 = findViewById(R.id.gif2);
        //MESSAGE = findViewById(R.id.message);
        BACK = findViewById(R.id.back);
        //BTN = findViewById(R.id.track);
        //ts = findViewById(R.id.tow_service);
        //ds = findViewById(R.id.dispatch_service);

        String id = getIntent().getStringExtra("reference");
        System.out.println(id);
        //String id = getIntent().getStringExtra("reference");

        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(Track.this, Dashboard.class);
                onBackPressed();
                //startActivity(intent);
            }
        });

        //RNA.setText();
        //RNU.setText();

        HashMap<String,String> user = sessionManager.getUserInfo();
        fid = user.get(sessionManager.ID);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_track, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        Rider_name = object.getString("rider_name").trim();
                        Rider_number = object.getString("rider_phone").trim();
                        String id = object.getString("order_id").trim();
                        Sender_address = object.getString("sender_address").trim();
                        Receiver_address = object.getString("receiver_address").trim();
                        Amount = object.getString("amount").trim();

                        //RNA.setVisibility(View.VISIBLE);
                        //RNU.setVisibility(View.VISIBLE);

                        //gif1.setVisibility(View.GONE);
                        //gif2.setVisibility(View.GONE);

                        track_show show = new track_show(Rider_name,Rider_number,id,Sender_address,Receiver_address,Amount);
                        trackmessage.add(0,show);

                        //CALL.setVisibility(View.VISIBLE);
                        //MESSAGE.setVisibility(View.VISIBLE);
                        //BTN.setVisibility(View.VISIBLE);


                    }
                    adapter = new track_message(trackmessage,Track.this);
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
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> track = new HashMap<>();
                //track.put("reference", id);
                track.put("sender_id", fid);
                track.put("status", "Accepted");
                return track ;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onItemClick(track_show show2) {
        String Call = show2.getRider_phone();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+ Call));
        startActivity(intent);
    }
}