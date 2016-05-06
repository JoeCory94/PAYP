package com.joe.payp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private List<ListObject> cities;

    //UI
    TextView mTextView;
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
        cities = new ArrayList<>();
        Firebase messagesRef = mRootRef.child("Payments");
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                /*String message = dataSnapshot.getValue(String.class);
                Log.v("E_VALUE", message);
                mMessages.add(message);
                adapter.notifyDataSetChanged();*/

                Map<String, String> map = dataSnapshot.getValue(Map.class);
                String message = "The date was " + map.get("Date") + " and the cost was " + map.get("Cost");
                //Log.v("E_VALUE", message);
                mMessages.add(message);
                /*adapter.notifyDataSetChanged();*/



                cities.add(new ListObject(message,R.mipmap.ic_launcher));

                LocationAdapter adapter = new LocationAdapter(cities);
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedCity=mMessages.get(position).toString();
                Toast.makeText(getApplicationContext(), "Choice : "+selectedCity,   Toast.LENGTH_SHORT).show();

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
                        R.layout.parking_list_element, null);
            }

            ImageView imgCity = (ImageView)convertView.findViewById(R.id.imgCity);
            TextView lblCity = (TextView)convertView.findViewById(R.id.lblCity);

            ListObject location = cities.get(position);

            imgCity.setImageResource(location.getCityPicture());
            lblCity.setText(location.getCityName());

            return convertView;

        }// end get view

    }// end adapter class
}

