package com.carsontrinh.cs160.represent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    public static final String LOCATION_TYPE_MESSAGE = "LOCATION_TYPE";
    public static final String ZIPCODE_MESSAGE = "ZIPCODE";
    public static final String COORDINATES_MESSAGE = "COORDINATES";

    public static final String TYPE_ZIPCODE = "type-zipcode";
    public static final String TYPE_CURRENT_LOCATION = "type-current-location";
    public static final String TYPE_RANDOM_LOCATION = "type-random";


     // Code credit:
     // Location services: Used some code from
     // https://github.com/googlesamples/android-play-location/tree/master/BasicLocatorSample/java

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


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
                System.out.println("<0>");
                if (!checkPermissions()) {
                    System.out.println("PERMISSIONS NOT ENABLED");
                    requestPermissions();
                } else {
                    System.out.println("PERMISSIONS ENABLED");
                    getLastLocation();
                    TextView textView = (TextView) findViewById(R.id.textView2);
                    textView.setText(getString(R.string.current_location, userLatitude, userLongitude));
                    if (userLatitude != null && userLongitude != null) {
                        submitIntent(TYPE_CURRENT_LOCATION, userLatitude.toString() + "," + userLongitude.toString());
                    }
                }
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
    private void submitIntent(String locationType, String input) {
        Intent intent = new Intent(this, CongressionalActivity.class);
        intent.putExtra(LOCATION_TYPE_MESSAGE, locationType);

        System.out.println("<2>" + locationType);
        switch (locationType) {
            case TYPE_ZIPCODE:
                intent.putExtra(ZIPCODE_MESSAGE, input);
                break;
            case TYPE_CURRENT_LOCATION:
                intent.putExtra(COORDINATES_MESSAGE, input);
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

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                            userLatitude = mLastLocation.getLatitude();
                            userLongitude = mLastLocation.getLongitude();

                            System.out.printf("Latitude: %f", userLatitude);
                            System.out.printf("Longitude: %f", userLongitude);
                        }
                    }
                });
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Log.i(TAG, "Requesting permission");
        // Request permission. It's possible this can be auto answered if device policy
        // sets the permission in a given state or the user denied the permission
        // previously and checked "Never ask again".
        startLocationPermissionRequest();
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

}
