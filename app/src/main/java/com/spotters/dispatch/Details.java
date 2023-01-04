package com.spotters.dispatch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Details extends AppCompatActivity {
    ImageView Btnback;
    TextView RN, PN, PW, SA, RA;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Btnback = findViewById(R.id.back);
        RN = findViewById(R.id.RN);
        PN = findViewById(R.id.PN);
        PW = findViewById(R.id.PW);
        SA = findViewById(R.id.SA);
        RA = findViewById(R.id.RA);




        Btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}