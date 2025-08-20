package com.example.aqbahinventoryapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SmsPermissionActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 101;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_permission);

        tvStatus = findViewById(R.id.tvSmsPermissionStatus);
        Button btnRequest = findViewById(R.id.btnRequestSmsPermission);

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSmsPermission();
            }
        });
    }

    private void checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_CODE);
        } else {
            sendTestSms();
        }
    }

    private void sendTestSms() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("1234567890", null,
                    "Test notification: Inventory app alert!",
                    null, null);
            tvStatus.setText("SMS sent successfully!");
        } catch (Exception e) {
            tvStatus.setText("Failed to send SMS: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendTestSms();
            } else {
                Toast.makeText(this, "SMS permission denied. App will continue without SMS.", Toast.LENGTH_LONG).show();
                tvStatus.setText("Permission denied");
            }
        }
    }
}
