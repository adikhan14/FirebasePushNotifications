package com.app.firebasepushnotifications;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TextView tvProfile, tvUsers, tvNotifications;
    private ViewPager mViewPager;
    private PagerViewAdapter mPagerViewAdapter;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvProfile = findViewById(R.id.tv_profile);
        tvUsers = findViewById(R.id.tv_all_users);
        tvNotifications = findViewById(R.id.tv_notifications);
        mViewPager = findViewById(R.id.main_pager);
        mViewPager.setOffscreenPageLimit(2);

        mAuth = FirebaseAuth.getInstance();

        mPagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mPagerViewAdapter);

        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        tvUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });

        tvNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTabs(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null){
            Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

    }

    private void changeTabs(int position) {

        if (position == 0) {
            tvProfile.setTextColor(getResources().getColor(R.color.textTabBright));
            tvProfile.setTextSize(22);
            tvUsers.setTextColor(getResources().getColor(R.color.textTabLight));
            tvUsers.setTextSize(16);
            tvNotifications.setTextColor(getResources().getColor(R.color.textTabLight));
            tvNotifications.setTextSize(16);
        } else if (position == 1) {
            tvProfile.setTextColor(getResources().getColor(R.color.textTabLight));
            tvProfile.setTextSize(16);
            tvUsers.setTextColor(getResources().getColor(R.color.textTabBright));
            tvUsers.setTextSize(22);
            tvNotifications.setTextColor(getResources().getColor(R.color.textTabLight));
            tvNotifications.setTextSize(16);
        } else if (position == 2) {
            tvProfile.setTextColor(getResources().getColor(R.color.textTabLight));
            tvProfile.setTextSize(16);
            tvUsers.setTextColor(getResources().getColor(R.color.textTabLight));
            tvUsers.setTextSize(16);
            tvNotifications.setTextColor(getResources().getColor(R.color.textTabBright));
            tvNotifications.setTextSize(22);
        }

    }
}
