package com.FairuzMuhammadJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
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
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.addBus){
            moveActivity(this, AddBusActivity.class);
        }
        return (super.onOptionsItemSelected(item));
    }

    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    protected void getBus(){
        int userId = MainActivity.loggedAccount.id;
        mApiService.getMyBus(userId).enqueue(new Callback<List<Bus>>(){
            public void onResponse(Call<List< Bus >> call, Response<List<Bus>> response){
                if(!response.isSuccessful()){
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
}