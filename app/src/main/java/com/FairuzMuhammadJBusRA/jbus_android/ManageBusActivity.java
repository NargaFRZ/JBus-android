package com.FairuzMuhammadJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.FairuzMuhammadJBusRA.jbus_android.model.Account;
import com.FairuzMuhammadJBusRA.jbus_android.model.BaseResponse;
import com.FairuzMuhammadJBusRA.jbus_android.model.Bus;
import com.FairuzMuhammadJBusRA.jbus_android.request.BaseApiService;
import com.FairuzMuhammadJBusRA.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBusActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private RenterArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        adapter = new RenterArrayAdapter(this, new ArrayList<>());

        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);

        getBus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manage_bus_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addBus) {
            moveActivity(this, AddBusActivity.class);
        }
        return (super.onOptionsItemSelected(item));
    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
        finish();
    }

    protected void getBus() {
        int userId = MainActivity.loggedAccount.id;
        mApiService.getMyBus(userId).enqueue(new Callback<List<Bus>>() {
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Bus> myBuses = response.body();
                adapter.clear();
                adapter.addAll(myBuses);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "No Bus Found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveActivity(this, AboutMeActivity.class);
    }

    private class RenterArrayAdapter extends ArrayAdapter<Bus> {
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
            TextView tvPrice = convertView.findViewById(R.id.price);
            TextView tvCapacity = convertView.findViewById(R.id.capacity);

            tvBusName.setText(bus.name);
            tvDeparture.setText(capital(bus.departure.stationName.toString()));
            tvArrival.setText(capital(bus.arrival.stationName.toString()));
            String price = String.format(Locale.getDefault(), "%.2f", bus.price.price);
            tvPrice.setText("IDR " + price);
            tvCapacity.setText(Integer.toString(bus.capacity));

            ImageView schedule = convertView.findViewById(R.id.calendar);
            schedule.setOnClickListener(v->{
                Intent i = new Intent(mContext, ManageBusSchedule.class);
                i.putExtra("busId", bus.id);
                mContext.startActivity(i);
                finish();
            });

            convertView.setOnClickListener(v->{
                Intent i = new Intent(mContext, BusDetails.class);
                i.putExtra("busId", bus.id);
                mContext.startActivity(i);
                finish();
            });

            return convertView;
        }

        private String capital(String original) {
            if (original == null || original.isEmpty()) {
                return original;
            }
            String[] words = original.split(" ");
            StringBuilder capitalizedString = new StringBuilder();

            for (String word : words) {
                String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                capitalizedString.append(capitalizedWord).append(" ");
            }

            return capitalizedString.toString().trim();
        }
    }
}