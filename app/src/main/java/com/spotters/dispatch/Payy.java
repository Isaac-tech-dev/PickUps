package com.spotters.dispatch;

import static co.paystack.android.design.widget.BuildConfig.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.design.widget.BuildConfig;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class Payy extends AppCompatActivity {
    //private static final String PSTK_PUBLIC_KEY = "pk_test_f069de6d29089c973072403107bf7eb255f4e1a1";
    private static final String PSTK_PUBLIC_KEY = "pk_live_71cd7f037a29dc8da56e51a0e0ac239141ef8d10";
    TextView c_name,c_num,r_name,r_id,location,destination,money, cid, ccom;
    ImageView Btnback;
    String fid, E_mail;
    private static String URL_MOVE ="https://spotters.tech/dispatch-it/android/dispatch_request.php";

    private EditText mCardNumber;
    private EditText mCardExpiry;
    private EditText mCardCVV;
    SessionManager sessionManager;
    String na, mPhone, rid, AMM, Reference, mLocation, mDestination,rider_id,rider_name,rider_phone, Amount, P_name, P_weight, R_phone, R_name, S_address, R_address, C_id, C_com;
    int amount;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payy);

        initializePaystack();
        initializeFormVariables();

        sessionManager = new SessionManager(Payy.this);
        sessionManager.checkLogin();

        c_name = findViewById(R.id.c_name);
        c_num = findViewById(R.id.c_num);
        r_name = findViewById(R.id.r_name);
        r_id = findViewById(R.id.r_id);
        location = findViewById(R.id.location);
        destination = findViewById(R.id.destination);
        money = findViewById(R.id.price);
        Btnback = findViewById(R.id.back);

        cid = findViewById(R.id.cid);
        ccom = findViewById(R.id.ccom);

        HashMap<String, String> user = sessionManager.getUserInfo();
        fid = user.get(sessionManager.ID);
        mPhone = user.get(sessionManager.PHONE);
        String mEMAIL = user.get(sessionManager.EMAIL);
        E_mail = mEMAIL;

        //final String id = String.valueOf(fid);
        final String fn = user.get(sessionManager.FIRSTNAME);
        final String ln = user.get(sessionManager.LASTNAME);
        //final String ph = String.valueOf(phh);

        //Random random = new Random();
        //int randtext = random.nextInt(10000);
        Reference = getIntent().getStringExtra("order_id");
        //System.out.println(Reference);
        na = fn + " " + ln;

        //rider_id = getIntent().getStringExtra("ID");

        //rider_name = getIntent().getStringExtra("rider_name");
        //rider_phone = getIntent().getStringExtra("rider_phone");

        P_name = getIntent().getStringExtra("package_name");
        P_weight = getIntent().getStringExtra("package_weight");
        R_name = getIntent().getStringExtra("receiver_name");
        R_phone = getIntent().getStringExtra("receiver_number");
        S_address = getIntent().getStringExtra("sender_address");
        R_address = getIntent().getStringExtra("receiver_address");
        C_id = getIntent().getStringExtra("created_id");
        C_com = getIntent().getStringExtra("created_company");

        Amount = getIntent().getStringExtra("amount");
        amount = Integer.parseInt(Amount);

        mLocation = getIntent().getStringExtra("location");
        mDestination = getIntent().getStringExtra("destination");
        //System.out.println(mDestination);
        //final String mEmail = getIntent().getStringExtra("Email");

        //final int KMM = Distance;
        rid = rider_id;
        AMM = Amount;
        //final String EMM = String.valueOf(mEMAIL);

        //phh.setText(mPhone);
