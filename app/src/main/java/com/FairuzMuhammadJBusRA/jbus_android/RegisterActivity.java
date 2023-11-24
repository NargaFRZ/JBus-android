package com.FairuzMuhammadJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.FairuzMuhammadJBusRA.jbus_android.request.BaseApiService;
import com.FairuzMuhammadJBusRA.jbus_android.request.UtilsApi;
import com.FairuzMuhammadJBusRA.jbus_android.model.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton = null;
    private BaseApiService mApiService;
    private Context mContext;
    private EditText name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        mContext = this;
        mApiService = UtilsApi.getApiService();
        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(v->{
            handleRegister();
        });
    }

    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    protected void handleRegister() {
    String nameS = name.getText().toString();
    String emailS = email.getText().toString();
    String passwordS = password.getText().toString();
    if (nameS.isEmpty() || emailS.isEmpty() || passwordS.isEmpty()) {
        Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
        return;
    }
    mApiService.register(nameS, emailS, passwordS).enqueue(new Callback<BaseResponse<Account>>() {
        @Override
        public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                return;
            }
            BaseResponse<Account> res = response.body();
            if (res.success) finish();
            Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
            Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}