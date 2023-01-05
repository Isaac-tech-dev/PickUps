package com.spotters.dispatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity {
    TextView time;
    SessionManager sessionManager;
    String fn,ln,na;
    CardView DS,COMP,HI,TR;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        time = findViewById(R.id.prof);

        DS = findViewById(R.id.dispatch);
        //COMP = findViewById(R.id.complaint);
        HI = findViewById(R.id.history);
        TR = findViewById(R.id.track);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserInfo();
        fn = user.get(sessionManager.FIRSTNAME);
        ln = user.get(sessionManager.LASTNAME);

        na = fn + " " + ln;



        time.setText(Time());

        //Implement CardView
        DS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dispatch = new Intent(Dashboard.this,Dispatch.class);
                startActivity(dispatch);
            }
        });

//        COMP.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent complaint = new Intent(Dashboard.this,Complaint.class);
//                startActivity(complaint);
//            }
//        });

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

        setUpToolbar();
        navigationView = (NavigationView) findViewById(R.id.side_navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        Intent tracking = new Intent(Dashboard.this, Profile.class);
                        startActivity(tracking);
                        return true;

                    case R.id.complaint:
                        Intent complaint = new Intent(Dashboard.this, Complaint.class);
                        startActivity(complaint);
                        return true;

                    case R.id.inbox:
                        Intent message = new Intent(Dashboard.this, Inbox.class);
                        startActivity(message);
                        return true;

                    case R.id.about:
                        Intent about = new Intent(Dashboard.this, About.class);
                        startActivity(about);
                        return true;

                    case R.id.logout:
                        Intent logout = new Intent(Dashboard.this, Login.class);
                        startActivity(logout);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    private void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private String Time() {
        Calendar time = Calendar.getInstance();
        int to = time.get(Calendar.HOUR_OF_DAY);
        if( to < 12){
            return "Good Morning," + " " + fn;
        }else if( to < 16){
            return "Good Afternoon," + " " + fn;
        }else if( to < 20){
            return "Good Evening," + " " + fn;
        }else {
            return "Good Night," + " " + fn;
        }
    }
}