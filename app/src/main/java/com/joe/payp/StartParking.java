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

public class StartParking extends AppCompatActivity {

    public static String IDCode = "ID1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_parking_activity);

        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://glowing-torch-2458.firebaseio.com/TestUser");

        Button btnStartParking = (Button) findViewById(R.id.btnStartParking);

        btnStartParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase getRef = ref.child(IDCode);
                Map<String, Object> nickname = new HashMap<String, Object>();
                nickname.put("Parking", "1");
                getRef.updateChildren(nickname);

                Intent i = new Intent(StartParking.this, StopParking.class);
                startActivity(i);
            }
        });
    }
}
