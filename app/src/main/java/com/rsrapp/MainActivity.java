package com.rsrapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

//This Activity runs after the initial SplashActivity
//Here the user can either open a dialog about privacy information, or tap a button to go to MapsActivity

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeActivity();
        privacyDialog();
    }
    //This method creates the privacy dialog. Within the dialog information is given, along with a hyperlink and dismiss button for the hyperlink
    private void privacyDialog() {


        RelativeLayout privacybutton = findViewById(R.id.privacy_button);

        privacybutton.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view) {

                //Creating an instance of AlertDialog
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_privacy, null);  //Custom layout used for the dialog
                mBuilder.setView(mView);
                TextView textView = mView.findViewById(R.id.terms_of_use);  //TextView where privacy text and hyperlink will be displayed

                //Create and dispaly the dialog
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                //Here the text is setup with the clickable hyperlink
                String text = getResources().getString(R.string.privacy_dialog_text); //This is the resource which contains information about the RSR privacy policy
                SpannableString ss = new SpannableString(text);
                ClickableSpan clickableSpan = new ClickableSpan() {@Override
                public void onClick(@NonNull View view) {
                    //The intent launches the given website when the hyperlink has been clicked
                    Intent url_link = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.rsr.nl/index.php?page=privacy-wetgeving"));
                    startActivity(url_link);
                }
                };

                //The ClickableSpan is used to create a hyperlink
                //Characters between 38-51 are set to be clickable
                ss.setSpan(clickableSpan, 38, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(ss);
                textView.setMovementMethod(LinkMovementMethod.getInstance());




                //Button with onClickListener which closes the Privacy Dialog:
                Button close_dialog = mView.findViewById(R.id.close_privacy_dialog);

                close_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }


    //This method allows the user to change screen to the Map Activity
    public void changeActivity() {

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
