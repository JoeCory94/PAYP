package com.joe.payp;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

public class MainActivity extends AppCompatActivity {

    String DeviceID;
    String IDCounter;
    String ParkingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto.ttf");


        Button btnStartParking = (Button) findViewById(R.id.btnStartParking);
        btnStartParking.setTypeface(typeface);

        Button btnPaymentMode = (Button) findViewById(R.id.btnPaymentMode);
        btnPaymentMode.setTypeface(typeface);

        Button btnStopParking = (Button) findViewById(R.id.btnStopParking);
        btnStopParking.setTypeface(typeface);

        TextView textTop = (TextView) findViewById(R.id.textTop);
        textTop.setTypeface(typeface);


        btnStartParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (MainActivity.this, StartParking.class);
                startActivity(i);
            }
        });

        btnStopParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent (MainActivity.this, StopParking.class);
                startActivity(a);
            }
        });

    }

    public void getPaymentID(){
        final Firebase ref = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + DeviceID + "/Payments/IDCounter");
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

    public void getParkingValue(){
        final Firebase ref = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + DeviceID + "/Parked");
        Query queryRef = ref.orderByKey();

        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                ParkingValue = snapshot.getValue().toString();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ParkingValue = dataSnapshot.getValue().toString();
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

}