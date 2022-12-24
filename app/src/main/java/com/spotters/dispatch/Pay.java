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

public class Pay extends AppCompatActivity {
    private static final String PSTK_PUBLIC_KEY = "pk_test_f069de6d29089c973072403107bf7eb255f4e1a1";
    TextView c_name,c_num,r_name,r_id,location,destination,money;
    String fid, E_mail;
    private static String URL_MOVE ="https://spotters.tech/dispatch_app/android/dispatch_request.php";

    private TextInputLayout mCardNumber;
    private TextInputLayout mCardExpiry;
    private TextInputLayout mCardCVV;
    SessionManager sessionManager;
    String Reference, mLocation, mDestination,rider_id,rider_name, Amount, P_name, P_weight, R_phone, R_name;
    int amount;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        initializePaystack();
        initializeFormVariables();

        sessionManager = new SessionManager(Pay.this);
        sessionManager.checkLogin();

       c_name = findViewById(R.id.c_name);
       c_num = findViewById(R.id.c_num);
       r_name = findViewById(R.id.r_name);
       r_id = findViewById(R.id.r_id);
       location = findViewById(R.id.location);
       destination = findViewById(R.id.destination);
       money = findViewById(R.id.price);

        HashMap<String, String> user = sessionManager.getUserInfo();
        fid = user.get(sessionManager.ID);
        String mPhone = user.get(sessionManager.PHONE);
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
        final String na = fn + " " + ln;

        rider_id = getIntent().getStringExtra("ID");

        rider_name = getIntent().getStringExtra("rider_name");

        P_name = getIntent().getStringExtra("package_name");
        P_weight = getIntent().getStringExtra("package_weight");
        R_name = getIntent().getStringExtra("receiver_name");
        R_phone = getIntent().getStringExtra("receiver_number");

        Amount = getIntent().getStringExtra("amount");
        amount = Integer.parseInt(Amount);

        mLocation = getIntent().getStringExtra("location");
        mDestination = getIntent().getStringExtra("destination");
        //System.out.println(mDestination);
        //final String mEmail = getIntent().getStringExtra("Email");

        //final int KMM = Distance;
        String rid = rider_id;
        final String AMM = Amount;
        //final String EMM = String.valueOf(mEMAIL);

        //phh.setText(mPhone);
        c_name.setText(na);
        c_num.setText(mPhone);
        r_name.setText(rider_name);
        r_id.setText(rid);
        location.setText(mLocation);
        destination.setText(mDestination);
        money.setText(AMM);
//        sessionManager = new SessionManager(this);
//        sessionManager.checkLogin();
//
//        HashMap<String, String> user = sessionManager.getUserInfo();
//

        pass(fid,na,mPhone,rid,mLocation,mDestination,Reference,AMM,P_name,P_weight,R_name,R_phone);
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

        Objects.requireNonNull(mCardExpiry.getEditText()).addTextChangedListener(new TextWatcher() {
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
                //String email = user.get(sessionManager.EMAIL);


                //phh.setText("908c8ccc");

                //int cash = getIntent().getIntExtra("Amount", 0);

                String cardNumber = mCardNumber.getEditText().getText().toString();
                String cardExpiry = mCardExpiry.getEditText().getText().toString();
                String cvv = mCardCVV.getEditText().getText().toString();

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

                PaystackSdk.chargeCard(Pay.this, charge, new Paystack.TransactionCallback() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        parseResponse(transaction.getReference());
                    }

                    private void parseResponse(String reference) {
                        String message = "Payment Successful - " + reference;
                        Toast.makeText(Pay.this, message , Toast.LENGTH_SHORT).show();
                        Intent Finish = new Intent(Pay.this, Dashboard.class);
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
                        Toast.makeText(Pay.this, "Payment Unsuccessful", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Pay.this, String.format("Gateway response: %s", result), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Pay.this, String.format("There was a problem verifying %s on the backend: %s ", this.reference, error), Toast.LENGTH_LONG).show();
                //dismissDialog();
            }
        }

        @Override
        protected String doInBackground(String... reference) {
            try {
                this.reference = reference[0];
                String json = String.format("{\"reference\":\"%s\"}", this.reference);
                String url1 = "https://spotters.tech/dispatch_app/android/pay.php" + json;
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

    private void pass(String ID, String N, String P, String RID, String PL, String DE, String Ref, String cash, String PN,String PW, String RN, String RP){

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
                send.put("sender_id", ID);
                send.put("sender_name", N);
                send.put("sender_phone", P);
                send.put("rider_id", RID);
                send.put("sender_address", PL);
                send.put("receiver_address" , DE);
                send.put("order_id", Ref);
                send.put("amount", cash);
                send.put("package_name", PN);
                send.put("package_weight", PW);
                send.put("receiver_name", RN);
                send.put("receiver_phone", RP);
                send.put("status", "Pending");
                return send;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Pay.this);
        requestQueue.add(stringRequest);
    }
}