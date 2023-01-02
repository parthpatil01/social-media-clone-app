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


        String profile = "https://firebasestorage.googleapis.com/v0/b/social-media-14c00.appspot.com/o/POSTS%2Fparth_123%2F1653238030711?alt=media&token=99e8d79d-8522-4d68-97fb-da5eda59413f";
        String profile1 = "https://expertphotography.b-cdn.net/wp-content/uploads/2020/08/social-media-profile-photos.jpg";
        String profile2 = "https://img.freepik.com/free-photo/side-view-man-looking-away_23-2148749546.jpg?w=996&t=st=1668928374~exp=1668928974~hmac=103b100a869785061c0ccc6b07dbbb4887a0ceda4e5fa8a35664cc663d92e50e";
        String profile3 = "https://myanimals.com/wp-content/uploads/2018/05/Rottweiler-2.jpg?auto=webp&quality=45&width=1920&crop=16:9,smart,safe";



        List<String> images = new ArrayList<>();

        images.add("https://wallpaperaccess.com/full/91412.jpg");
        images.add("https://wallpapercave.com/wp/wp3862162.jpg");
        images.add("https://iphone6papers.com/wp-content/uploads/papers.co-vy78-circle-red-simple-minimal-pattern-background-33-iphone6-wallpaper-250x444.jpg");

        List<String> images1 = new ArrayList<>();
        images1.add("https://wallpapercave.com/w/wp5525525.jpg");
        images1.add("https://wallpapercave.com/w/wp4788551.jpg");

        List<String> images2 = new ArrayList<>();
        images2.add("https://wallpapercave.com/w/wp4861261.jpg");
        images2.add("https://wallpapercave.com/w/wp5525528.jpg");

        List<String> images3 = new ArrayList<>();
        images3.add("https://wallpapercave.com/w/wp5139209.jpg");
        images3.add("https://wallpapercave.com/w/wp5525539.jpg");
        images3.add("https://wallpapercave.com/w/wp5525565.jpg");

        list = new ArrayList<>();
        list.add(new StoriesModel("parth_xy", profile, images));
        list.add(new StoriesModel("rene", profile1, images1));
        list.add(new StoriesModel("nulee", profile2, images2));
        list.add(new StoriesModel("ohayooooo", profile3, images3));


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

//        handler = new Handler();
//        final int delay = 500; // 1000 milliseconds == 1 second
//
//        runnable= new Runnable() {
//            int counter=0;
//            public void run() {
//                System.out.println("Home fragment handler"+" "+counter);
//                if(!prevalentuser.usersData.isEmpty() || counter==15) {
//
//                    popupateFeed();
//                    System.out.println("Home fragment handler stopped");
//
//                }else{
//                    handler.postDelayed(this,delay);
//                    counter++;
//                }
//            }
//        };
//
//        handler.postDelayed(runnable, delay);


        StoriesAdapter adapter = new StoriesAdapter(list, getContext());
        stories.setAdapter(adapter);

    }



    @Override
    public void onPause() {
        super.onPause();
//        handler.removeCallbacks(runnable);
    }

    private void popupateFeed() {

        pb.setVisibility(View.GONE);

        LinearLayoutManager feedLayoutManager = new LinearLayoutManager(getContext());
        feedLayoutManager.setOrientation(RecyclerView.VERTICAL);
        feed.setLayoutManager(feedLayoutManager);


//        List<UsersData> listUser = prevalentuser.usersData;
//
//
//
//        FeedAdapter feedAdapter = new FeedAdapter(listUser, getContext(), new FeedAdapter.SendToOtherUserFrag() {
//            @Override
//            public void send(String username) {
//
//                args.putString("otherUser",username);
//                fragment.setArguments(args);
//
//                requireActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).
//                        replace(R.id.homepage_framelayout, fragment).commit();
//            }
//        });

//        for (int i = 0; i < listUser.size(); i++) {
//
//            UsersData ud=listUser.get(i);
//            FirebaseUtil.snapFirebase(ud.getUsername(), new FirebaseUtil.snapInterface() {
//                @Override
//                public void onUpdateRetrieved(UsersData.PostData postsData) {
//
//                }
//            });
//
//
//        }

//        feed.setAdapter(FeedAdapterUtil.getAdapter());
//        handler.removeCallbacks(runnable);


    }


    private void init(View view) {

        stories = view.findViewById(R.id.stories_recycler_view);
        feed = view.findViewById(R.id.feed_recycler_view);
        pb=view.findViewById(R.id.homefarg_progressbar);
        sendMessage=view.findViewById(R.id.sendMessage);
    }
}

/*






* */