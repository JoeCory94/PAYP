package com.joe.payp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Joe on 06/05/2016.
 */
public class MainActivity extends AppCompatActivity {

    public static String DeviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        TextView myTextView=(TextView)findViewById(R.id.Title);
        Typeface typeFace= Typeface.createFromAsset(getAssets(),"fonts/OpenSans.ttf");
        myTextView.setTypeface(typeFace);

        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Button btnStartParking = (Button) findViewById(R.id.btnStartParking);

        btnStartParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, StartParking.class);
                startActivity(i);
            }
        });

    }
}
