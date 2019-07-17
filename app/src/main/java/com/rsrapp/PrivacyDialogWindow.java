package com.rsrapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import java.util.Objects;

public class PrivacyDialogWindow extends AppCompatDialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));


        // dialog_text contains "This is a http://test.org/"
        String msg = getResources().getString(R.string.privacy_policy);
        SpannableString spanMsg = new SpannableString(msg);
        Linkify.addLinks(spanMsg, Linkify.ALL);


        //MAYBE USE INFLATER AND setView LIKE IN OTHER DIALOG TO SET CUSTOM STYLE HERE....?
        builder.setTitle("Privacybeleid") //Title of the dialog
                .setMessage(spanMsg) //Message given by the dialog



                //A button which the user can click on to close the dialog
                .setPositiveButton("Bevestig", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        //returns the created dialog
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Make the dialog's TextView clickable
        ((TextView)this.getDialog().findViewById(R.id.terms_of_use))
                .setMovementMethod(LinkMovementMethod.getInstance());

    }

}