package com.spotters.dispatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dispatch extends AppCompatActivity implements dispatch_adapter.ItemClickListener {
    RecyclerView recyclerView;
    dispatch_adapter adapter;
    ArrayList<show1> driverlist = new ArrayList<>();
    EditText PL, DP;
    Button Btn;
    ImageView bk;
    ProgressBar loading;
    String Amount, FN, LN, N, ID, mLocation, mDestination, pickup, dropoff, phone;
    Spinner spinner1, spinner2;
    ArrayList<String> pickuplist = new ArrayList<>();
    ArrayAdapter<String> pickupadapter;
    ArrayList<String> destinationlist = new ArrayList<>();
    ArrayAdapter<String> destinationadapter;


    private static String url_rider = "https://spotters.tech/dispatch-it/android/driver_request.php";

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);

        PL = findViewById(R.id.input_search);
        DP = findViewById(R.id.input_destination);
        Btn = findViewById(R.id.search);
        loading = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.rider_recycle);
        bk = findViewById(R.id.back);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        spinner1 = findViewById(R.id.pickup_spinner);
        spinner2 = findViewById(R.id.destination_spinner);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);


        bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLocation = PL.getText().toString().trim();
                mDestination = DP.getText().toString().trim();

                if(!mLocation.isEmpty() && !mDestination.isEmpty()){
                    Search();
                    //System.out.println(mLocation);
                }else{
                    PL.setError("Type Location");
                    DP.setError("Type Destination");
                }
            }
        });

        //Endpoints
        String url_pickup = "https://spotters.tech/dispatch-it/android/product.php";
        String url_dropoff = "https://spotters.tech/dispatch-it/android/product2.php";

        //PICKUP SPINNER BEGINS...........................
        //PICKUP SPINNER
        JsonObjectRequest pickupjsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_pickup, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("product");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String location = jsonObject.optString("pickup_state");
                        pickuplist.add(location);
                        pickupadapter = new ArrayAdapter<>(Dispatch.this, android.R.layout.simple_spinner_item, pickuplist);
                        pickupadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner1.setAdapter(pickupadapter);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Dispatch.this, "Error!!!" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Dispatch.this, "Error!!!" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(pickupjsonObjectRequest);
        //PICKUP SPINNER ENDS.....................

        //Spinner Begins
        //Complain Spinner
        JsonObjectRequest destinationjsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_dropoff, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("product");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String destination = jsonObject.optString("dropoff_state");
                        destinationlist.add(destination);
                        destinationadapter = new ArrayAdapter<>(Dispatch.this, android.R.layout.simple_spinner_item, destinationlist);
                        destinationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(destinationadapter);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Dispatch.this, "Error!!!" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Dispatch.this, "Error!!!" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(destinationjsonObjectRequest);
        //Complain Spinner
    }

    private void Search() {
        Btn.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        Intent state = new Intent(Dispatch.this,Rider_View.class);
        state.putExtra("Location_address", mLocation);
        state.putExtra("Destination_address", mDestination);


        pickup = this.PL.getText().toString().trim();
        dropoff = this.DP.getText().toString().trim();
        //System.out.println(location);

        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_rider, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i =0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        Amount = object.getString("amount").trim();
                        FN = object.getString("rider_fn").trim();
                        LN = object.getString("rider_ln").trim();
                        pickup = object.getString("location").trim();
                        dropoff = object.getString("destination").trim();
                        phone = object.getString("rider_ph").trim();
                        ID = object.getString("ID").trim();

                        N = FN + " " + LN;

                        System.out.println(dropoff);
                        //String ID = object.getString("reference").trim();

                        //int amount = Integer.parseInt(Amount);
                        show1 show = new show1(N, pickup, dropoff, Amount);
                        driverlist.add(0,show);
                    }
                    adapter = new dispatch_adapter(driverlist, Dispatch.this);
                    recyclerView.setAdapter(adapter);

                    loading.setVisibility(View.GONE);
                    PL.setText(null);
                    DP.setText(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Btn.setVisibility(View.VISIBLE);
                    Toast.makeText(Dispatch.this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Dispatch.this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> get = new HashMap<>();
                get.put("location", pickup);
                get.put("destination", dropoff);
                get.put("status", "Online");
                return get;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClick(show1 show1) {
        String NN = show1.getName();
        String AM = show1.getAmount();
        String PL = show1.getPickup();
        String DL = show1.getDestination();

        Intent send = new Intent(Dispatch.this,Rider_View.class);
        send.putExtra("location", PL);
        send.putExtra("destination", DL);
        send.putExtra("name",NN);
        send.putExtra("rider_phone", phone);
        send.putExtra("amount",AM);
        send.putExtra("ID", ID);
        startActivity(send);
        //System.out.println(location);
    }
    void buttonsender(View v) {
        Intent send = new Intent(this, Track.class);
        send.putExtra("KEY_SENDER", pickupadapter.getContext().toString());
        send.putExtra("KEY_SENDER", destinationadapter.getContext().toString());
        startActivity(send);
    }
}