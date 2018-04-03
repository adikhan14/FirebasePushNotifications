package com.app.firebasepushnotifications;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button bLogin, bRegister;

    private ProgressBar mProgressBarLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mProgressBarLogin = findViewById(R.id.progressBar_login);

        bLogin = findViewById(R.id.btn_login);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String passwrod = etPassword.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(passwrod)) {
                    mProgressBarLogin.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, passwrod).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String tokenId = FirebaseInstanceId.getInstance().getToken();
                                String currentId = mAuth.getCurrentUser().getUid();

                                Map<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("token_id", tokenId);

                                mFirebaseFirestore.collection("Users").document(currentId).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        sendToMain();
                                    }
                                });

                            } else {
                                Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG)
                                        .show();
                            }
                            mProgressBarLogin.setVisibility(View.INVISIBLE);
                        }


                    });

                }

            }
        });

        bRegister = findViewById(R.id.btn_login_register);
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(loginIntent);
        finish();

    }
}
