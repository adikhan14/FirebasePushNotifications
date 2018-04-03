package com.app.firebasepushnotifications;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Adil khan on 2/12/2018.
 */

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    private List<Users> usersList;
    Context mContext;

    public UsersRecyclerAdapter(Context mContext, List<Users> usersList) {
        this.usersList = usersList;
        this.mContext = mContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final String user_name = usersList.get(position).getName();
        holder.tv_user_name.setText(user_name);
        Glide.with(mContext).load(usersList.get(position).getImage()).into(holder.user_image_view);

        final String user_id = usersList.get(position).userId;
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(mContext, SendActivity.class);
                sendIntent.putExtra("user_id", user_id);
                sendIntent.putExtra("user_name", user_name);
                mContext.startActivity(sendIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private CircleImageView user_image_view;
        private TextView tv_user_name;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            user_image_view = mView.findViewById(R.id.user_list_image);
            tv_user_name = mView.findViewById(R.id.user_list_name);


        }
    }
}
