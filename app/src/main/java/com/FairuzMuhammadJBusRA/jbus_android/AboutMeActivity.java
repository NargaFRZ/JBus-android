package com.FairuzMuhammadJBusRA.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;
import android.os.Bundle;

public class AboutMeActivity extends AppCompatActivity {

    private String username = "lebron.james";
    private String email = "kingjames@gmail.com";
    private String balance = "IDR 1,525,478,507,462.00";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        getSupportActionBar().hide();

        TextView initial = findViewById(R.id.initial);
        TextView usernameid = findViewById(R.id.username);
        TextView emailid = findViewById(R.id.email);
        TextView balanceid = findViewById(R.id.balance);

        initial.setText(getInitials(username));
        usernameid.setText(username);
        emailid.setText(email);
        balanceid.setText(balance);
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
}
