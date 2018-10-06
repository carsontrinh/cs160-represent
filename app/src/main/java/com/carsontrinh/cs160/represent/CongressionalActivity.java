package com.carsontrinh.cs160.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CongressionalActivity extends AppCompatActivity {

    private final String API_KEY_GEOCODIO = "835eab88c6bb5cc7aed43f66aeebbb388bd25d8";

    /** True if the provided input is a zipcode, and false if it is an address. */
    private String locationType;
    private String zipcode;
    private String coordinates;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);

        initializeVolley();


        Intent intent = getIntent();
        locationType = intent.getStringExtra(MainActivity.LOCATION_TYPE_MESSAGE);

        String address;

        switch (locationType) {
            case MainActivity.TYPE_ZIPCODE:
                zipcode = intent.getStringExtra(MainActivity.ZIPCODE_MESSAGE);
                address = zipcode;
                break;
            case MainActivity.TYPE_CURRENT_LOCATION:
                coordinates = intent.getStringExtra(MainActivity.COORDINATES_MESSAGE);
                address = coordinatesToAddress(coordinates);
                break;
            default:
                return;
        }

        run(address);
    }

    /** Converts COORDINATES - a String "<latitude>,<longitude>" to an address using reverse-geocoding. */
    private String coordinatesToAddress(String coordinates) {
        // TODO
        return null;
    }

    private void run(String address) {
        final TextView volleyTextView = (TextView) findViewById(R.id.volleyText);

        // Geocodio stuff.
        StringBuilder query = new StringBuilder();
        query.append("https://api.geocod.io/v1.3/");
        query.append("geocode?");  // specifies if <geocode> or <reverse geocode> (for coords)
        query.append("q="); query.append(address);  // address
        query.append("&fields="); query.append("cd");  // congressional district
        query.append("&api_key="); query.append(API_KEY_GEOCODIO);  // specifies API key

        System.out.println("<100>: " + query.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, query.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

     /** Initializes a new Volley Request Queue. */
    private void initializeVolley() {
        queue = Volley.newRequestQueue(this);
    }
}
