package com.app.firebasepushnotifications;

import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button bLogin, bRegister;
    private CircleImageView ibImage;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;

    private StorageReference mStorageReference;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private ProgressBar mRegisterProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mStorageReference = FirebaseStorage.getInstance().getReference().child("images");
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mRegisterProgressBar = findViewById(R.id.progressbar_register);

        etName = findViewById(R.id.et_reg_name);
        etEmail = findViewById(R.id.et_reg_email);
        etPassword = findViewById(R.id.et_reg_password);

        imageUri = null;
        ibImage = findViewById(R.id.ib_reg_image);

        bRegister = findViewById(R.id.btn_register);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    mRegisterProgressBar.setVisibility(View.VISIBLE);
                    final String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();

                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final String userId = mAuth.getCurrentUser().getUid();
                                    final StorageReference user_profile = mStorageReference.child(userId + ".jpg");
                                    user_profile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) {

                                            if (uploadTask.isSuccessful()) {

                                                String tokenId = FirebaseInstanceId.getInstance().getToken();
                                                String downloadUri = uploadTask.getResult().getDownloadUrl().toString();
                                                Map<String, Object> userMap = new HashMap<>();
                                                userMap.put("name", name);
                                                userMap.put("image", downloadUri);
                                                userMap.put("token_id", tokenId);
                                                mFirestore.collection("Users").document(userId).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        sentToMain();
                                                    }

                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(RegisterActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                        mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(RegisterActivity.this, "ImageError : " + uploadTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                mRegisterProgressBar.setVisibility(View.INVISIBLE);

                                            }

                                        }
                                    });

                                } else {
                                    Toast.makeText(RegisterActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                    }
                }

            }
        });

        bLogin = findViewById(R.id.btn_reg_login);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ibImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            ibImage.setImageURI(imageUri);
        }
    }

    private void sentToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
