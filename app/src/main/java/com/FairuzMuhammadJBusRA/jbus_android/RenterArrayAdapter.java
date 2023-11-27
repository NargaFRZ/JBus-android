package com.FairuzMuhammadJBusRA.jbus_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.FairuzMuhammadJBusRA.jbus_android.model.Bus;
import com.FairuzMuhammadJBusRA.jbus_android.model.Facility;

import java.util.List;
import java.util.Locale;

public class RenterArrayAdapter extends ArrayAdapter<Bus> {
    public RenterArrayAdapter(Context context, List<Bus> buses) {
        super(context, 0, buses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bus bus = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.managebus_view, parent, false);
        }

        TextView tvBusName = convertView.findViewById(R.id.busName);
        TextView tvDeparture = convertView.findViewById(R.id.departure);
        TextView tvArrival = convertView.findViewById(R.id.arrival);
        /*TextView tvBusType = convertView.findViewById(R.id.busType);
        TextView tvFacilities = convertView.findViewById(R.id.facilities);
        TextView tvCapacity = convertView.findViewById(R.id.capacity);
        TextView tvPrice = convertView.findViewById(R.id.price);
        String price = String.format(Locale.getDefault(), "%.2f", bus.price.price);

         */

        tvBusName.setText(bus.name);
        tvDeparture.setText(bus.departure.stationName.toString());
        tvArrival.setText(bus.arrival.stationName.toString());
        /*
        tvBusType.setText(bus.busType.toString());
        tvFacilities.setText(getFacilitiesString(bus.facilities));
        tvCapacity.setText(bus.capacity);
        tvPrice.setText(price);
         */

        return convertView;
    }

    private String capital(String original) {
        if (original == null || original.isEmpty()) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

    private String getFacilitiesString(List<Facility> facilities) {
        StringBuilder facilitiesString = new StringBuilder();
        for (Facility facility : facilities) {
            facilitiesString.append(facility.name()).append(", ");
        }
        if (facilitiesString.length() > 0) {
            facilitiesString.setLength(facilitiesString.length() - 2);
        }
        return facilitiesString.toString();
    }
}
