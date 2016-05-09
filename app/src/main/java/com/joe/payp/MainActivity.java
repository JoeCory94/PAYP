package com.joe.payp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static String DeviceID;
    public static Integer IDCounter;
    String ParkingValue;
    String userValid = "";
    public static String ParkingLocation = "Tap The PAYP NFC Tag.";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        Firebase.setAndroidContext(this);

        TextView parkingLocation = (TextView) findViewById(R.id.parkingLocation);
        parkingLocation.setText(ParkingLocation.toString());

        
        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        parkingQuery();

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto.ttf");


        Button btnStartParking = (Button) findViewById(R.id.btnStartParking);
        btnStartParking.setTypeface(typeface);

        Button btnRecentPayments = (Button) findViewById(R.id.btnRecentPayments);
        btnRecentPayments.setTypeface(typeface);

        TextView textTop = (TextView) findViewById(R.id.textTop);
        textTop.setTypeface(typeface);

        TextView parkingText = (TextView) findViewById(R.id.parkingText);
        parkingText.setTypeface(typeface);


        btnRecentPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (MainActivity.this, RecentParking.class);
                startActivity(i);
            }
        });



        btnStartParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();
            }
        });

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // create an intent with tag data and deliver to this activity
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { ndefIntent };
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };

    }

    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        //String s = action + "\n\n" + tag.toString();
        String s = "";

        // parse through all NDEF messages and their records and pick text type only
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage)data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            s = (
                                    new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
                                            textEncoding));

                            Toast.makeText(MainActivity.this, "Parking Location Stored.",
                                    Toast.LENGTH_SHORT).show();


                            //Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto.ttf");
                            //parkingLocation.setTypeface(typeface);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }
        }

        ParkingLocation = "Parked At: " + s.toString();

        System.out.println(ParkingLocation);

        TextView parkingLocation = (TextView) findViewById(R.id.parkingLocation);
        parkingLocation.setText(ParkingLocation.toString());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
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
                    getParkingValue();
                    getPaymentID();
                } else {
                    if(userValid != "Valid"){
                        userValid = "NotValid";
                        System.out.println("NO");
                    }
                }
                if(snapshot.getKey().toString().equals("ZZZZZZZZZZ")){
                    if(userValid.equals("NotValid")){
                        setUser();
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
        userID6.put("IDValue", 0);
        userRef6.updateChildren(userID6);

        getParkingValue();
        getPaymentID();

    }

    public void getParkingValue(){
        final Firebase ref = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + DeviceID + "/Parked");
        Query queryRef = ref.orderByKey();

        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                ParkingValue = snapshot.getValue().toString();
                if(snapshot.getValue().toString().equals("0")){
                    Intent i = new Intent (MainActivity.this, StartParking.class);
                    startActivity(i);
                }
                if(snapshot.getValue().toString().equals("1")){
                    Intent i = new Intent (MainActivity.this, StopParking.class);
                    startActivity(i);
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

    public void parkingQuery(){
        final Firebase ref = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + DeviceID + "/Parked");
        Query queryRef = ref.orderByKey();

        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                ParkingValue = snapshot.getValue().toString();
                if(snapshot.getValue().toString().equals("0")){
                    TextView parkingText = (TextView) findViewById(R.id.parkingText);
                    parkingText.setText("You Are Not Currently Parking.");
                }
                if(snapshot.getValue().toString().equals("1")){
                    TextView parkingText = (TextView) findViewById(R.id.parkingText);
                    parkingText.setText("You Are Currently Parking.");
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

    public void getPaymentID(){
        final Firebase ref = new Firebase("https://glowing-torch-2458.firebaseio.com/Accounts/" + DeviceID + "/Payments/IDCounter");
        Query queryRef = ref.orderByKey();

        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                IDCounter = Integer.parseInt(snapshot.getValue().toString());
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