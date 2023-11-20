package com.FairuzMuhammadJBusRA.jbus_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.FairuzMuhammadJBusRA.jbus_android.model.Bus;

import java.util.List;

public class BusArrayAdapter extends ArrayAdapter<Bus> {
    public BusArrayAdapter(Context context, List<Bus> buses) {
        super(context, 0, buses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bus bus = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view, parent, false);
        }

        TextView tvBusName = convertView.findViewById(R.id.busName);
        TextView tvDepartureCity = convertView.findViewById(R.id.departureCity);
        TextView tvArrivalCity = convertView.findViewById(R.id.arrivalCity);

        tvBusName.setText(bus.name);
        tvDepartureCity.setText(capital(bus.departure.city.toString()));
        tvArrivalCity.setText(capital(bus.arrival.city.toString()));

        return convertView;
    }

    private String capital(String original) {
        if (original == null || original.isEmpty()) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }
}
