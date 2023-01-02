package com.example.socialmedia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialmedia.adapters.FeedAdapter;
import com.example.socialmedia.fragments.HomeFragment;
import com.example.socialmedia.fragments.NotificationFragment;
import com.example.socialmedia.fragments.ProfileFragment;
import com.example.socialmedia.fragments.SearchFragment;
import com.example.socialmedia.prevalent.prevalentuser;
import com.example.socialmedia.utils.FeedAdapterUtil;
import com.example.socialmedia.utils.RequestsAdapterUtil;
import com.google.android.material.tabs.TabLayout;

public class Homepage extends AppCompatActivity {

    private TabLayout tabLayout;
    private static boolean firstRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        FeedAdapterUtil.setContent(this,getApplicationContext());
        RequestsAdapterUtil.setContext(this);

        if(firstRun) {
            prevalentuser.firebaseProfileData();
            firstRun=false;
        }


        tabLayout = findViewById(R.id.tablayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectIcon(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                unselectIcon(tab.getPosition());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2) {
                    Intent intent = new Intent(Homepage.this, AddPostActivity.class);
                    overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_left );
                    startActivity(intent);
                }
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.homepage_framelayout, new HomeFragment()).commit();
        tabLayout.getTabAt(0).setIcon(R.drawable.homepage);

    }


    private void selectIcon(int position) {
        switch (position) {

            case 0:
                tabLayout.getTabAt(position).setIcon(R.drawable.homepage);
                getSupportFragmentManager().beginTransaction().replace(R.id.homepage_framelayout, new HomeFragment()).commit();

                break;

            case 1:
                tabLayout.getTabAt(position).setIcon(R.drawable.search_selected);
                getSupportFragmentManager().beginTransaction().replace(R.id.homepage_framelayout, new SearchFragment()).commit();

                break;

            case 2:
                Intent intent = new Intent(Homepage.this, AddPostActivity.class);
                startActivity(intent);
                break;

            case 3:
                tabLayout.getTabAt(position).setIcon(R.drawable.heart);
                getSupportFragmentManager().beginTransaction().replace(R.id.homepage_framelayout, new NotificationFragment()).commit();

                break;
            case 4:
                tabLayout.getTabAt(position).setIcon(R.drawable.user);
                getSupportFragmentManager().beginTransaction().replace(R.id.homepage_framelayout, new ProfileFragment()).commit();

                break;

        }
    }

    private void unselectIcon(int position) {

        switch (position) {
            case 0:
                tabLayout.getTabAt(position).setIcon(R.drawable.home);
                break;

            case 1:
                tabLayout.getTabAt(position).setIcon(R.drawable.search_logo);
                break;


            case 3:
                tabLayout.getTabAt(position).setIcon(R.drawable.heart_unselected);
                break;
            case 4:
                tabLayout.getTabAt(position).setIcon(R.drawable.user_unselected);
                break;
        }
    }


}