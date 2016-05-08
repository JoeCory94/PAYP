package com.joe.payp;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static String DeviceID;
    public static String IDCounter;
    String ParkingValue;
    String userValid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        Firebase.setAndroidContext(this);

        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto.ttf");

        Button btnStartParking = (Button) findViewById(R.id.btnStartParking);
        btnStartParking.setTypeface(typeface);

        Button btnRecentPayments = (Button) findViewById(R.id.btnRecentPayments);
        btnRecentPayments.setTypeface(typeface);

        TextView textTop = (TextView) findViewById(R.id.textTop);
        textTop.setTypeface(typeface);

        btnRecentPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (MainActivity.this, RecentParking.class);
                startActivity(i);
            }
        });



        btnStartParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();
            }
        });

    }

    private void checkUser(){

        final Firebase ref4 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/");
        Query queryRef = ref4.orderByKey();

        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                if(snapshot.getKey().toString().equals(DeviceID)){
                    System.out.println("YES");
                    userValid = "Valid";
                    getParkingValue();
                    getPaymentID();
                } else {
                    if(userValid != "Valid"){
                        userValid = "NotValid";
                        System.out.println("NO");
                    }
                }
                if(snapshot.getKey().toString().equals("ZZZZZZZZZZ")){
                    if(userValid.equals("NotValid")){
                        setUser();
                    }
                }

                System.out.println("LOOP 1");
                System.out.println(userValid);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    private void setUser(){
        System.out.println("SET USER");
        Firebase ref1 = new Firebase("https://glowing-torch-2458.firebaseio.com/");
        Firebase userRef = ref1.child("Accounts");
        Map<String, Object> userID = new HashMap<String, Object>();
        userID.put(DeviceID, "0");
        userRef.updateChildren(userID);

        Firebase ref2 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts");
        Firebase userRef2 = ref2.child(DeviceID);
        Map<String, Object> userID2 = new HashMap<String, Object>();
        userID2.put("Parked", "0");
        userRef2.updateChildren(userID2);

        Firebase ref3 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + DeviceID);
        Firebase userRef3 = ref3.child("Parked");
        Map<String, Object> userID3 = new HashMap<String, Object>();
        userID3.put("ParkingValue", "0");
        userRef3.updateChildren(userID3);

        Firebase ref = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/");
        Firebase userRef4 = ref.child(DeviceID);
        Map<String, Object> userID4 = new HashMap<String, Object>();
        userID4.put("Payments", "0");
        userRef4.updateChildren(userID4);

        Firebase ref5 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + DeviceID);
        Firebase userRef5 = ref5.child("Payments");
        Map<String, Object> userID5 = new HashMap<String, Object>();
        userID5.put("IDCounter", "0");
        userRef5.updateChildren(userID5);

        Firebase ref6 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + DeviceID + "/Payments");
        Firebase userRef6 = ref6.child("IDCounter");
        Map<String, Object> userID6 = new HashMap<String, Object>();
        userID6.put("IDValue", "0");
        userRef6.updateChildren(userID6);

        getParkingValue();
        getPaymentID();

    }

    public void getParkingValue(){
        final Firebase ref = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + DeviceID + "/Parked");
        Query queryRef = ref.orderByKey();

        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                ParkingValue = snapshot.getValue().toString();
                if(snapshot.getValue().toString().equals("0")){
                    Intent i = new Intent (MainActivity.this, StartParking.class);
                    startActivity(i);
                }
                if(snapshot.getValue().toString().equals("1")){
                    Intent i = new Intent (MainActivity.this, StopParking.class);
                    startActivity(i);
                }
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

}