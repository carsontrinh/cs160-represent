package com.carsontrinh.cs160.represent;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    /* Adapted from this example: https://stackoverflow.com/a/40584425/9911641 */

    private List<LegislatorInfo> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    // data is passed into the constructor
    RecyclerViewAdapter(Context context, List<LegislatorInfo> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LegislatorInfo legislator = mData.get(position);
        holder.subText.setText(legislator.getParty() + " " + legislator.getRepresentativeType());
        holder.primaryText.setText(legislator.getFirstName() + " " + legislator.getLastName());
        if (legislator.getRepresentativeType().equalsIgnoreCase("representative")) {
            holder.subText.append("\n" + legislator.getFormattedAddress());
        } else {
            holder.subText.append("\n" + legislator.getState());
        }

        String imageUrl = String.format("http://bioguide.congress.gov/bioguide/photo/%s/%s.jpg", legislator.getLastName().charAt(0), legislator.getId());
System.out.println("IMAGE URL:::: " + imageUrl);
        Picasso.get().load(imageUrl).placeholder(R.mipmap.ic_launcher).fit().into(holder.mediaImage);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView primaryText;
        TextView subText;
        ImageView mediaImage;

        ViewHolder(View itemView) {
            super(itemView);
            primaryText = itemView.findViewById(R.id.primary_text);
            subText = itemView.findViewById(R.id.sub_text);
            mediaImage = itemView.findViewById(R.id.media_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    LegislatorInfo getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

