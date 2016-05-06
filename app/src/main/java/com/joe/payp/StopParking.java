package com.joe.payp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe on 06/05/2016.
 */
public class StopParking extends AppCompatActivity {

    String IDCode = StartParking.IDCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_parking_activity);

        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://glowing-torch-2458.firebaseio.com/TestUser/");

        Button btnStopParking = (Button) findViewById(R.id.btnStopParking);

        btnStopParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase getRef = ref.child(IDCode);
                Map<String, Object> nickname = new HashMap<String, Object>();
                nickname.put("Parking", "0");
                getRef.updateChildren(nickname);

                Intent i = new Intent(StopParking.this, MainActivity.class);
                startActivity(i);

                System.out.println(IDCode);
            }
        });
    }
}
