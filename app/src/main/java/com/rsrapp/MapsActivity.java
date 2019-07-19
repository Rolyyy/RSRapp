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
import android.widget.Toast;
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

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
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

        mapFragment.getMapAsync(this); //NULL OBJECT REFERENCE BEING CAUSED HERE???
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
           // mMap.setMyLocationEnabled(true); //This shows on map the current location dot, including orientation. Uncomment to enable it again
        }



        RelativeLayout callbutton = findViewById(R.id.callbutton);

        callbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {



                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_calling, null);
                Button callnumber = mView.findViewById(R.id.dialog_call_prompt);



                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create(); //had to make this final due to dialog.dismiss() ... maybe will be changed when Im cleaning the code up???
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.show();

                callnumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        readyCallPrompt();
                    }
                });

                Button closedialog = mView.findViewById(R.id.close_dialog_button);

                closedialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        ImageView backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(view.getContext(), MainActivity.class);
                startActivity(myintent);
            }
        });


    }

    public void checkInternetConnection(){

         ConnectivityManager cm = (ConnectivityManager) (MapsActivity.this).getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
         if (activeNetwork != null) {
         // connected to the internet
         if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
         // connected to wifi
         } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
         // connected to mobile data
         }
         } else {
             // not connected to the internet

             AlertDialog.Builder internet_dialog = new AlertDialog.Builder(MapsActivity.this);
             internet_dialog.setMessage(R.string.no_internet_dialog_text)
                     .setTitle(R.string.no_internet_dialog_title)
             .setPositiveButton(R.string.no_internet_dialog_dismiss_button, null);
             internet_dialog.show();


         }

    }

    public void readyCallPrompt(){

        //Toast.makeText(this, "Calling number...", Toast.LENGTH_LONG).show();
        Uri number = Uri.parse("tel:+319007788990");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);

    }

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
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show();
                }
                return;
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






    @Override
    public void onLocationChanged(Location location) {

        lastLocation = location;
        Log.d("locationtag", "lastlocation= " + lastLocation);

        if(currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        Log.d("locationtag", "latLng =" + latLng);

        //This animates the camera with movement and zooming towards device location
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        mMap.animateCamera(cameraUpdate);


        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));





        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);
        Log.d("locationtag", "address =" + address);

        /**
         * String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String zip = addresses.get(0).getPostalCode();
        String country = addresses.get(0).getCountryName();
        */


        final MarkerOptions markerOptions = new MarkerOptions();
       markerOptions.position(latLng);
       markerOptions.snippet(address);
       markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));


        currentUserLocationMarker = mMap.addMarker(markerOptions);
        currentUserLocationMarker.showInfoWindow();


        if (googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }


    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
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
