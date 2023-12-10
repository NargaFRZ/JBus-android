package com.FairuzMuhammadJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.FairuzMuhammadJBusRA.jbus_android.model.BaseResponse;
import com.FairuzMuhammadJBusRA.jbus_android.model.Bus;
import com.FairuzMuhammadJBusRA.jbus_android.model.Invoice;
import com.FairuzMuhammadJBusRA.jbus_android.model.Payment;
import com.FairuzMuhammadJBusRA.jbus_android.request.BaseApiService;
import com.FairuzMuhammadJBusRA.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RenterPaymentActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private CombinedAdapter combinedAdapter;
    private List<PaymentAndBus> combinedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter_payment);
        getSupportActionBar().hide();

        mApiService = UtilsApi.getApiService();
        mContext = this;

        combinedAdapter = new CombinedAdapter(this, combinedList);
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(combinedAdapter);

        getPayment();
    }

    private static class BusPlaceholder {
        int busId;

        public BusPlaceholder(int busId) {
            this.busId = busId;
        }
    }
    protected void getPayment() {
        int renterId = MainActivity.loggedAccount.id;

        mApiService.getRenterPayment(renterId).enqueue(new Callback<List<Payment>>() {
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Payment> payments = response.body();
                    combinedList.clear();
                    for (Payment payment : payments) {
                        getBus(payment, payment.busId);
                    }
                } else {
                    Toast.makeText(mContext, "Error fetching payments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                Toast.makeText(mContext, "Error fetching payments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBus(final Payment payment, int busId) {
        mApiService.getBusbyId(busId).enqueue(new Callback<Bus>() {
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bus bus = response.body();
                    combinedList.add(new PaymentAndBus(payment, bus));
                    combinedAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Error fetching buses", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Bus> call, Throwable t) {
                Toast.makeText(mContext, "Error fetching buses", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
        finish();
    }

    private void cancelPayment(int id){
        mApiService.cancel(id).enqueue(new Callback<BaseResponse<Payment>>(){
            public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response){
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(mContext, "Successfully cancelled Payment", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Error cancelling payment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                Toast.makeText(mContext, "Error cancelling payment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void acceptPayment(int id){
        mApiService.accept(id).enqueue(new Callback<BaseResponse<Payment>>(){
            public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response){
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(mContext, "Successfully accepted Payment", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Error cancelling payment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                Toast.makeText(mContext, "Error cancelling payment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveActivity(this, AboutMeActivity.class);
    }
    private class CombinedAdapter extends ArrayAdapter<PaymentAndBus> {
        private static final int TYPE_BUS = 0;
        private static final int TYPE_PAYMENT = 1;

        public CombinedAdapter(Context context, List<PaymentAndBus> objects) {
            super(context, 0, objects);
        }

        @Override
        public int getItemViewType(int position) {
            Object item = getItem(position);
            if (item instanceof Bus) {
                return TYPE_BUS;
            } else if (item instanceof Payment) {
                return TYPE_PAYMENT;
            } else if (item instanceof BusPlaceholder) {
                return TYPE_BUS;
            }
            return -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PaymentAndBus paymentAndBus = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.renterpayment_view, parent, false);
            }

            TextView tvBusName = convertView.findViewById(R.id.busName);
            TextView tvDepartureCity = convertView.findViewById(R.id.departureCity);
            TextView tvArrivalCity = convertView.findViewById(R.id.arrivalCity);
            TextView tvPrice = convertView.findViewById(R.id.price);
            TextView tvSeats = convertView.findViewById(R.id.seats);
            TextView tvDate = convertView.findViewById(R.id.date);
            TextView tvStatus = convertView.findViewById(R.id.status);

            if (paymentAndBus.bus != null) {
                tvBusName.setText(paymentAndBus.bus.name);
                tvDepartureCity.setText(paymentAndBus.bus.departure.stationName.toString());
                tvArrivalCity.setText(paymentAndBus.bus.arrival.stationName.toString());
                String price = String.format(Locale.getDefault(), "%.2f", paymentAndBus.bus.price.price);
                tvPrice.setText("IDR " + price);
            }

            if (paymentAndBus.payment != null) {
                StringBuilder seatsBuilder = new StringBuilder();
                for (String seat : paymentAndBus.payment.busSeat) {
                    if (seatsBuilder.length() > 0) {
                        seatsBuilder.append(", ");
                    }
                    seatsBuilder.append(seat);
                }
                tvSeats.setText(seatsBuilder.toString());
                tvStatus.setText(paymentAndBus.payment.status.toString());
                if(paymentAndBus.payment.status == Invoice.PaymentStatus.WAITING){
                    Button cancel = convertView.findViewById(R.id.cancel);
                    cancel.setVisibility(View.VISIBLE);
                    cancel.setOnClickListener(v->{
                        cancelPayment(paymentAndBus.payment.id);
                        cancel.setVisibility(View.GONE);
                        tvStatus.setText(Invoice.PaymentStatus.FAILED.toString());
                    });
                    Button accept = convertView.findViewById(R.id.accept);
                    accept.setVisibility(View.VISIBLE);
                    accept.setOnClickListener(v->{
                        acceptPayment(paymentAndBus.payment.id);
                        accept.setVisibility(View.GONE);
                        tvStatus.setText(Invoice.PaymentStatus.SUCCESS.toString());
                        MainActivity.loggedAccount.balance += (paymentAndBus.payment.busSeat.size() * paymentAndBus.bus.price.price);
                    });
                }
                tvDate.setText(paymentAndBus.payment.departureDate.toString());
            }

            return convertView;
        }
    }

    protected class PaymentAndBus {
        public Payment payment;
        public Bus bus;

        public PaymentAndBus(Payment payment, Bus bus) {
            this.payment = payment;
            this.bus = bus;
        }
    }

}