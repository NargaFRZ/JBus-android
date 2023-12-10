package com.FairuzMuhammadJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import com.FairuzMuhammadJBusRA.jbus_android.model.Account;
import com.FairuzMuhammadJBusRA.jbus_android.model.BaseResponse;
import com.FairuzMuhammadJBusRA.jbus_android.request.BaseApiService;
import com.FairuzMuhammadJBusRA.jbus_android.request.UtilsApi;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutMeActivity extends AppCompatActivity {
    private Button topUpButton, register, manage, logout, payment = null;
    private EditText amount = null;
    private TextView initial, usernameid, emailid, balanceid, areyou;
    private BaseApiService mApiService;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        getSupportActionBar().hide();

        mContext = this;
        mApiService = UtilsApi.getApiService();
        initial = findViewById(R.id.initial);
        usernameid = findViewById(R.id.username);
        emailid = findViewById(R.id.email);
        balanceid = findViewById(R.id.balance);
        topUpButton = findViewById(R.id.topupbutton);
        payment =  findViewById(R.id.payment);
        amount = findViewById(R.id.topupamount);
        areyou = findViewById(R.id.areyou);
        register = findViewById(R.id.companyreg);
        manage = findViewById(R.id.manage);
        logout = findViewById(R.id.logout);
        String balances = String.format(Locale.getDefault(), "%.2f", MainActivity.loggedAccount.balance);

        initial.setText(getInitials(MainActivity.loggedAccount.name));
        usernameid.setText(MainActivity.loggedAccount.name);
        emailid.setText(MainActivity.loggedAccount.email);
        balanceid.setText("IDR " + balances);

        topUpButton.setOnClickListener(v->{
            topUp();
        });

        logout.setOnClickListener(v->{
            MainActivity.loggedAccount = null;
            moveActivity(this, LoginActivity.class);
        });

        if(MainActivity.loggedAccount.company!=null){
            manage.setVisibility(View.VISIBLE);
            payment.setVisibility(View.VISIBLE);
            register.setVisibility(View.GONE);
            register.setEnabled(false);
            areyou.setVisibility(View.GONE);
        }

        register.setOnClickListener(v->{
            moveActivity(this, RegisterRenterActivity.class);
        });

        manage.setOnClickListener(v->{
            moveActivity(this, ManageBusActivity.class);
        });

        payment.setOnClickListener(v->{
            moveActivity(this, RenterPaymentActivity.class);
        });
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        String[] parts = name.split(" ");
        String initials = "";
        for (String part : parts) {
            if (!part.trim().isEmpty()) {
                initials += part.substring(0, 1).toUpperCase();
            }
        }
        return initials;
    }

    protected void topUp(){
        int userId = MainActivity.loggedAccount.id;
        String amountS = amount.getText().toString();
        if(amountS.isEmpty()){
            Toast.makeText(mContext,"Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        double amountD = Double.parseDouble(amountS);
        mApiService.topUp(userId, amountD).enqueue(new Callback<BaseResponse<Account>>(){
            public void onResponse(Call<BaseResponse< Account >> call, Response<BaseResponse<Account>> response){
                if(!response.isSuccessful()){
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Account> res = response.body();
                if(res.success){
                    MainActivity.loggedAccount.balance = res.payload.balance;
                    String balances = String.format(Locale.getDefault(), "%.2f", MainActivity.loggedAccount.balance);
                    balanceid.setText("IDR " + balances);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                System.out.println("On Failure");
                Toast.makeText(mContext, "Invalid Input", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        moveActivity(this, MainActivity.class);
    }
}
