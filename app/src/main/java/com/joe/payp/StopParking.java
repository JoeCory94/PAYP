package com.joe.payp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe on 06/05/2016.
 */
public class StopParking extends AppCompatActivity {

    String IDCode;
    public String startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_parking_activity);

        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://glowing-torch-2458.firebaseio.com");



        Button btnStopParking = (Button) findViewById(R.id.btnStopParking);

        btnStopParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                LocalTime end = new LocalTime();
                //Log.e("TEST ", start.toString());

                Integer Time = Minutes.minutesBetween(LocalTime.parse(startTime), end).getMinutes();


                Firebase getRef = ref.child(IDCode);
                Map<String, Object> parking = new HashMap<>();
                parking.put("Parking", "0");
                getRef.updateChildren(parking);

                Map<String, Object> time = new HashMap<String, Object>();
                time.put("TotalTime", Time);
                getRef.updateChildren(time);

                Intent i = new Intent(StopParking.this, MainActivity.class);
                startActivity(i);

                System.out.println(IDCode);

*/

                Log.v("E_VALUE", startTime + " thing");
            }
        });
    }
}
