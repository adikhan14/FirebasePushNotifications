package com.app.firebasepushnotifications;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
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

public class NotificationsFragment extends Fragment {

    RecyclerView rvUsersList;
    private List<Notifications> notificationsList;

    private FirebaseFirestore mFirebaseFirestore;
    NotificationRecyclerView mNotificationRecyclerAdapter;

    public NotificationsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifications_fragment, container, false);

        rvUsersList = view.findViewById(R.id.rv_notification_list);
        notificationsList = new ArrayList<>();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mNotificationRecyclerAdapter = new NotificationRecyclerView(container.getContext(), notificationsList);
        rvUsersList.setHasFixedSize(true);
        rvUsersList.setLayoutManager(new LinearLayoutManager(container.getContext()));
        rvUsersList.setAdapter(mNotificationRecyclerAdapter);

        mFirebaseFirestore.collection("Users").document(current_user_id).collection("Notifications").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                    Notifications notification = doc.getDocument().toObject(Notifications.class);
                    notificationsList.add(notification);
                    mNotificationRecyclerAdapter.notifyDataSetChanged();

                }
            }
        });


        return view;

    }


}
