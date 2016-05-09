package com.joe.payp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

/**
 * Created by Joe on 09/05/2016.
 */
public class PaymentSummary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        setContentView(R.layout.payment_summary);

        TextView lblPaymentID = (TextView)findViewById(R.id.PaymentID1);
        lblPaymentID.setText(RecentParking.PaymentIDClicked.toString());

        getPaymentDetails();

    }

    private void getPaymentDetails(){

        final Firebase ref4 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + MainActivity.DeviceID + "/Payments/" + RecentParking.PaymentIDClicked);
        Query queryRef = ref4.orderByKey();

        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                if(snapshot.getKey().toString().equals("Cost")){

                    TextView lblCost = (TextView)findViewById(R.id.lblCost);
                    lblCost.setText(snapshot.getValue().toString());

                }
                if(snapshot.getKey().toString().equals("Date")){

                    TextView lblDate = (TextView)findViewById(R.id.lblDate);
                    lblDate.setText(snapshot.getValue().toString());

                }
                if(snapshot.getKey().toString().equals("EndTime")){

                    TextView lblEndTime = (TextView)findViewById(R.id.lblEndTime);
                    lblEndTime.setText(snapshot.getValue().toString());

                }
                if(snapshot.getKey().toString().equals("StartTime")){

                    TextView lblStartTime = (TextView)findViewById(R.id.lblStartTime);
                    lblStartTime.setText(snapshot.getValue().toString());

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
}
