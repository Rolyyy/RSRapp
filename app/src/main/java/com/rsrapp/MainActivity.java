package com.rsrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
               // PrivacyDialogWindow privacyDialogWindow = new PrivacyDialogWindow();
             //   privacyDialogWindow.show(getSupportFragmentManager(), "Privacy window");



                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_privacy, null);

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();





               // Button callnumber = mView.findViewById(R.id.dialog_call_prompt);


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
