package com.app.firebasepushnotifications;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adil khan on 2/7/2018.
 */

public class UsersFragment extends Fragment {


    RecyclerView rvUsersList;
    private List<Users> usersList;

    private FirebaseFirestore mFirebaseFirestore;
    UsersRecyclerAdapter mUserRecyclerAdapter;

    public UsersFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_fragment, container, false);

        rvUsersList = view.findViewById(R.id.rv_users_list);
        usersList = new ArrayList<>();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        mUserRecyclerAdapter = new UsersRecyclerAdapter(container.getContext(), usersList);
        rvUsersList.setHasFixedSize(true);
        rvUsersList.setLayoutManager(new LinearLayoutManager(container.getContext()));
        rvUsersList.setAdapter(mUserRecyclerAdapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        usersList.clear();

        mFirebaseFirestore.collection("Users").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        String userId = doc.getDocument().getId();

                        Users user = doc.getDocument().toObject(Users.class).withId(userId);
                        usersList.add(user);
                        mUserRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
