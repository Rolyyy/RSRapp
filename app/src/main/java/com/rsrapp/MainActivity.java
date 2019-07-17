package com.rsrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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



        privacybutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               // PrivacyDialogWindow privacyDialogWindow = new PrivacyDialogWindow();
             //   privacyDialogWindow.show(getSupportFragmentManager(), "Privacy window");



                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_privacy, null);

                TextView textView = mView.findViewById(R.id.terms_of_use);



                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create(); //had to make this final due to dialog.dismiss() ... maybe will be changed when Im cleaning the code up???
                dialog.show();


                String text = "Om deze app te gebruiken, dient u het privacybeleid te accepteren";
                SpannableString ss = new SpannableString(text);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        Intent Getintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.rsr.nl/index.php?page=privacy-wetgeving"));
                        startActivity(Getintent);
                    }
                };
                ss.setSpan(clickableSpan, 38, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(ss);
                textView.setMovementMethod(LinkMovementMethod.getInstance());


                Button closedialog = mView.findViewById(R.id.close_privacy_dialog);

                closedialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });



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
