package com.spotters.dispatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity {
    TextView time, name;
    SessionManager sessionManager;
    String fn,ln,na;
    CardView DS,COMP,HI,TR;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        time = findViewById(R.id.prof);
        name = findViewById(R.id.name);

        DS = findViewById(R.id.dispatch);
        COMP = findViewById(R.id.complaint);
        HI = findViewById(R.id.history);
        TR = findViewById(R.id.track);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserInfo();
        fn = user.get(sessionManager.FIRSTNAME);
        ln = user.get(sessionManager.LASTNAME);

        na = fn + " " + ln;

        name.setText(na);


        time.setText(Time());

        //Implement CardView
        DS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dispatch = new Intent(Dashboard.this,Dispatch.class);
                startActivity(dispatch);
            }
        });

        COMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent complaint = new Intent(Dashboard.this,Complaint.class);
                startActivity(complaint);
            }
        });

        HI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent history = new Intent(Dashboard.this,History.class);
                startActivity(history);
            }
        });

        TR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent track = new Intent(Dashboard.this,Track.class);
                startActivity(track);
            }
        });
    }

    private String Time() {
        Calendar time = Calendar.getInstance();
        int to = time.get(Calendar.HOUR_OF_DAY);
        if( to < 12){
            return "Good Morning,";
        }else if( to < 16){
            return "Good Afternoon,";
        }else if( to < 20){
            return "Good Evening,";
        }else {
            return "Good Night,";
        }
    }
}