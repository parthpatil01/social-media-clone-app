package com.example.socialmedia;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.socialmedia.addpostfragments.CameraFragment;
import com.example.socialmedia.addpostfragments.GalleryFragment;
import com.example.socialmedia.utils.Permissions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class AddPostActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private List<Fragment> fragments;
    private TabLayout tabLayout;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        init();
        setupViewPager();


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right );
    }

    public void init() {
        tabLayout = findViewById(R.id.add_post_tablayout);
        viewPager = findViewById(R.id.add_post_viewpager);
        fragments = new ArrayList<>();
        fragments.add(new GalleryFragment());
        fragments.add(new CameraFragment());
    }

    private void setupViewPager() {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        viewPager.setAdapter(sectionsPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText(getString(R.string.gallery));
                        break;

                    case 1:
                        tab.setText(getString(R.string.camera));
                        break;
                }
            }
        }

        ).attach();

    }

    private class SectionsPagerAdapter extends FragmentStateAdapter {

        public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }


//    public void verifyPermissions(String[] permissions){
//
//        ActivityCompat.requestPermissions(
//                AddPostActivity.this,
//                permissions,
//                VERIFY_PERMISSIONS_REQUEST
//        );
//    }
//
//
//     // Check an array of permissions
//
//    public boolean checkPermissionsArray(String[] permissions){
//
//        for(int i = 0; i< permissions.length; i++){
//            String check = permissions[i];
//            if(!checkPermissions(check)){
//                return false;
//            }
//        }
//        return true;
//    }
//
//
//     //Check a single permission is it has been verified
//
//    public boolean checkPermissions(String permission){
//
//        int permissionRequest = ActivityCompat.checkSelfPermission(AddPostActivity.this, permission);
//
//        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
//            return false;
//        }
//        else{
//            return true;
//        }
//    }
}