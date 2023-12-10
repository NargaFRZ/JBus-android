package com.FairuzMuhammadJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.FairuzMuhammadJBusRA.jbus_android.model.BaseResponse;
import com.FairuzMuhammadJBusRA.jbus_android.model.Bus;
import com.FairuzMuhammadJBusRA.jbus_android.model.Facility;
import com.FairuzMuhammadJBusRA.jbus_android.model.Payment;
import com.FairuzMuhammadJBusRA.jbus_android.model.Schedule;
import com.FairuzMuhammadJBusRA.jbus_android.request.BaseApiService;
import com.FairuzMuhammadJBusRA.jbus_android.request.UtilsApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {
    private int busID;
    private BaseApiService mApiService;
    private Context mContext;
    private TextView tvBusName, tvDepartureCity, tvArrivalCity, tvBusType, tvPrice, tvFacilities, seat;

    private Spinner departuredate;
    private Button bookseats, backbutton;
    private GridLayout seatsGridLayout;
    private List<String> selectedSeats = new ArrayList<>();
    private int renterId;
    private double dprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        getSupportActionBar().hide();
        mContext = this;
        mApiService = UtilsApi.getApiService();
        tvBusName  = findViewById(R.id.bus_name);
        tvDepartureCity = findViewById(R.id.departurestation);
        tvArrivalCity = findViewById(R.id.arrival_station);
        tvBusType = findViewById(R.id.bus_type);
        tvPrice = findViewById(R.id.price);
        tvFacilities = findViewById(R.id.facilities);
        departuredate = this.findViewById(R.id.departure_date);
        bookseats = findViewById(R.id.bookbutton);
        backbutton = findViewById(R.id.backbutton);
        seatsGridLayout = findViewById(R.id.seats_grid_layout);
        busID = this.getIntent().getIntExtra("busId", -1);
        busDetails();

        backbutton.setOnClickListener(v->{
            moveActivity(this, MainActivity.class);
        });

        bookseats.setOnClickListener(v->{
            makebooking();
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void busDetails(){
        mApiService.getBusbyId(busID).enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                if (!response.isSuccessful()) return;

                Bus bus = response.body();
                renterId = bus.accountId;
                tvBusName.setText(bus.name);
                tvDepartureCity.setText(bus.departure.stationName.toString());
                tvArrivalCity.setText(bus.arrival.stationName.toString());
                String price = String.format(Locale.getDefault(), "%.2f", bus.price.price);
                dprice = bus.price.price;
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                List<String> scheduleStrings = new ArrayList<>();
                for (Schedule schedule : bus.schedules) {
                    String formattedDate = dateFormat.format(schedule.departureSchedule);
                    scheduleStrings.add(formattedDate);
                }

                ArrayAdapter schedule = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, scheduleStrings);
                schedule.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                departuredate.setAdapter(schedule);
                departuredate.setOnItemSelectedListener(deptOISL);
            }

            @Override
            public void onFailure(Call<Bus> call, Throwable t) {

            }
        });
    }

    AdapterView.OnItemSelectedListener deptOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            seat = findViewById(R.id.seat);
            seat.setVisibility(View.VISIBLE);
            String selectedDate = (String) parent.getItemAtPosition(position);
            mApiService.getSeats(busID, selectedDate).enqueue(new Callback<Map<String, Boolean>>() {
                @Override
                public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        populateSeatsGrid(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {
                }
            });
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private void populateSeatsGrid(Map<String, Boolean> seats) {
        seatsGridLayout.removeAllViews();
        selectedSeats.clear();

        int index = 1;

        for (Map.Entry<String, Boolean> entry : seats.entrySet()) {
            CheckBox checkBox = new CheckBox(this);
            String seatLabel = "RA" + index++;
            checkBox.setText(seatLabel);
            checkBox.setChecked(false);

            if (!entry.getValue()) {
                checkBox.setEnabled(false);
                checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedSeats.add(entry.getKey());
                } else {
                    selectedSeats.remove(entry.getKey());
                }
            });

            seatsGridLayout.addView(checkBox);
        }

    }

    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
        finish();
    }

    private void makebooking(){
        int buyerId = MainActivity.loggedAccount.id;
        String selectedDate = (String) departuredate.getSelectedItem();

        if (selectedSeats.isEmpty()) {
            return;
        }

        mApiService.makeBooking(buyerId, renterId, busID, selectedSeats, selectedDate).enqueue(new Callback<BaseResponse<Payment>>() {
            @Override
            public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                if (!response.isSuccessful()) return;

                BaseResponse<Payment> res = response.body();
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                MainActivity.loggedAccount.balance -= (selectedSeats.size() * dprice);
                mContext.startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
            }
        });

    }
}