//        c_name.setText(na);
//        c_num.setText(mPhone);
//        r_name.setText(rider_name);
//        r_id.setText(rid);
//        location.setText(mLocation);
//        destination.setText(mDestination);
//        money.setText(AMM);
//
//        cid.setText(C_id);
//        ccom.setText(C_com);
//        sessionManager = new SessionManager(this);
//        sessionManager.checkLogin();
//
//        HashMap<String, String> user = sessionManager.getUserInfo();

        Btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//
        //pass(fid,na,mPhone,rid,mLocation,mDestination,Reference,AMM,P_name,P_weight,R_name,R_phone,S_address,R_address,rider_name, rider_phone, C_id, C_com);


    }
    private void initializePaystack() {
        PaystackSdk.initialize(getApplicationContext());
        PaystackSdk.setPublicKey(PSTK_PUBLIC_KEY);
    }


    private void initializeFormVariables() {
        mCardNumber = findViewById(R.id.til_card_number);
        mCardExpiry = findViewById(R.id.til_card_expiry);
        mCardCVV = findViewById(R.id.til_card_cvv);

        //String Email = mEMAIL;

        // this is used to add a forward slash (/) between the cards expiry month
        // and year (11/21). After the month is entered, a forward slash is added
        // before the year

        Objects.requireNonNull(mCardExpiry).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 2 && !s.toString().contains("/")) {
                    s.append("/");
                }
            }
        });

        ProgressBar loading = findViewById(R.id.progressBar);
        Button button = findViewById(R.id.btn_make_payment);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                button.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);

                pass();
                //String email = user.get(sessionManager.EMAIL);


                //phh.setText("908c8ccc");

                //int cash = getIntent().getIntExtra("Amount", 0);

                String cardNumber = mCardNumber.getText().toString();
                String cardExpiry = mCardExpiry.getText().toString();
                String cvv = mCardCVV.getText().toString();

                String[] cardExpiryArray = cardExpiry.split("/");
                int expiryMonth = Integer.parseInt(cardExpiryArray[0]);
                int expiryYear = Integer.parseInt(cardExpiryArray[1]);
                //amount = getIntent().getStringExtra("amount");
                String mEmail = getIntent().getStringExtra("Email");
                amount *= 100;


                Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);

                Charge charge = new Charge();
                charge.setAmount(amount);
                charge.setEmail(E_mail);
                charge.setCard(card);

                PaystackSdk.chargeCard(Payy.this, charge, new Paystack.TransactionCallback() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        parseResponse(transaction.getReference());
                    }

                    private void parseResponse(String reference) {
                        String message = "Payment Successful - " + reference;
                        Toast.makeText(Payy.this, message , Toast.LENGTH_SHORT).show();
                        Intent Finish = new Intent(Payy.this, Payment_Successful.class);
                        //Intent trg = new Intent(Checkout.this,Track.class);
                        Finish.putExtra("reference", Reference);
                        //System.out.println(Reference);
                        //Finish.putExtra("Current_location", mLocation);
                        //Finish.putExtra("Destination", mDestination);
                        //Finish.putExtra("Amount", Amount);
                        //Finish.putExtra("Distance", Distance);
                        //startActivity(trg);
                        startActivity(Finish);
                    }

                    @Override
                    public void beforeValidate(Transaction transaction) {
                        Log.d("Main Activity", "beforeValidate: " + transaction.getReference());
                    }

                    @Override
                    public void onError(Throwable error, Transaction transaction) {
                        Toast.makeText(Payy.this, "Payment Unsuccessful", Toast.LENGTH_SHORT).show();
                        Log.d("Main Activity", "onError: " + error.getLocalizedMessage());
                        Log.d("Main Activity", "onError: " + error);
                    }

                });
            }
        });

    }

    private class verifyOnServer extends AsyncTask<String, Void, String> {
        private String reference;
        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Toast.makeText(Payy.this, String.format("Gateway response: %s", result), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Payy.this, String.format("There was a problem verifying %s on the backend: %s ", this.reference, error), Toast.LENGTH_LONG).show();
                //dismissDialog();
            }
        }

        @Override
        protected String doInBackground(String... reference) {
            try {
                this.reference = reference[0];
                String json = String.format("{\"reference\":\"%s\"}", this.reference);
                String url1 = "https://spotters.tech/dispatch-it/android/pay_live.php" + json;
                URL url = new URL(url1);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));

                String inputLine;
                inputLine = in.readLine();
                in.close();
                return inputLine;
            } catch (Exception e) {
                error = e.getClass().getSimpleName() + ": " + e.getMessage();
            }
            return null;
        }

        public void execute(String reference) {
        }
    }

    private void pass(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MOVE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> send = new HashMap<>();
                send.put("sender_id", fid);
                send.put("sender_name", na);
                send.put("sender_phone", mPhone);
                //send.put("rider_id", RID);
                send.put("sender_state", mLocation);
                send.put("receiver_state" , mDestination);
                send.put("sender_address", S_address);
                send.put("receiver_address" , R_address);
                send.put("order_id", Reference);
                send.put("amount", AMM);
                send.put("package_name", P_name);
                send.put("package_weight", P_weight);
                send.put("receiver_name", R_name);
                send.put("receiver_phone", R_phone);
                //send.put("rider_name", RNA);
                //send.put("rider_phone", RPH);
                send.put("created_by_id", C_id);
                send.put("created_by_company", C_com);
                send.put("status", "Pending");
                return send;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Payy.this);
        requestQueue.add(stringRequest);
    }
}