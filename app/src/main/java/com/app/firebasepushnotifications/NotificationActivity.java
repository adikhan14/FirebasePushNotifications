package com.app.firebasepushnotifications;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    private TextView tv_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        String dataMessage = getIntent().getStringExtra("message");
        String dataFrom = getIntent().getStringExtra("from_user_id");
        tv_notification = findViewById(R.id.tv_notification);
        tv_notification.setText("From : " + dataFrom + " | Message : " + dataMessage);

    }
}
