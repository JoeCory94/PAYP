package com.joe.payp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        Button btnStartParking = (Button) findViewById(R.id.btnParkingMode);

        btnStartParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (MainActivity.this, StartParking.class);
                startActivity(i);
            }
        });

        Button btnRecentParking = (Button) findViewById(R.id.btnParkingMode);

        btnRecentParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (MainActivity.this, RecentParking.class);
                startActivity(i);
            }
        });

        Button btnHelp = (Button) findViewById(R.id.btnAbout);

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (MainActivity.this, Help.class);
                startActivity(i);
            }
        });
    }
}