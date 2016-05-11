package com.joe.payp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;


/**
 * Created by Joe on 09/05/2016.
 */
public class PaymentSummary extends AppCompatActivity {

    String paymentAmount;
    public static final int PAYPAL_REQUEST_CODE = 123;

    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        setContentView(R.layout.payment_summary);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fa.ttf");
        TextView back = (TextView) findViewById(R.id.back);
        back.setTypeface(typeface);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentSummary.this, RecentParking.class);
                startActivity(i);
            }
        });

        Button buttonPay = (Button) findViewById(R.id.buttonPay);

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PaymentSummary.this, PayPalService.class);

                getPayment();

                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                startService(intent);

            }
        });

        TextView lblPaymentID = (TextView)findViewById(R.id.PaymentID);
        lblPaymentID.setText(RecentParking.PaymentIDClicked.toString());

        getPaymentDetails();



    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
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
                    paymentAmount = snapshot.getValue().toString();
                    TextView totalAmount = (TextView) findViewById(R.id.TotalPayment);
                    totalAmount.setText(paymentAmount);

                }
                if(snapshot.getKey().toString().equals("Date")){

                    TextView lblDate = (TextView)findViewById(R.id.Date);
                    lblDate.setText(snapshot.getValue().toString());

                }
                if(snapshot.getKey().toString().equals("EndTime")){

                    TextView lblEndTime = (TextView)findViewById(R.id.EndTime);
                    lblEndTime.setText(snapshot.getValue().toString());

                }
                if(snapshot.getKey().toString().equals("StartTime")){

                    TextView lblStartTime = (TextView)findViewById(R.id.StartTime);
                    lblStartTime.setText(snapshot.getValue().toString());

                }
                if(snapshot.getKey().toString().equals("Paid")){

                    if(snapshot.getValue().toString().equals("0")){
                        TextView Paid = (TextView)findViewById(R.id.Paid);
                        Paid.setText("No");
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

    private void getPayment() {
        //Getting the amount from editText

        //paymentAmount = RecentParking.PaymentIDClicked.toString();
        //paymentAmount = 6.6;

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "GBP", "Simplified Coding Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }



}
