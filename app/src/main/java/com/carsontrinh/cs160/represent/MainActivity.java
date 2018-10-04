package com.carsontrinh.cs160.represent;

import android.location.Location;
import android.support.annotation.NonNull;
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

     // Code credit:
     // Location services: Used some code from
     // https://github.com/googlesamples/android-play-location/tree/master/BasicLocatorSample/java

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    private String mLatitudeLabel;
    private String mLongitudeLabel;

    private Double userLatitude;
    private Double userLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        findViewById(R.id.zipcode_submit_button).setEnabled(false);

        run();
    }

    private void run() {
        final EditText zipcodeEditText = (EditText) findViewById(R.id.zipcode_input);
        final Button zipcodeSubmitButton = (Button) findViewById(R.id.zipcode_submit_button);

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
            }
        });
    }

    /** Returns whether or not a string is a valid 5-digit zipcode input. */
    private boolean isValidZipcode(String zipcode) {
        if (zipcode == null || zipcode.length() != 5) {
            return false;
        }
        try {
            int parsedZipcode = Integer.parseInt(zipcode);
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

}
