package com.joe.payp;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.HashMap;
import java.util.Map;

public class StopParking extends AppCompatActivity {

    String startTime;
    String endTime;
    Integer Time;
    Double Cost;
    public static String ParkingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.stop_parking_activity);

        Button stopParking = (Button) findViewById(R.id.btnStopParking);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto.ttf");
        stopParking.setTypeface(typeface);

        TextView logo = (TextView) findViewById(R.id.textTop);
        logo.setTypeface(typeface);

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

        getParkingValue();

        stopParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ParkingValue != null && !ParkingValue.isEmpty()) {
                    if(ParkingValue.equals("0")) {
                        Toast.makeText(StopParking.this, "You Are Not Currently Parked. Click Start Parking.",
                                Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(StopParking.this, StartParking.class);
                        startActivity(i);
                    }
                    if(ParkingValue.equals("1")) {
                        stopParking();
                        setEndTime();

                        Intent i = new Intent(StopParking.this, MainActivity.class);
                        startActivity(i);
                    }
                }else{
                    Toast.makeText(StopParking.this, "Currently Searching Database. Please Try Again",
                            Toast.LENGTH_SHORT).show();
                    System.out.println(ParkingValue);
                }
            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();
        //costCalculation();
    }

    public void getParkingValue(){
        final Firebase ref = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + MainActivity.DeviceID + "/Parked");
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

    private void stopParking(){
        Firebase ref2 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + MainActivity.DeviceID + "/Parked");

        Map<String, Object> parking = new HashMap<String, Object>();
        parking.put("ParkingValue", "0");
        ref2.updateChildren(parking);
    }

    private void setEndTime(){
        Firebase ref3 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + MainActivity.DeviceID + "/Payments/ID" + MainActivity.IDCounter);
        LocalTime endTimeVar = new LocalTime();

        String strEndTime = endTimeVar.toString();

        Map<String, Object> endTime = new HashMap<String, Object>();
        endTime.put("EndTime", strEndTime);
        ref3.updateChildren(endTime);

        getTimes();
    }

    private void storeID(){
        Firebase ref9 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + MainActivity.DeviceID + "/Payments/ID" + MainActivity.IDCounter);

        Map<String, Object> endTime = new HashMap<String, Object>();
        endTime.put("PaymentID", MainActivity.IDCounter);
        ref9.updateChildren(endTime);

    }

    private void incrementID(){
        Integer newID = Integer.parseInt(MainActivity.IDCounter) + 1;

        Firebase ref7 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + MainActivity.DeviceID + "/Payments/IDCounter");

        Map<String, Object> ID = new HashMap<String, Object>();
        ID.put("IDValue", String.valueOf(newID));
        ref7.updateChildren(ID);
    }

    private void getTimes(){

        final Firebase ref4 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + MainActivity.DeviceID + "/Payments/ID" + MainActivity.IDCounter);
        Query queryRef = ref4.orderByKey();

        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                if(snapshot.getKey().toString() == "StartTime"){
                    startTime = snapshot.getValue().toString();
                    System.out.println(startTime);
                }

                if(snapshot.getKey().toString() == "EndTime"){
                    endTime = snapshot.getValue().toString();
                    System.out.println(endTime);
                }

                if(startTime != null && !startTime.isEmpty()){
                    if(endTime != null && !endTime.isEmpty()){
                        storeID();
                        costCalculation();
                    }
                }
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

    private void costCalculation() {

        System.out.println(startTime + " and " + endTime);



        Time = Minutes.minutesBetween(LocalTime.parse(startTime), LocalTime.parse(endTime)).getMinutes();

        Cost = Time * 0.3;

        Firebase ref5 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + MainActivity.DeviceID + "/Payments/ID" + MainActivity.IDCounter);

        Map<String, Object> cost = new HashMap<String, Object>();
        cost.put("Cost", "£"+Cost+"0");
        ref5.updateChildren(cost);

        Map<String, Object> time = new HashMap<String, Object>();
        time.put("Time", Time);
        ref5.updateChildren(time);

        System.out.println(Cost + " " + Time);

        incrementID();
    }


}
