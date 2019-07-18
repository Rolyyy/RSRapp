package com.rsrapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


//This Class is used for the custom InfoWindow when device location is marked
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {


    private final View mWindow;
    private Context mContext;

    //Constructor
    public CustomInfoWindowAdapter(Context mContext) {

        this.mContext = mContext;

        //Set the custom xml infowindow to be used for layout
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_address_infowindow, null);
    }

    //This method sets the text to the view
    private void renderWindowText(Marker marker, View view){

        String snippet = marker.getSnippet();
        TextView tvSnippet = view.findViewById(R.id.info_window_address); //This TextView is where the device address is displayed


        if(!snippet.equals("")){
            tvSnippet.setText(snippet);
        }
    }


    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}
