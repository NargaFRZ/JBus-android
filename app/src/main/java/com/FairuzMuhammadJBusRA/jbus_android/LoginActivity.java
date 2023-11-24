package com.FairuzMuhammadJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.FairuzMuhammadJBusRA.jbus_android.model.Account;
import com.FairuzMuhammadJBusRA.jbus_android.model.BaseResponse;
import com.FairuzMuhammadJBusRA.jbus_android.request.BaseApiService;
import com.FairuzMuhammadJBusRA.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView registerNow = null;
    private Button loginButton = null;
    private EditText email, password;
    private BaseApiService mApiService;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mContext = this;
        mApiService = UtilsApi.getApiService();
        registerNow = findViewById(R.id.register_now);
        loginButton = findViewById(R.id.login_button);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        registerNow.setOnClickListener(v->{
            moveActivity(this, RegisterActivity.class);
            viewToast(this, "Register Account");
        });

        loginButton.setOnClickListener(v->{
            handleLogin();
        });
    }

    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    protected void handleLogin(){
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();
        if (emailS.isEmpty() || passwordS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        mApiService.login(emailS, passwordS).enqueue(new Callback<BaseResponse<Account>>() {
            public void onResponse(Call<BaseResponse< Account >> call, Response<BaseResponse<Account>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Account> res = response.body();
                if (res.success) {
                    MainActivity.loggedAccount = res.payload;
                    moveActivity(LoginActivity.this, MainActivity.class);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                System.out.println("On Failure");
                Toast.makeText(mContext, "Email atau Password salah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}