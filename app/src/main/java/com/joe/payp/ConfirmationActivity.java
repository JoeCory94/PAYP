
package com.joe.payp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmationActivity extends AppCompatActivity {

    String PayPalID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        Firebase.setAndroidContext(this);

        //Getting Intent
        Intent intent = getIntent();


        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));

            storePaymentDetails();

            Toast.makeText(ConfirmationActivity.this, "Payment Complete.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent (ConfirmationActivity.this, MainActivity.class);
            startActivity(i);


        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent (ConfirmationActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        //Views
        TextView textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus= (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);

        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        PayPalID = jsonDetails.getString("id");
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(paymentAmount+" GBP");
    }

    private void storePaymentDetails(){

        Firebase ref3 = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + MainActivity.DeviceID + "/Payments/" + RecentParking.PaymentIDClicked);

        Map<String, Object> payment = new HashMap<String, Object>();
        payment.put("PayPalID", PayPalID);
        payment.put("Paid", "1");
        ref3.updateChildren(payment);


    }
}