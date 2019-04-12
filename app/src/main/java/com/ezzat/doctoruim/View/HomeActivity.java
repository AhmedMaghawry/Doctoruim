package com.ezzat.doctoruim.View;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezzat.doctoruim.Control.PagerAdapter;
import com.ezzat.doctoruim.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private TextView countRequests;
    private FrameLayout requests;
    private ImageView messages, logout;
    private int count = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Doctors"));
        tabLayout.addTab(tabLayout.newTab().setText("Patients"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        logout = toolbar.findViewById(R.id.logout);
        messages = toolbar.findViewById(R.id.email);
        requests = toolbar.findViewById(R.id.request);
        countRequests = toolbar.findViewById(R.id.cart_badge);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:OPEN Messages
            }
        });

        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Open requests
            }
        });

        setupBadge();
    }

    private void setupBadge() {

        if (countRequests != null) {
            if (count == 0) {
                if (countRequests.getVisibility() != View.GONE) {
                    countRequests.setVisibility(View.GONE);
                }
            } else {
                countRequests.setText(String.valueOf(Math.min(count, 99)));
                if (countRequests.getVisibility() != View.VISIBLE) {
                    countRequests.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
