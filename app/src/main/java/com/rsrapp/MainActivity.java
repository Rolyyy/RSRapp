package com.rsrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeActivityListener();
        privacyButtonListener();

    }

    private void privacyButtonListener() {

        RelativeLayout privacybutton = findViewById(R.id.privacy_button);

        //add button click listener, then:

        privacybutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PrivacyDialogWindow privacyDialogWindow = new PrivacyDialogWindow();
                privacyDialogWindow.show(getSupportFragmentManager(), "Privacy window");
            }
        });

    }


    public void changeActivityListener() {

        RelativeLayout bottombutton = findViewById(R.id.bottombutton);
        //An On Click Listener which will change the activity with the use of an intent
        bottombutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(view.getContext(), MapsActivity.class);
                startActivity(myintent);
            }
        });


    }
}
