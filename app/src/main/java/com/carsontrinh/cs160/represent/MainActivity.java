package com.carsontrinh.cs160.represent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public static final String LOCATION_TYPE_MESSAGE = "LOCATION_TYPE";
    public static final String ZIPCODE_MESSAGE = "ZIPCODE";
    public static final String COORDINATES_MESSAGE = "COORDINATES";

    public static final String TYPE_ZIPCODE = "type-zipcode";
    public static final String TYPE_CURRENT_LOCATION = "type-current-location";
    public static final String TYPE_RANDOM_LOCATION = "type-random";


     // Code credit:
     // Location services: Used some code from
     // https://www.youtube.com/watch?v=JlMxbTMutkA

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;


    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    private Double userLatitude;
    private Double userLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        findViewById(R.id.zipcode_submit_button).setEnabled(false);
        run();
    }

    private void run() {
        final EditText zipcodeEditText = (EditText) findViewById(R.id.zipcode_input);
        final Button zipcodeSubmitButton = (Button) findViewById(R.id.zipcode_submit_button);

        final Button currentLocationButton = (Button) findViewById(R.id.current_location_button);
        final Button randomLocationButton = (Button) findViewById(R.id.random_location_button);

        zipcodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isValidZipcode(s.toString().trim())) {
                    zipcodeSubmitButton.setEnabled(true);
                } else {
                    zipcodeSubmitButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        zipcodeSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Start intent to start congressional view
                submitIntent(TYPE_ZIPCODE, zipcodeEditText.getText().toString());
            }
        });

        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Start intent to start congressional view
                requestLocationUpdates();
            }
        });

        randomLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Start intent to start congressional view
            }
        });
    }

    /** Submits an intent to switch to {@link CongressionalActivity}. */
    private void submitIntent(String locationType, String address) {
        Intent intent = new Intent(this, CongressionalActivity.class);
        intent.putExtra(LOCATION_TYPE_MESSAGE, locationType);

        switch (locationType) {
            case TYPE_ZIPCODE:
                intent.putExtra(ZIPCODE_MESSAGE, address);
                break;
            case TYPE_CURRENT_LOCATION:
                System.out.println("CURRENT_LOCATION:::: " + address);
                intent.putExtra(COORDINATES_MESSAGE, address);
                break;
            default:
                return;
        }
        startActivity(intent);
    }

    /** Returns whether or not a string is a valid 5-digit zipcode input. */
    private boolean isValidZipcode(String zipcode) {
        if (zipcode == null || zipcode.length() != 5) {
            return false;
        }
        try {
            Integer.parseInt(zipcode);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED)) {


            fusedLocationProviderClient = new FusedLocationProviderClient(this);
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(2000);
            locationRequest.setInterval(4000);
            locationRequest.setNumUpdates(1);

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {

                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    userLongitude = locationResult.getLastLocation().getLongitude();
                    userLatitude = locationResult.getLastLocation().getLatitude();

                    TextView textView = (TextView) findViewById(R.id.textView2);
                    textView.setText(getString(R.string.current_location, userLatitude, userLongitude));
                    if (userLatitude != null && userLongitude != null) {
                        submitIntent(TYPE_CURRENT_LOCATION, userLatitude.toString() + "," + userLongitude.toString());
                    }
                }
            }, getMainLooper());

        } else {
            callPermissions();
        }
    }

    public void callPermissions() {
        Permissions.check(this/*context*/, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, "Location Access is required to use current location",
                new Permissions.Options().setSettingsDialogTitle("Warning").setRationaleDialogTitle("Info"),
                new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        requestLocationUpdates();
                    }
                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPerimissions){
                        super.onDenied(context, deniedPerimissions);
                        callPermissions();

                    }
                });
    }

}
