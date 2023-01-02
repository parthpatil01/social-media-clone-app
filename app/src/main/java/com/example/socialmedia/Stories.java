package com.example.socialmedia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.socialmedia.fragments.HomeFragment;
import com.example.socialmedia.fragments.StoryFragment;

import java.util.Timer;

public class Stories extends FragmentActivity {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;
    public static int initPosition=0;
    public static Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

        viewPager = findViewById(R.id.pager);
        viewPager.setPageTransformer(new StoryTransition());
        pagerAdapter = new ScreenSlidePagerAdapter(this, new ScreenSlidePagerAdapter.StoryPageListener() {
            @Override
            public void onStoryEnd(int position) {
                position=position+1;
                    if(position<HomeFragment.list.size()){
                        viewPager.setCurrentItem(position);
                    }else {
                        timer.cancel();
                        StoryFragment.timerTask.cancel();
                        finish();
                    }
            }
        });
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(initPosition,false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
        StoryFragment.timerTask.cancel();
    }

    public static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        private StoryPageListener listener;
        public ScreenSlidePagerAdapter(FragmentActivity fa,StoryPageListener listener) {
            super(fa);
            this.listener=listener;
        }

        @Override
        public Fragment createFragment(int position) {
            return new StoryFragment(HomeFragment.list.get(position),position,listener);
        }

        @Override
        public int getItemCount() {
            return HomeFragment.list.size();
        }

        public interface StoryPageListener {
            public void onStoryEnd(int position) ;
        }
    }


}


//{
//
//        if (count == 500) {
//        progressBarIndex++;
//        if (progressBarIndex < progressContainer.getChildCount()) {
//        count = 0;
//        Glide.with(getApplicationContext()).load(images.get(progressBarIndex)).into(story);
//        } else {
//        timer.cancel();
//        finish();
//        }
//
//        } else {
//        System.out.println(count);
//        count++;
//        ((ProgressBar) progressContainer.getChildAt(progressBarIndex)).setProgress(count / 5);
//        }
//
//
//        }