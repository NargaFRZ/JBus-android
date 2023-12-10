package com.FairuzMuhammadJBusRA.jbus_android;

import static java.lang.System.exit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.FairuzMuhammadJBusRA.jbus_android.model.Account;
import com.FairuzMuhammadJBusRA.jbus_android.model.BaseResponse;
import com.FairuzMuhammadJBusRA.jbus_android.model.Bus;
import com.FairuzMuhammadJBusRA.jbus_android.request.BaseApiService;
import com.FairuzMuhammadJBusRA.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button[] btns;
    private int currentPage = 0;
    private int pageSize = 10;
    private int listSize;
    private int noOfPages;
    private List<Bus> listBus = new ArrayList<>();
    private Button prevButton = null;
    private Button nextButton = null;
    private ListView busListView = null;
    private HorizontalScrollView pageScroll = null;
    private BaseApiService mApiService;
    private Context mContext;
    public static Account loggedAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("JBus");

        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);
        busListView = findViewById(R.id.listview);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        getTotalBus();

        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0? currentPage-1 : 0;
            goToPage(currentPage);
        });

        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages -1? currentPage+1 : currentPage;
            goToPage(currentPage);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.account){
            moveActivity(this, AboutMeActivity.class);
        }
        if(id == R.id.search){

        }
        if(id == R.id.payment){
            moveActivity(this, CheckPaymentActivity.class);
        }
        return (super.onOptionsItemSelected(item));
    }
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void paginationFooter(Integer listSize) {
        int val = listSize % pageSize;
        val = val == 0 ? 0 : 1;
        noOfPages = listSize / pageSize + val;
        LinearLayout ll = findViewById(R.id.btn_layout);
        ll.removeAllViews();
        btns = new Button[noOfPages];

        prevButton.setVisibility(noOfPages > 1 ? View.VISIBLE : View.GONE);
        nextButton.setVisibility(noOfPages > 1 ? View.VISIBLE : View.GONE);

        if (noOfPages <= 6) {
            ((FrameLayout.LayoutParams) ll.getLayoutParams()).gravity = Gravity.CENTER;
        }
        for (int i = 0; i < noOfPages; i++) {
            btns[i] = new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText("" + (i + 1));
            btns[i].setTextColor(getResources().getColor(R.color.black));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
            ll.addView(btns[i], lp);
            final int j = i;
            btns[j].setOnClickListener(v -> {
                currentPage = j;
                goToPage(j);
            });
        }
    }
    private void goToPage(int index) {
        for (int i = 0; i< noOfPages; i++) {
            if (i == index) {
                btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
                btns[i].setGravity(Gravity.CENTER);
                btns[i].setPadding(0, 0, 0, 0);
                scrollToItem(btns[index]);
                viewPaginatedList(currentPage);
            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
            }
        }
    }

    private void scrollToItem(Button item) {
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) / 2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }

    private void viewPaginatedList(int page) {
        mApiService.getBus(page, pageSize).enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                System.out.println(response.body());
                if(response.isSuccessful()) {
                    List<Bus> paginatedList = response.body();
                    ArrayAdapter<Bus> adapter = new BusArrayAdapter(mContext, paginatedList);
                    busListView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
            }
        });
    }

    protected void getTotalBus() {
        mApiService.numberOfBuses().enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    paginationFooter(response.body());
                    goToPage(currentPage);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
            }
        });
    }

    private class BusArrayAdapter extends ArrayAdapter<Bus> {
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
            tvDepartureCity.setText(capital(bus.departure.stationName.toString()));
            tvArrivalCity.setText(capital(bus.arrival.stationName.toString()));

            Button book = convertView.findViewById(R.id.book);
            book.setOnClickListener(v->{
                Intent i = new Intent(mContext, BookingActivity.class);
                i.putExtra("busId", bus.id);
                mContext.startActivity(i);
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