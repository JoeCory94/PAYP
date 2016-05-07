package com.joe.payp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import org.joda.time.LocalTime;

import java.util.HashMap;
import java.util.Map;

public class StartParking extends AppCompatActivity {

    String IDCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        setContentView(R.layout.start_parking_activity);

        getPaymentID();

        Button btnStartParking = (Button) findViewById(R.id.btnStartParking);

        btnStartParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IDCounter != null && !IDCounter.isEmpty()){
                    setStartTime();
                    setParked();

                    Intent i = new Intent(StartParking.this, StopParking.class);
                    startActivity(i);
                } else{
                    Toast.makeText(StartParking.this, "An Error Occurred. Please Try Again",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void getPaymentID(){
        final Firebase ref = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + MainActivity.DeviceID + "/Payments/IDCounter");
        Query queryRef = ref.orderByKey();

        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                IDCounter = snapshot.getValue().toString();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                IDCounter = dataSnapshot.getValue().toString();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

    }

    public void setParked(){
        Firebase ref2 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + MainActivity.DeviceID + "/Parked");

        Map<String, Object> parking = new HashMap<String, Object>();
        parking.put("ParkingValue", "1");
        ref2.updateChildren(parking);
    }

    public void setStartTime(){
        Firebase ref3 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + MainActivity.DeviceID + "/Payments/ID" + IDCounter);
        LocalTime startTimeVar = new LocalTime();

        String strStartTime = startTimeVar.toString();

        Map<String, Object> startTime = new HashMap<String, Object>();
        startTime.put("StartTime", strStartTime);
        ref3.updateChildren(startTime);
    }
}
