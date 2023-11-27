package com.FairuzMuhammadJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.FairuzMuhammadJBusRA.jbus_android.model.Account;
import com.FairuzMuhammadJBusRA.jbus_android.model.BaseResponse;
import com.FairuzMuhammadJBusRA.jbus_android.model.Renter;
import com.FairuzMuhammadJBusRA.jbus_android.request.BaseApiService;
import com.FairuzMuhammadJBusRA.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRenterActivity extends AppCompatActivity {

    private Button registerButton = null;
    private BaseApiService mApiService;
    private Context mContext;
    private EditText name, address, phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_renter);
        getSupportActionBar().hide();

        mContext = this;
        mApiService = UtilsApi.getApiService();
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.number);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(v->{
            handleRegister();
        });
    }

    protected void handleRegister() {
        int userId = MainActivity.loggedAccount.id;
        String nameS = name.getText().toString();
        String addressS = address.getText().toString();
        String phoneS = phone.getText().toString();
        if (nameS.isEmpty() || addressS.isEmpty() || phoneS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.registerRenter(userId, nameS, addressS, phoneS).enqueue(new Callback<BaseResponse<Renter>>() {
            @Override
            public void onResponse(Call<BaseResponse<Renter>> call, Response<BaseResponse<Renter>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Renter> res = response.body();
                if (res.success) {
                    MainActivity.loggedAccount.company = res.payload;
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Renter>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}