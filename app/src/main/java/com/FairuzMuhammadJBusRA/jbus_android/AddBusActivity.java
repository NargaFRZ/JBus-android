package com.FairuzMuhammadJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import com.FairuzMuhammadJBusRA.jbus_android.model.*;
import com.FairuzMuhammadJBusRA.jbus_android.request.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBusActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private Context mContext;
    private EditText busName, busCapacity, busPrice;
    private BusType[] busType = BusType.values();
    private BusType selectedBusType;
    private Spinner busTypeDropdown;
    private List<Station> stationList = new ArrayList<>();
    private int selectedDepartureStation;
    private int selectedArrivalStation;
    private Spinner departureStationDropdown;
    private Spinner arrivalStationDropdown;
    private TableLayout facilitiesSection;
    private CheckBox[] facilitiesCheckBox;
    private List<Facility> selectedFacilities = new ArrayList<>();
    private Button addBusButton;

    private AdapterView.OnItemSelectedListener busTypeOISL, departureOISL, arrivalOISL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {this.getSupportActionBar().hide();}
        catch (NullPointerException e){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        mApiService = UtilsApi.getApiService();
        mContext = this;

        busName = findViewById(R.id.register_bus_name);
        busCapacity = findViewById(R.id.register_bus_capacity);
        busPrice = findViewById(R.id.register_bus_price);
        busTypeDropdown = findViewById(R.id.bus_type_dropdown);
        departureStationDropdown = findViewById(R.id.departure_station_dropdown);
        arrivalStationDropdown = findViewById(R.id.arrival_station_dropdown);
        facilitiesSection = findViewById(R.id.facilities_checkbox_section);
        addBusButton = findViewById(R.id.create_bus_button);

        ArrayAdapter adapterBusType = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, busType);
        adapterBusType.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        busTypeDropdown.setAdapter(adapterBusType);
        busTypeDropdown.setOnItemSelectedListener(setBusType());

        getStation();
        setFacilitiesCheckbox();

        addBusButton.setOnClickListener(x -> {
            handleAddBus();
        });
    }

    private void moveActivity (Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    private void viewToast (Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    protected AdapterView.OnItemSelectedListener setBusType(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBusType = busType[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    protected void getStation() {
        mApiService.getAllStation().enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if (response.isSuccessful()) {
                    stationList = response.body();
                    List<String> stationName = new ArrayList<>();
                    for (Station station : stationList) {
                        stationName.add(station.stationName);
                    }
                    ArrayAdapter<Station> adapter = new ArrayAdapter(AddBusActivity.this, android.R.layout.simple_spinner_item, stationName);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    departureStationDropdown.setAdapter(adapter);
                    arrivalStationDropdown.setAdapter(adapter);
                    departureStationDropdown.setOnItemSelectedListener(departureOISL);
                    arrivalStationDropdown.setOnItemSelectedListener(arrivalOISL);

                    departureOISL = new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedDepartureStation = stationList.get(i).id;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    };

                    arrivalOISL = new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedArrivalStation = stationList.get(i).id;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    };
                }
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {

            }
        });
    }

    protected void setFacilitiesCheckbox() {
        Facility[] facilityList = Facility.values();
        facilitiesCheckBox = new CheckBox[facilityList.length];

        for (int i = 0; i < facilityList.length; i++) {
            facilitiesCheckBox[i] = new CheckBox(mContext);
            facilitiesCheckBox[i].setText(facilityList[i].toString());
            facilitiesCheckBox[i].setTextColor(getResources().getColor(R.color.white));
            facilitiesCheckBox[i].setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            final int k = i;
            facilitiesCheckBox[i].setOnCheckedChangeListener((x, isChecked) -> {
                if (isChecked) {
                    selectedFacilities.add(facilityList[k]);
                } else {
                    selectedFacilities.remove(facilityList[k]);
                }
            });
        }

        int row = (facilityList.length % 3 == 0) ? facilityList.length / 3 : (facilityList.length / 3) + 1;
        TableRow[] rowList = new TableRow[row];
        int checkBoxIndex = 0;

        for (TableRow thisRow : rowList) {
            thisRow = new TableRow(mContext);
            thisRow.setGravity(Gravity.CENTER_VERTICAL);

            for (int i = 0; i < 3; i++) {
                if (checkBoxIndex < facilitiesCheckBox.length) {
                    CheckBox thisCheck = facilitiesCheckBox[checkBoxIndex++];
                    thisRow.addView(thisCheck);
                }
            }

            facilitiesSection.addView(thisRow);
        }
    }

    protected void handleAddBus(){
        String busNameValue = busName.getText().toString();
        String busCapacityValue = busCapacity.getText().toString();
        String busPriceValue = busPrice.getText().toString();

        if(busNameValue.isEmpty() || busCapacityValue.isEmpty() || busPriceValue.isEmpty()){
            viewToast(mContext, "Field Tidak Boleh Kosong");
            return;
        }

        if(!busCapacityValue.matches("\\d+")){
            viewToast(mContext, "Field Capacity harus berupa angka");
        }

        if(!busPriceValue.matches("\\d+")){
            viewToast(mContext, "Field Harga harus berupa angka");
        }

        int capacity = Integer.valueOf(busCapacityValue);
        int price = Integer.valueOf(busPriceValue);
        mApiService.create(MainActivity.loggedAccount.id, busNameValue, capacity, selectedFacilities, selectedBusType, price, selectedDepartureStation, selectedArrivalStation)
                .enqueue(new Callback<BaseResponse<Bus>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                        if(!response.isSuccessful()){
                            viewToast(mContext, "Application error " + response.code());
                            return;
                        }

                        BaseResponse<Bus> res = response.body();
                        if(res.success) {
                            finish();
                            moveActivity(mContext, ManageBusActivity.class);
                        }
                        viewToast(mContext, res.message);
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                        viewToast(mContext, "Ada problem pada server");
                    }
                });
    }
}