package com.joe.payp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.Map;

public class StartParking extends AppCompatActivity {

    String ID = "IDC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        setContentView(R.layout.start_parking_activity);

        getPaymentID();

    }

    public void getPaymentID(){
        Firebase ref = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + MainActivity.DeviceID);
        Query queryRef = ref.orderByKey();
        queryRef.addChildEventListener(new ChildEventListener() {
            /*@Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                String s = snapshot.getValue().toString();
                System.out.println(s + " <----");
            }*/

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                /*String message = dataSnapshot.getValue(String.class);
                Log.v("E_VALUE", message);
                mMessages.add(message);
                adapter.notifyDataSetChanged();*/

                Map<String, String> map = dataSnapshot.getValue(Map.class);
                String message = map.get("ParkingSessionCounter").toString();
                Log.v("E_VALUE", message);

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


}
