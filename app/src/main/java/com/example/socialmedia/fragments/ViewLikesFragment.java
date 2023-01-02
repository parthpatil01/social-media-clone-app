package com.example.socialmedia.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.R;
import com.example.socialmedia.adapters.ViewLikesAdapter;
import com.example.socialmedia.models.LikesFollowersFollowing;
import com.example.socialmedia.utils.FirebaseUtil;

import java.util.List;

public class ViewLikesFragment extends Fragment {

    private RecyclerView rv;
    private ViewLikesAdapter vad;
    private LinearLayout viewLikes;
    private TextView noData;

    public ViewLikesFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_likes, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.view_likes_fragment_recycler);
        viewLikes=view.findViewById(R.id.view_likes_progress);
        noData=view.findViewById(R.id.no_data_found);

        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(linearLayout);


        String postId = getArguments().getString("postId");
        String username = getArguments().getString("username");

        FirebaseUtil.getTotalLikes(postId, username, new FirebaseUtil.TotalLikes() {
            @Override
            public void onResult(List<LikesFollowersFollowing> list) {

                Handler handler = new Handler();
                final int delay = 500; // 1000 milliseconds == 1 second

                Runnable runnable = new Runnable() {
                    int counter = 0;
                    public void run() {
                        if (list.isEmpty() && counter != 5) {
                            System.out.println("view likes handler running");
                            counter++;
                            handler.postDelayed(this, delay);
                        } else {
                            noData.setVisibility(View.VISIBLE);
                            viewLikes.setVisibility(View.GONE);
                            handler.removeCallbacks(this);
                            vad = new ViewLikesAdapter(getContext(), list);
                            rv.setAdapter(vad);
                        }
                    }
                };

                handler.postDelayed(runnable, delay);


            }
        });


    }

}