package com.joe.payp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecentParking extends AppCompatActivity {

    //Firebase
    Firebase mRootRef;
    ArrayList<String> mMessages = new ArrayList<>();
    List<ListObject> payments = new ArrayList<>();

    //UI
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_parking);

        Firebase.setAndroidContext(this);

        mRootRef = new Firebase("https://glowing-torch-2458.firebaseio.com/");

        mListView = (ListView)findViewById(R.id.listView);

    }

    @Override
    protected void onStart() {
        super.onStart();


        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mMessages);

        mListView.setAdapter(adapter);

        Firebase messagesRef = mRootRef.child("Accounts/" + MainActivity.DeviceID + "/Payments");
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                System.out.println("Test 9");

                Map<String, String> map = dataSnapshot.getValue(Map.class);
                String PaymentID = "ID"/*map.get("PaymentID")*/;
                String Date = "Date"/*map.get("CityName")*/;
                String Cost = map.get("Cost");
                String StartTime = map.get("StartTime");
                String EndTime = map.get("EndTime");

                mMessages.add(PaymentID);

                payments.add(new ListObject(PaymentID, Date, Cost, StartTime, EndTime));

                LocationAdapter adapter =  new LocationAdapter(payments);

                mListView.setAdapter(adapter);


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

    private class LocationAdapter extends ArrayAdapter<ListObject> {

        public LocationAdapter(List<ListObject> items) {
            super(RecentParking.this, 0, items);
        }

        @Override
        public android.view.View getView(int position, android.view.View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.recent_payments_object, null);
            }

            TextView lblPaymentID = (TextView)convertView.findViewById(R.id.lblPaymentID);
            TextView lblDate = (TextView)convertView.findViewById(R.id.lblDate);
            TextView lblCost = (TextView)convertView.findViewById(R.id.lblCost);
            TextView lblStartTime = (TextView)convertView.findViewById(R.id.lblStartTime);
            TextView lblEndTime = (TextView)convertView.findViewById(R.id.lblEndTime);

            ListObject location = payments.get(position);

            lblPaymentID.setText(location.getPaymentID());
            lblDate.setText(location.getDate());
            lblCost.setText(location.getCost());
            lblStartTime.setText(location.getStartTime());
            lblEndTime.setText(location.getEndTime());

            return convertView;

        }// end get view

    }// end adapter class

    @Override
    protected void onPause() {
        super.onPause();

        mMessages.clear();
        payments.clear();

    }


}