package com.carsontrinh.cs160.represent;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class LegislatorActivity extends BaseActivity {

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
            subText.append("\n" + legislator.getStateFormatted());
            System.out.println("WOOOOOOOOOOW" + legislator.getStateFormatted());
        }

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(legislator.getColor())
                .borderWidthDp(10)
                .cornerRadiusDp(3)
                .oval(false)
                .build();
        Picasso.get().load(legislator.getImageURL()).placeholder(R.mipmap.ic_launcher).fit().transform(transformation).into(mediaImage);
    }


    public void phoneOnClick(View view) {
        String phoneNumber = legislator.getPhoneNumber();
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                "tel", phoneNumber, null));
        startActivity(phoneIntent);
    }

    public void contactOnClick(View view) {
        Uri uri = Uri.parse(legislator.getContactForm());
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);
    }

    public void webOnClick(View view) {
        Uri uri = Uri.parse(legislator.getUrl());
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);
    }
}
