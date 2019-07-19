package com.rsrapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }
        checkInternetConnection();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Objects.requireNonNull(mapFragment).getMapAsync(this); //NullObjectReference can be caused here
    }



    //This method is called whenever the map is ready to be used
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //if statement checks the permissions of location services
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
           // mMap.setMyLocationEnabled(true); //This shows on map the current location dot, including orientation. Uncomment to enable it again
        }



        bottomButtonListener();
        backButtonListener();

    }

    //this method sets up an onClickListener for the button on the bottom of the screen
    //2 actions can be carried out depending on screen size
    public void bottomButtonListener(){

        final RelativeLayout callbutton = findViewById(R.id.callbutton);
        callbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(screenWidthCheck() > 600){
                    //If screen had a width greater than 600dp, it is determined to be a tablet in this case, no dialog is displayed.
                    //Instead, a window appears at the bottom of the screen with information on how to call RSR services

                    callbutton.setVisibility(View.GONE);
                    RelativeLayout bottom_tablet_text = findViewById(R.id.tablet_map_bottomt);
                    bottom_tablet_text.setVisibility(View.VISIBLE);
                }
                else{
                    callingDialog();
                }
            }
        });
    }

    //This method opens a dialog window which can be used to call RSR phone number
    public void callingDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_calling, null); //sets the custom layout for the dialog
        Button call_number = mView.findViewById(R.id.dialog_call_prompt);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.BOTTOM); //set the dialog to the bottom of the screen
        dialog.show();


        call_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pressing the button calls another method which carries out the calling prompt
                readyCallPrompt();
            }
        });

        //This is a custom button to close the dialog
        Button close_dialog = mView.findViewById(R.id.close_dialog_button);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    //This method sets up a button to go back to the previous activity (ActivityMain)
    public void backButtonListener(){

        ImageView back_button = findViewById(R.id.backbutton);
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(view.getContext(), MainActivity.class);
                startActivity(myintent);
            }
        });



    }

    //Checks the screen width of the device in dp
    public int screenWidthCheck() {

        Configuration configuration = MapsActivity.this.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;
        Log.d("Tag", "Device Width:" + screenWidthDp);
        return screenWidthDp;

    }


    //Checks if device has a working internet connection
    public void checkInternetConnection(){

         ConnectivityManager cm = (ConnectivityManager) (MapsActivity.this).getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
         if (activeNetwork != null) {
         Log.d("Tag", "connected to the internet");
         if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
         Log.d("Tag", "Connected to Wifi");
         } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
         Log.d("Tag", "connected to mobile data");
         }
         } else {
            //If there is no internet access, user is notified of this through a dialog window
             Log.d("Tag", "FAILED to Connect to Internet");
             AlertDialog.Builder internet_dialog = new AlertDialog.Builder(MapsActivity.this);
             internet_dialog.setMessage(R.string.no_internet_dialog_text)
                     .setTitle(R.string.no_internet_dialog_title)
             .setPositiveButton(R.string.no_internet_dialog_dismiss_button, null);
             internet_dialog.show();
         }

    }

    //Method carries out action to call RSR number
    public void readyCallPrompt(){

        Uri number = Uri.parse("tel:+319007788990");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);

    }

    //Method is used to asks for location permissions from user. Permissions must be given in order to proceed, and mark device location
    public boolean checkUserLocationPermission ()
    {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
           if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
           {
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
           }
           else
           {
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);

           }
           return false;
        }
        else
        {
            return true;
        }

    }

    //This method handles the permission request response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        switch (requestCode)
        {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    //User has denied Location Permissions for application, and it notified via Dialog window
                    AlertDialog.Builder location_dialog = new AlertDialog.Builder(MapsActivity.this);
                    location_dialog.setMessage(R.string.no_location_dialog_text)
                            .setTitle(R.string.no_location_dialog_title)
                            .setPositiveButton(R.string.no_internet_dialog_dismiss_button, null);
                    location_dialog.show();
                }

        }

    }

    protected synchronized void buildGoogleApiClient(){

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


            googleApiClient.connect();
        }

    //This method is called whenever there is a change in location
    //This is the method which Sets location marker, the camera view, and displays address to the user
    @Override
    public void onLocationChanged(Location location) {
        Log.d("LocationTag", "lastlocation= " + location);

        //If there is already a marker set, it is removed
        if(currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }

        //Get device location in terms of coordinates (latitude + longitude)
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("LocationTag", "latLng =" + latLng);

        //This animates the camera with movement and zooming towards device location
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        mMap.animateCamera(cameraUpdate);


        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this)); //Set the info window to the custom one created




        //Geocoder is used to convert latitude+longitude of type LatLng to a String based address
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = Objects.requireNonNull(addresses).get(0).getAddressLine(0); //info: Previously app has failed to get device location and address is set to null causing a crash
        Log.d("LocationTag", "address =" + address);


        final MarkerOptions markerOptions = new MarkerOptions();  //This is the marker which will show device location
       markerOptions.position(latLng); //Sets position of marker
       markerOptions.snippet(address); //shows the device address in the infoWindow
       markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)); //Sets the appearance of the location marker


        currentUserLocationMarker = mMap.addMarker(markerOptions); //Adds the marker to the map
        currentUserLocationMarker.showInfoWindow();     //Shows the infoWindow which displays device address
        //currentUserLocationMarker.setVisible(true);


        //After setting current location of user, this stops the location update
        if (googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }


    }


    //This method is called whenever the device is connected
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);  //Interval for updating device location
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //if statement checks the permissions of location services
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //FusedLocationApi gets the device location
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        }


    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
