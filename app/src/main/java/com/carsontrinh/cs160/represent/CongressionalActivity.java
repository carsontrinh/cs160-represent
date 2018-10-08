package com.carsontrinh.cs160.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class CongressionalActivity extends BaseActivity implements RecyclerViewAdapter.ItemClickListener {

    public static final String MESSAGE_FIRST_NAME = "FIRST_NAME";
    public static final String MESSAGE_LAST_NAME = "LAST_NAME";
    public static final String MESSAGE_REPRESENTATIVE_TYPE = "REPRESENTATIVE_TYPE";
    public static final String MESSAGE_PARTY = "PARTY";
    public static final String MESSAGE_STATE = "STATE";
    public static final String MESSAGE_DISTRICT = "DISTRICT";
    public static final String MESSAGE_FORMATTED_ADDRESS = "FORMATTED_ADDRESS";
    public static final String MESSAGE_URL = "URL";
    public static final String MESSAGE_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String MESSAGE_CONTACT_FORM = "CONTACT_FORM";
    public static final String MESSAGE_ID = "ID";

    private final String API_KEY_GEOCODIO = "835eab88c6bb5cc7aed43f66aeebbb388bd25d8";

    RecyclerViewAdapter adapter;

    /** True if the provided input is a zipcode, and false if it is an address. */
    private String locationType;

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
                address = intent.getStringExtra(MainActivity.ZIPCODE_MESSAGE);
                break;
            case MainActivity.TYPE_CURRENT_LOCATION:
                address = intent.getStringExtra(MainActivity.COORDINATES_MESSAGE);
                System.out.println("ADDRESS:::: " + address);
                break;
            default:
                return;
        }

        fetchData(address);
    }

    /** Gets JSon stuff from Geocodia. */
    private void fetchData(String address) {

        // Geocodio stuff.
        StringBuilder query = new StringBuilder();
        query.append("https://api.geocod.io/v1.3/");

        switch (locationType) {
            case MainActivity.TYPE_ZIPCODE:
                query.append("geocode?");
                break;
            case MainActivity.TYPE_CURRENT_LOCATION:
                query.append("reverse?");
                break;
            default:
                return;
        }

        query.append("q="); query.append(address);  // address
        query.append("&fields="); query.append("cd");  // congressional district
        query.append("&api_key="); query.append(API_KEY_GEOCODIO);  // specifies API key

        System.out.println("QUERY:::: " + query.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, query.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                displayAPIDataToUser(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }


    private void displayAPIDataToUser(JSONObject response) {
        // data to populate the RecyclerView with
        HashSet<LegislatorInfo> legislatorSet = new HashSet<>();
        parse(response, legislatorSet);

        ArrayList<LegislatorInfo> legislators = new ArrayList<>(legislatorSet);
        Collections.sort(legislators, new Comparator<LegislatorInfo>() {
            @Override
            public int compare(LegislatorInfo o1, LegislatorInfo o2) {
                String o1Type = o1.getRepresentativeType();
                String o2Type = o2.getRepresentativeType();

                if (o1Type.equalsIgnoreCase("senator") &&
                    o2Type.compareTo(o1Type) < 1) {
                    return -1;
                } else if (o1Type.equalsIgnoreCase("representative") &&
                        o2Type.compareTo(o1Type) > 1) {
                    return 1;
                } else {
                    return o1.getLastName().compareTo(o2.getLastName()) >= 0 ? 1 : -1;
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_people);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, legislators);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void parse(JSONObject response, HashSet<LegislatorInfo> legislatorSet) {

        try {

            JSONArray jsonArrayResults = response.getJSONArray("results");

            for (int i = 0; i < jsonArrayResults.length(); i++) {

                JSONObject jsonArrayResult = jsonArrayResults.getJSONObject(i);

                JSONObject location = jsonArrayResult.getJSONObject("address_components");
                String state = location.getString("state");
                String formattedAddress = jsonArrayResult.getString("formatted_address");
System.out.println("STATE: " + state);
System.out.println("FORMATTEDADDRESS: " + formattedAddress);

                JSONObject fields = jsonArrayResult.getJSONObject("fields");
                JSONArray districts = fields.getJSONArray("congressional_districts");


                for (int j = 0; j < districts.length(); j++) {
                    JSONObject district = districts.getJSONObject(j);
                    String districtName = district.getString("name");

                    JSONArray legislators = district.getJSONArray("current_legislators");
                    for (int k = 0; k < legislators.length(); k++) {
                        JSONObject legislator = legislators.getJSONObject(k);
                        String type = legislator.getString("type");
System.out.println("TYPE: " + type);

                        JSONObject bio = legislator.getJSONObject("bio");
                        String lastName = bio.getString("last_name");
System.out.println("LASTNAME: " + lastName);
                        String firstName = bio.getString("first_name");
System.out.println("FIRSTNAME: " + firstName);
                        String party = bio.getString("party");
System.out.println("PARTY: " + party);

                        JSONObject contact = legislator.getJSONObject("contact");
                        String url = contact.getString("url");
System.out.println("URL: " + url);
                        String phone = contact.getString("phone");
System.out.println("PHONE: " + phone);
                        String contactForm = contact.getString("contact_form");
System.out.println("CONTACTFORM: " + contactForm);

                        JSONObject references = legislator.getJSONObject("references");
                        String id = references.getString("bioguide_id");
System.out.println("BIOGUIDEID: " + id);

                        LegislatorInfo aLegislator = new LegislatorInfo(
                                firstName, lastName, type, party,
                                state, districtName, formattedAddress,
                                url, phone, contactForm, id
                        );

                        legislatorSet.add(aLegislator);
                    }
                }

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(this, LegislatorActivity.class);
        LegislatorInfo legislator = adapter.getItem(position);
        intent.putExtra(MESSAGE_FIRST_NAME, legislator.getFirstName());
        intent.putExtra(MESSAGE_LAST_NAME, legislator.getLastName());
        intent.putExtra(MESSAGE_REPRESENTATIVE_TYPE, legislator.getRepresentativeType());
        intent.putExtra(MESSAGE_PARTY, legislator.getParty());
        intent.putExtra(MESSAGE_STATE, legislator.getState());
        intent.putExtra(MESSAGE_DISTRICT, legislator.getDistrict());
        intent.putExtra(MESSAGE_FORMATTED_ADDRESS, legislator.getFormattedAddress());
        intent.putExtra(MESSAGE_URL, legislator.getUrl());
        intent.putExtra(MESSAGE_PHONE_NUMBER, legislator.getPhoneNumber());
        intent.putExtra(MESSAGE_CONTACT_FORM, legislator.getContactForm());
        intent.putExtra(MESSAGE_ID, legislator.getId());
//        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

     /** Initializes a new Volley Request Queue. */
    private void initializeVolley() {
        queue = Volley.newRequestQueue(this);
    }
}
