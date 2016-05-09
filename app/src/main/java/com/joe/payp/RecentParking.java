package com.joe.payp;


import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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

    Integer Counter = 0;

    public static Integer PaymentIDClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_parking);

        Firebase.setAndroidContext(this);

        mRootRef = new Firebase("https://glowing-torch-2458.firebaseio.com/");

        mListView = (ListView)findViewById(R.id.listView);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fa.ttf");
        TextView back = (TextView) findViewById(R.id.back);
        back.setTypeface(typeface);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecentParking.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mMessages);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3)
            {
                PaymentIDClicked = Integer.parseInt(mMessages.get(position));
                //Toast.makeText(getApplicationContext(), "Choice : "+selectedCity,   Toast.LENGTH_SHORT).show();

                Intent i = new Intent (RecentParking.this, PaymentSummary.class);
                startActivity(i);

            }
        });

        Firebase messagesRef = mRootRef.child("Accounts/" + MainActivity.DeviceID + "/Payments");
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map<String, String> map = dataSnapshot.getValue(Map.class);
                String Date = map.get("Date");
                String Cost = map.get("Cost");
                String StartTime = map.get("StartTime");
                String EndTime = map.get("EndTime");


                mMessages.add(Counter.toString());

                if(Counter != null) {

                    payments.add(new ListObject(Counter, Date, Cost, StartTime, EndTime));

                }

                LocationAdapter adapter =  new LocationAdapter(payments);

                mListView.setAdapter(adapter);

                Counter = Counter + 1;


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

            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto.ttf");

            TextView lblPaymentID = (TextView)convertView.findViewById(R.id.PaymentID);
            TextView lblDate = (TextView)convertView.findViewById(R.id.Date);
            TextView lblCost = (TextView)convertView.findViewById(R.id.Cost);
            TextView lblStartTime = (TextView)convertView.findViewById(R.id.StartTime);
            TextView lblEndTime = (TextView)convertView.findViewById(R.id.EndTime);

            ListObject location = payments.get(position);

            lblPaymentID.setText("#" + location.getPaymentID());
            lblDate.setText(location.getDate());
            lblCost.setText(location.getCost());
            lblStartTime.setText(location.getStartTime());
            lblEndTime.setText(location.getEndTime());

            lblPaymentID.setTypeface(typeface);
            lblDate.setTypeface(typeface);
            lblCost.setTypeface(typeface);
            lblStartTime.setTypeface(typeface);
            lblEndTime.setTypeface(typeface);

            return convertView;

        }// end get view

    }// end adapter class

    @Override
    protected void onPause() {
        super.onPause();

        mMessages.clear();
        payments.clear();
        Counter = 0;

    }


}