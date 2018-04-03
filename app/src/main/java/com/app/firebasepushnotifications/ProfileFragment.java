package com.app.firebasepushnotifications;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Adil khan on 2/7/2018.
 */

public class ProfileFragment extends Fragment {

    private Button bLogout;
    private FirebaseAuth mAuth;

    private TextView tvProfileName;
    private CircleImageView civProfilePic;
    private String mUserId;

    private FirebaseFirestore mFirebaseFirestore;

    public ProfileFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        mUserId = mAuth.getCurrentUser().getUid();

        civProfilePic = view.findViewById(R.id.civ_profile_image);
        tvProfileName = view.findViewById(R.id.tv_profile_name);

        mFirebaseFirestore.collection("Users").document(mUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String user_name = documentSnapshot.getString("name");
                String user_image = documentSnapshot.getString("image");
                String user_token_id = documentSnapshot.getString("token_id");

                String tokenId = FirebaseInstanceId.getInstance().getToken();

                if (!tokenId.equals(user_token_id)) {
                    mAuth.signOut();
                    Intent loginIntent = new Intent(container.getContext(), LoginActivity.class);
                    startActivity(loginIntent);
                    getActivity().finish();
                } else {
                    tvProfileName.setText(user_name);
                    Glide.with(container.getContext()).load(user_image).into(civProfilePic);

                }


            }
        });

        bLogout = view.findViewById(R.id.b_reg_logout);
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> tokenMapResponse = new HashMap<>();
                tokenMapResponse.put("token_id", FieldValue.delete());
                mFirebaseFirestore.collection("Users").document(mUserId).update(tokenMapResponse).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mAuth.signOut();
                        Intent loginIntent = new Intent(container.getContext(), LoginActivity.class);
                        startActivity(loginIntent);
                        getActivity().finish();
                    }
                });

            }
        });

        return view;
    }
}
