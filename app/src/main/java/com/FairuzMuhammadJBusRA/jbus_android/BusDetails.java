package com.FairuzMuhammadJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.FairuzMuhammadJBusRA.jbus_android.model.Bus;
import com.FairuzMuhammadJBusRA.jbus_android.model.Facility;
import com.FairuzMuhammadJBusRA.jbus_android.model.Schedule;
import com.FairuzMuhammadJBusRA.jbus_android.request.BaseApiService;
import com.FairuzMuhammadJBusRA.jbus_android.request.UtilsApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusDetails extends AppCompatActivity {
    private int busID;
    private BaseApiService mApiService;
    private Context mContext;
    private TextView tvBusName, tvDepartureCity, tvArrivalCity, tvBusType, tvPrice, tvFacilities, tvCapacity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);
        getSupportActionBar().hide();
        mContext = this;
        mApiService = UtilsApi.getApiService();
        tvBusName  = findViewById(R.id.bus_name);
        tvDepartureCity = findViewById(R.id.departurestation);
        tvArrivalCity = findViewById(R.id.arrival_station);
        tvBusType = findViewById(R.id.bus_type);
        tvPrice = findViewById(R.id.price);
        tvFacilities = findViewById(R.id.facilities);
        tvCapacity = findViewById(R.id.capacity);

        busID = this.getIntent().getIntExtra("busId", -1);

        busDetails();
    }

    private void busDetails(){
        mApiService.getBusbyId(busID).enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                if (!response.isSuccessful()) return;

                Bus bus = response.body();
                tvBusName.setText(bus.name);
                tvDepartureCity.setText(bus.departure.stationName.toString());
                tvArrivalCity.setText(bus.arrival.stationName.toString());
                String price = String.format(Locale.getDefault(), "%.2f", bus.price.price);
                tvBusType.setText(bus.busType.toString());
                tvPrice.setText("IDR " + price);
                StringBuilder facilitiesBuilder = new StringBuilder();
                for (Facility facility : bus.facilities) {
                    if (facilitiesBuilder.length() > 0) {
                        facilitiesBuilder.append(", ");
                    }
                    facilitiesBuilder.append(facility.toString());
                }
                tvFacilities.setText(facilitiesBuilder.toString());
                tvCapacity.setText(Integer.toString(bus.capacity));

                Button book = findViewById(R.id.booking);

                book.setOnClickListener(v->{
                    Intent i = new Intent(mContext, BookingActivity.class);
                    i.putExtra("busId", bus.id);
                    mContext.startActivity(i);
                });
            }

            @Override
            public void onFailure(Call<Bus> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}