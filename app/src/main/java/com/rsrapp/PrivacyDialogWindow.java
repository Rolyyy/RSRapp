package com.rsrapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class PrivacyDialogWindow extends AppCompatDialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));



        //MAYBE USE INFLATER AND setView LIKE IN OTHER DIALOG TO SET CUSTOM STYLE HERE....?
        builder.setTitle("Privacybeleid") //Title of the dialog
                .setMessage("Om deze app te gebruiken, dient u het privacybeleid te accepteren") //Message given by the dialog



                //A button which the user can click on to close the dialog
                .setPositiveButton("Bevestig", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        //returns the created dialog
        return builder.create();
    }

}