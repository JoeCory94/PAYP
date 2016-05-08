package com.joe.payp;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Map;

public class StartParking extends AppCompatActivity {

    public static String IDCounter;
    public static String DeviceID;
    String userValid = "";
    public static String ParkingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Firebase.setAndroidContext(this);
        setContentView(R.layout.start_parking_activity);

        checkUser();

        Button btnStartParking = (Button) findViewById(R.id.btnStartParking);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto.ttf");

        btnStartParking.setTypeface(typeface);

        TextView logo = (TextView) findViewById(R.id.textTop);
        logo.setTypeface(typeface);

        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/fa.ttf");
        TextView back = (TextView) findViewById(R.id.back);
        back.setTypeface(typeface1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartParking.this, MainActivity.class);
                startActivity(i);
            }
        });

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            TextView text3 = (TextView) findViewById(R.id.textLeft);
            text3.setTypeface(typeface);

            TextView text4 = (TextView) findViewById(R.id.textRight);
            text4.setTypeface(typeface);
        }


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            TextView text1 = (TextView) findViewById(R.id.textMiddle);
            text1.setTypeface(typeface);

            TextView text2 = (TextView) findViewById(R.id.textBottom);
            text2.setTypeface(typeface);
        }



        btnStartParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IDCounter != null && !IDCounter.isEmpty()){
                    if(ParkingValue != null && !ParkingValue.isEmpty()) {
                        if(ParkingValue.equals("0")) {
                            setStartTime();
                            setDate();
                            setParked();

                            Intent i = new Intent(StartParking.this, StopParking.class);
                            startActivity(i);

                            System.out.println(ParkingValue);
                        }
                        if(ParkingValue.equals("1")) {
                            Toast.makeText(StartParking.this, "You Are Already Parking.",
                                    Toast.LENGTH_SHORT).show();

                            Intent a = new Intent(StartParking.this, StopParking.class);
                            startActivity(a);

                            System.out.println(ParkingValue);
                        }
                    }else{
                        Toast.makeText(StartParking.this, "Currently Setting Up Database. Please Try Again",
                                Toast.LENGTH_SHORT).show();
                        System.out.println(ParkingValue);
                    }
                } else{
                    Toast.makeText(StartParking.this, "Currently Setting Up Database. Please Try Again",
                            Toast.LENGTH_SHORT).show();
                    System.out.println(ParkingValue);
                }

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
                    getPaymentID();
                    getParkingValue();
                } else {
                    if(userValid != "Valid"){
                        userValid = "NotValid";
                        System.out.println("NO");
                    }
                }
                if(snapshot.getKey().toString().equals("ZZZZZZZZZZ")){
                    if(userValid.equals("NotValid")){
                        setUser();
                        getPaymentID();
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

        System.out.println("End");

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

    public void setParked(){
        Firebase ref2 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + DeviceID + "/Parked");

        Map<String, Object> parking = new HashMap<String, Object>();
        parking.put("ParkingValue", "1");
        ref2.updateChildren(parking);
    }

    public void setStartTime(){
        Firebase ref3 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + DeviceID + "/Payments/ID" + IDCounter);
        LocalTime startTimeVar = new LocalTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");

        String time = fmt.print(startTimeVar);


        Map<String, Object> startTime = new HashMap<String, Object>();
        startTime.put("StartTime", time);
        ref3.updateChildren(startTime);
    }

    public void setDate(){
        Firebase ref6 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + DeviceID + "/Payments/ID" + IDCounter);
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yy");

        String date = fmt.print(dt);


        Map<String, Object> startTime = new HashMap<String, Object>();
        startTime.put("Date", date);
        ref6.updateChildren(startTime);
    }
}
