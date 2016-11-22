package com.passioncreativestudio.myandroidplayground;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION_CODE = 1;

    private AddressResultReceiver resultReceiver;

    private Button setLocation;

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private LatLng mapLatLng;
    private boolean addressRequested;
    private String addressOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setLocation = (Button) findViewById(R.id.activity_maps_setLocation);
        setLocation.setOnClickListener(this);

        checkPermission();

        buildGoogleApiClient();

        addressRequested = false;
        resultReceiver = new AddressResultReceiver(new Handler());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        checkPermission();
        map.setMyLocationEnabled(true);

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mapLatLng = map.getCameraPosition().target;
                makeShortToast(mapLatLng.toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermission();
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LatLng markerLocation;

        if (lastLocation != null) {
            markerLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 18));

            if(!Geocoder.isPresent()) {
                Toast.makeText(this, "No geocoder available.", Toast.LENGTH_LONG).show();
                return;
            }

            if(addressRequested) {
                startIntentService();
            }

        } else {
            // Add a marker in Singapore and move the camera
            markerLocation = new LatLng(1.33, 103.81);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 14));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_LOCATION_CODE) {
            if(grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.

                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.

                        startActivity(new Intent(this, MainActivity.class));
                        finish();

                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        makeShortToast(mapLatLng.toString());

        if(googleApiClient.isConnected() && lastLocation != null) {
            startIntentService();
        }

        addressRequested = true;
        updateWidgets();
    }

    private void updateWidgets() {

    }

    private void buildGoogleApiClient() {
        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void makeShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_CODE);
        }
    }

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, lastLocation);
        startService(intent);
    }

    private void displayAddressOutput() {

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            displayAddressOutput();

            if(resultCode == Constants.SUCCESS_RESULT) {
                showToast("address found: " + addressOutput);
            }
        }
    }
}
