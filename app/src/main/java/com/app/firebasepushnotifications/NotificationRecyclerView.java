package com.app.firebasepushnotifications;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Adil khan on 2/22/2018.
 */

public class NotificationRecyclerView extends RecyclerView.Adapter<NotificationRecyclerView.ViewHolder> {

    private List<Notifications> notificationList;
    Context mContext;
    private FirebaseFirestore mFirebaseFirestore;

    public NotificationRecyclerView(Context mContext, List<Notifications> notificationList) {
        this.notificationList = notificationList;
        this.mContext = mContext;
    }

    @Override
    public NotificationRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item, parent, false);
        return new NotificationRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        String from_id = notificationList.get(position).getFrom();
        String message = notificationList.get(position).getMessage();

        holder.tv_user_msg.setText(message);

        mFirebaseFirestore.collection("Users").document(from_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String user_name = documentSnapshot.getString("name");
                String user_image = documentSnapshot.getString("image");

                holder.tv_user_name.setText(user_name);

                Glide.with(mContext).load(user_image).into(holder.user_image_view);

            }
        });


    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private CircleImageView user_image_view;
        private TextView tv_user_name, tv_user_msg;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            user_image_view = mView.findViewById(R.id.notification_list_image);
            tv_user_name = mView.findViewById(R.id.notification_list_name);
            tv_user_msg = mView.findViewById(R.id.notification_list_msg);


        }
    }
}
