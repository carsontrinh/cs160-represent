package com.carsontrinh.cs160.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class LegislatorActivity extends AppCompatActivity {

    LegislatorInfo legislator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legislator);

        Intent intent = getIntent();
        legislator = new LegislatorInfo(
                intent.getStringExtra(CongressionalActivity.MESSAGE_FIRST_NAME),
                intent.getStringExtra(CongressionalActivity.MESSAGE_LAST_NAME),
                intent.getStringExtra(CongressionalActivity.MESSAGE_REPRESENTATIVE_TYPE),
                intent.getStringExtra(CongressionalActivity.MESSAGE_PARTY),
                intent.getStringExtra(CongressionalActivity.MESSAGE_STATE),
                intent.getStringExtra(CongressionalActivity.MESSAGE_DISTRICT),
                intent.getStringExtra(CongressionalActivity.MESSAGE_FORMATTED_ADDRESS),
                intent.getStringExtra(CongressionalActivity.MESSAGE_URL),
                intent.getStringExtra(CongressionalActivity.MESSAGE_PHONE_NUMBER),
                intent.getStringExtra(CongressionalActivity.MESSAGE_CONTACT_FORM),
                intent.getStringExtra(CongressionalActivity.MESSAGE_ID));

        TextView subText = findViewById(R.id.sub_text);
        TextView primaryText = findViewById(R.id.primary_text);
        ImageView mediaImage = findViewById(R.id.media_image);

        subText.setText(legislator.getParty() + " " + legislator.getRepresentativeType());
        primaryText.setText(legislator.getFirstName() + " " + legislator.getLastName());
        if (legislator.getRepresentativeType().equalsIgnoreCase("representative")) {
            subText.append("\n" + legislator.getFormattedAddress());
        } else {
            subText.append("\n" + legislator.getState());
        }
        Picasso.get().load(legislator.getImageURL()).placeholder(R.mipmap.ic_launcher).fit().into(mediaImage);
    }
}
