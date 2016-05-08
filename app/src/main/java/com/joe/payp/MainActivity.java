package com.joe.payp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe on 06/05/2016.
 */
public class MainActivity extends AppCompatActivity {

    public static String DeviceID;
    String userValid = "NotValid";
    Integer Counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Firebase.setAndroidContext(this);

        /*TextView myTextView=(TextView)findViewById(R.id.Title);
        Typeface typeFace= Typeface.createFromAsset(getAssets(),"fonts/Roboto.ttf");
        myTextView.setTypeface(typeFace);
*/
        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        checkUser();

        Button btnStartParking = (Button) findViewById(R.id.btnStartParking);

        btnStartParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUser();
                Intent i = new Intent(MainActivity.this, StartParking.class);
                startActivity(i);
            }
        });

    }

    private void checkUser(){

        final Firebase ref4 = new Firebase("https://glowing-torch-2458.firebaseio.com/");
        Query queryRef = ref4.orderByKey();

        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Long a = snapshot.child("Accounts").getChildrenCount();
                if(snapshot.getKey().toString().equals(DeviceID)){
                    System.out.println("YES");
                } else {
                    System.out.println("NO");
                }
                Counter ++;
                if(Counter == snapshot.getChildrenCount()){
                    if(userValid == "NotValid"){
                        setUser();
                    }
                }
                System.out.println(Counter);

                System.out.println(a);
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
}
