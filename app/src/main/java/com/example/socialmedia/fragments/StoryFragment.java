package com.example.socialmedia.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.socialmedia.R;
import com.example.socialmedia.Stories;
import com.example.socialmedia.models.StoriesModel;

import java.util.Timer;
import java.util.TimerTask;

public class StoryFragment extends Fragment {

    private ImageView story;
    private LinearLayout progressContainer;
    private ProgressBar defaultProgressBar;
    private int progressBarIndex = 0;
    private int count = 0;
    private int progressCount = 0, childCount = 0;

    private Boolean timerOn = true;
    private StoriesModel storiesModel;
    public static TimerTask timerTask;
    private Stories.ScreenSlidePagerAdapter.StoryPageListener listener;
    private int position;


    public StoryFragment() {
        // Required empty public constructor
    }

    public StoryFragment(StoriesModel storiesModel, int position, Stories.ScreenSlidePagerAdapter.StoryPageListener listener) {
        this.storiesModel = storiesModel;
        this.listener = listener;
        this.position = position;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_fragmet, container, false);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        Glide.with(this).load(storiesModel.getStories().get(progressBarIndex)).into(story);

        setProgressBar();
        setControls();
    }

    @Override
    public void onResume() {
        super.onResume();
        setTimer();
        timerOn = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        Stories.timer.cancel();
        timerTask.cancel();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Stories.timer.cancel();
        timerTask.cancel();
    }


    private void init(View view) {
        story = view.findViewById(R.id.story);
        progressContainer = view.findViewById(R.id.progress_container);
        defaultProgressBar = (ProgressBar) progressContainer.getChildAt(0);


    }

    private void setProgressBar() {
        if (storiesModel.getStories() != null) {

            for (int i = 1; i < storiesModel.getStories().size(); i++) {
                ProgressBar progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
                progressBar.setLayoutParams(defaultProgressBar.getLayoutParams());
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.story_progressbar));
                progressContainer.addView(progressBar);
            }
        }
        childCount = progressContainer.getChildCount();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setControls() {

        float getWidth = (getResources().getDisplayMetrics().widthPixels / 100.0f) * 25;
        float upperLimit = (getResources().getDisplayMetrics().widthPixels - getWidth);
        float lowerLimit = getWidth;

        story.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        if (event.getX() <= lowerLimit && progressBarIndex > 0) {
                            ((ProgressBar) progressContainer.getChildAt(progressBarIndex)).setProgress(0);
                            progressBarIndex--;
                            ((ProgressBar) progressContainer.getChildAt(progressBarIndex)).setProgress(0);
                            count = 0;
                            Glide.with(getActivity()).load(storiesModel.getStories().get(progressBarIndex)).into(story);


                        } else if (event.getX() >= upperLimit) {

                            if (progressBarIndex < progressContainer.getChildCount() - 1) {

                                ((ProgressBar) progressContainer.getChildAt(progressBarIndex)).setProgress(100);
                                progressBarIndex++;
                                ((ProgressBar) progressContainer.getChildAt(progressBarIndex)).setProgress(0);
                                count = 0;
                                Glide.with(getActivity()).load(storiesModel.getStories().get(progressBarIndex)).into(story);

                            } else {
                                reset();
                                listener.onStoryEnd(position);
                                Stories.timer.cancel();
                                timerTask.cancel();
                            }
                        } else {

                            timerOn = false;
                            return true;
                        }

                    case MotionEvent.ACTION_UP:
                        timerOn = true;
                        return true;


                }

                return false;
            }
        });


    }


    private void setTimer() {

        Stories.timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {

                System.out.println("timer task "+position);
                if (timerOn) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (count == 100) {
                                progressBarIndex++;
                                if (progressBarIndex < childCount) {
                                    count = 0;
                                    Glide.with(getActivity()).load(storiesModel.getStories().get(progressBarIndex)).centerCrop().into(story);
                                } else {

                                    reset();
                                    listener.onStoryEnd(position);
                                    Stories.timer.cancel();
                                    timerTask.cancel();

                                }

                            } else {
                                System.out.println(count);
                                count++;
                                ((ProgressBar) progressContainer.getChildAt(progressBarIndex)).setProgress(count);
                            }


                        }
                    });

                }


            }
        };
        Stories.timer.scheduleAtFixedRate(timerTask, 0, 70);


    }


    private void reset() {

        if (progressBarIndex == progressContainer.getChildCount()) {
            progressBarIndex = 0;
            count = 0;

            for (int i = 0; i < progressContainer.getChildCount(); i++) {
                ((ProgressBar) progressContainer.getChildAt(i)).setProgress(0);

            }
            Glide.with(this).load(storiesModel.getStories().get(progressBarIndex)).into(story);

        }

    }


}