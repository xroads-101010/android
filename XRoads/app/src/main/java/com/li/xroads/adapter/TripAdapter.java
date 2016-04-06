package com.li.xroads.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.li.xroads.R;
import com.li.xroads.domain.Trip;

import java.util.ArrayList;

/**
 * Created by nagnath_p on 3/10/2016.
 */
public class TripAdapter extends ArrayAdapter<Trip> {


    public TripAdapter(Context context, ArrayList<Trip> trips) {
        super(context, 0, trips);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        Trip trip = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (rowView == null) {
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.item_trip, parent, false);
        }
        TextView tripName = (TextView) rowView.findViewById(R.id.tripName);
        TextView tripDestination = (TextView) rowView.findViewById(R.id.tripDestination);
        tripName.setText(trip.getName());
        tripDestination.setText(trip.getDestination());

        return rowView;
    }

    @Override
    public Trip getItem(int position) {
        return super.getItem(position);
    }
}
