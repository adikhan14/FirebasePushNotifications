package com.app.firebasepushnotifications;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {

    private TextView tvUserId;
    private EditText etSendMsg;
    private Button bSendMsg;
    String mUserId;
    String mCurrentId;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirebaseFirestore;
    ProgressBar mProgressBarSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mCurrentId = mAuth.getUid();

        mUserId = getIntent().getStringExtra("user_id");
        tvUserId = findViewById(R.id.tv_user_id);
        tvUserId.setText("Send to " + getIntent().getStringExtra("user_name"));

        etSendMsg = findViewById(R.id.et_send_msg);
        bSendMsg = findViewById(R.id.b_send_msg);
        mProgressBarSend = findViewById(R.id.progressBar_send);

        bSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etSendMsg.getText().toString();

                if (!TextUtils.isEmpty(msg)) {

                    mProgressBarSend.setVisibility(View.VISIBLE);

                    Map<String, Object> notificationMessage = new HashMap<>();
                    notificationMessage.put("from", mCurrentId);
                    notificationMessage.put("message", msg);

                    mFirebaseFirestore.collection("Users/" + mUserId + "/Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(SendActivity.this, "Notification Sent", Toast.LENGTH_SHORT).show();
                            etSendMsg.setText("");
                            mProgressBarSend.setVisibility(View.INVISIBLE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SendActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressBarSend.setVisibility(View.INVISIBLE);
                        }
                    });


                }
            }
        });
    }
}
