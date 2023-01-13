package com.example.socialmedia.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.socialmedia.Homepage;
import com.example.socialmedia.R;
import com.example.socialmedia.SendMessage;
import com.example.socialmedia.adapters.FeedAdapter;
import com.example.socialmedia.adapters.StoriesAdapter;
import com.example.socialmedia.models.FeedModel;
import com.example.socialmedia.models.StoriesModel;
import com.example.socialmedia.models.UsersData;
import com.example.socialmedia.prevalent.prevalentuser;
import com.example.socialmedia.utils.FeedAdapterUtil;
import com.example.socialmedia.utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView stories, feed;
    public static List<StoriesModel> list;
    public Handler handler;
    public Runnable runnable;
    private OtherUserProfileFragment fragment;
    private Bundle args;
    private ProgressBar pb;
    private ImageView sendMessage;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

//        args=new Bundle();
//        fragment=new OtherUserProfileFragment();


        LinearLayoutManager storiesLayoutManager = new LinearLayoutManager(getContext());
        storiesLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        stories.setLayoutManager(storiesLayoutManager);

        pb.setVisibility(View.GONE);

        LinearLayoutManager feedLayoutManager = new LinearLayoutManager(getContext());
        feedLayoutManager.setOrientation(RecyclerView.VERTICAL);
        feed.setLayoutManager(feedLayoutManager);

        feed.setAdapter(FeedAdapterUtil.getAdapter());

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SendMessage.class);
                startActivity(intent);
            }
        });

        StoriesAdapter adapter = new StoriesAdapter(list, getContext());
        stories.setAdapter(adapter);

    }



    @Override
    public void onPause() {
        super.onPause();
    }

    private void popupateFeed() {

        pb.setVisibility(View.GONE);

        LinearLayoutManager feedLayoutManager = new LinearLayoutManager(getContext());
        feedLayoutManager.setOrientation(RecyclerView.VERTICAL);
        feed.setLayoutManager(feedLayoutManager);


    }


    private void init(View view) {

        stories = view.findViewById(R.id.stories_recycler_view);
        feed = view.findViewById(R.id.feed_recycler_view);
        pb=view.findViewById(R.id.homefarg_progressbar);
        sendMessage=view.findViewById(R.id.sendMessage);
    }
}

