package com.spotters.dispatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.Random;

public class Rider_View extends AppCompatActivity {
    EditText pn,pw,rn,rno,sa,ra;
    TextView na,nu,dn,am,l,id,dr;
    ImageView bc;
    String fid, order_id, name, mPhone, driver, pickup, amount, destination, ID;
    //int amount;
    Button sd, ts,ds;
    ProgressBar loading;
    SessionManager sessionManager;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_view);

        na = findViewById(R.id.name);
        nu = findViewById(R.id.num);
        dn = findViewById(R.id.dname);
        am = findViewById(R.id.amount);
        l = findViewById(R.id.location);
        dr = findViewById(R.id.destination);
        id = findViewById(R.id.id);
        bc = findViewById(R.id.back);
        pn = findViewById(R.id.PN);
        pw = findViewById(R.id.PW);
        rn = findViewById(R.id.RN);
        rno = findViewById(R.id.RNO);
        sa = findViewById(R.id.SA);
        ra = findViewById(R.id.RA);
        sd = findViewById(R.id.button);
        loading = findViewById(R.id.progressBar);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String,String> user = sessionManager.getUserInfo();
        fid = user.get(sessionManager.ID);
        final String fn = user.get(sessionManager.FIRSTNAME);
        final String ln = user.get(sessionManager.LASTNAME);
        String mId = user.get(sessionManager.ID);
        mPhone = user.get(sessionManager.PHONE);

        //String id = String.valueOf(fid);

        name = fn + " " + ln;

        driver = getIntent().getStringExtra("name");
        amount = getIntent().getStringExtra("amount");
        pickup = getIntent().getStringExtra("location");
        destination = getIntent().getStringExtra("destination");
        ID = getIntent().getStringExtra("ID");

        //String location = pickup;

        //System.out.println(location);


        na.setText(name);
        nu.setText(mPhone);
        dn.setText(driver);
        am.setText(amount);
        l.setText(pickup);
        id.setText(ID);
        dr.setText(destination);

        bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent go = new Intent(Rider_View.this, Dashboard.class);
                onBackPressed();

            }
        });

        sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nPN = pn.getText().toString().trim();
                String nPW = pw.getText().toString().trim();
                String nRN = rn.getText().toString().trim();
                String nRNO = rno.getText().toString().trim();
                String nRA = ra.getText().toString().trim();
                String nSA = sa.getText().toString().trim();

                if(!nPN.isEmpty() && !nPW.isEmpty() && !nRN.isEmpty() && !nRNO.isEmpty() && !nRA.isEmpty() && !nSA.isEmpty()){
                    Submit();
                }else{
                    pn.setError("Type Package Name");
                    pw.setError("Type Package Weight");
                    rn.setError("Type Receiver Name");
                    rno.setError("Type Receiver Number");
                    ra.setError("Type receiver address");
                    sa.setError("Type sender address");
                }
            }
        });
    }

    private void Submit() {
        sd.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        String NAME = name;
        String PHONE = mPhone;

        Random random = new Random();
        int randtext = random.nextInt(10000);
        order_id = "SPD-" + String.valueOf(randtext);

        String order = order_id;

        final String package_name = this.pn.getText().toString().trim();
        final String package_weight = this.pw.getText().toString().trim();
        final String receiver_name = this.rn.getText().toString().trim();
        final String receiver_number = this.rno.getText().toString().trim();
        final String sender_address = this.sa.getText().toString().trim();
        final String receiver_address = this.ra.getText().toString().trim();
        //String locate = location;
        //final String receiver_location = this.ra.getText().toString().trim();

        Intent intent = new Intent(Rider_View.this, Pay.class);
        intent.putExtra("amount", amount);
        intent.putExtra("order_id", order);
        intent.putExtra("destination", destination);
        intent.putExtra("ID", ID);
        intent.putExtra("rider_name", driver);
        intent.putExtra("location", pickup);
        intent.putExtra("package_name", package_name);
        intent.putExtra("package_weight", package_weight);
        intent.putExtra("receiver_name", receiver_name);
        intent.putExtra("receiver_number", receiver_number);
        intent.putExtra("sender_address", sender_address);
        intent.putExtra("receiver_address", receiver_address);
        Toast.makeText(this, "Order Taken", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        //System.out.println(pickup);
        finish();
    }
}