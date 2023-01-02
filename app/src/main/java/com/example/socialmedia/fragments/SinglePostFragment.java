package com.example.socialmedia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.R;
import com.example.socialmedia.SquareImage;
import com.example.socialmedia.adapters.FeedAdapter;
import com.example.socialmedia.models.FeedModel;
import com.example.socialmedia.prevalent.prevalentuser;
import com.example.socialmedia.utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;


public class SinglePostFragment extends Fragment {


    private SquareImage image;
    private TextView username, userCap, capUser, viewLikes, viewComments;
    private LinearLayout capVisibility;
    private String usern, postId;
    private ImageView heart;
    private RecyclerView recycler;
    private FeedModel f;
    private List<FeedModel> list;

    public SinglePostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        String imgUrl = getArguments().getString("imageurl");
        usern = getArguments().getString("username");
        String caption = getArguments().getString("caption");
        postId = getArguments().getString("postId");

        f = new FeedModel();
        f.setId(postId);
        f.setUsername(usern);
        f.setCaption(caption);
        f.setImageUrl(imgUrl);

        list = new ArrayList<>();
        list.add(f);
        likesCount();

    }

    private void likesCount() {
        FirebaseUtil.getLikes(postId, usern, new FirebaseUtil.LikesInterface() {
            @Override
            public void onLikesReceived(int likes, boolean liked) {

                f.setLikes(likes);
                f.setLiked(liked);

                setAdapter();
            }
        });


    }

    private void setAdapter(){

        LinearLayoutManager feedLayoutManager = new LinearLayoutManager(getContext());
        feedLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recycler.setLayoutManager(feedLayoutManager);

        recycler.setAdapter(new FeedAdapter(list, getContext(), new FeedAdapter.SendToOtherUserFrag() {
            @Override
            public void send(String username) {

            }
        }, new FeedAdapter.LikeOrRemove() {
            @Override
            public void likeRemove(String username, String postId, FeedAdapter.FeedViewHolder holder, int position) {

                FirebaseUtil.likeOrRemove(username, postId, new FirebaseUtil.LikesORRemoves() {
                    @Override
                    public void onResult(boolean existed) {

                        if (existed) {
                            int x = f.getLikes() - 1;
                            holder.heart.setImageResource(R.drawable.like);
                            f.setLiked(false);
                            f.setLikes(x);
                            holder.likes.setText(x + " likes");

                        } else {
                            int x = f.getLikes() + 1;
                            holder.heart.setImageResource(R.drawable.like_red);
                            f.setLiked(true);
                            f.setLikes(x);
                            holder.likes.setText(x + " likes");
                        }

                    }
                });

            }
        }, new FeedAdapter.ViewLikes() {
            @Override
            public void viewLikes(String postId, String username) {

                Bundle args = new Bundle();
                ViewLikesFragment fragment = new ViewLikesFragment();

                args.putString("postId", postId);
                args.putString("username", username);
                fragment.setArguments(args);

                requireActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).
                        add(R.id.homepage_framelayout, fragment).commit();

            }
        }, new FeedAdapter.ViewComments() {
            @Override
            public void viewComments(String postId, String username) {

                Bundle args = new Bundle();
                ViewCommentsFragment fragment = new ViewCommentsFragment();

                args.putString("postId", postId);
                args.putString("username", username);
                fragment.setArguments(args);

                requireActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).
                        add(R.id.homepage_framelayout, fragment).commit();


            }
        }));
    }

    private void init(View view) {
        username = view.findViewById(R.id.userName_single);
        userCap = view.findViewById(R.id.user_caption);
        image = view.findViewById(R.id.myPost);
        capUser = view.findViewById(R.id.caption_username);
        capVisibility = view.findViewById(R.id.visible_if_cap);
        viewLikes = view.findViewById(R.id.view_likes);
        viewComments = view.findViewById(R.id.comments);
        heart = view.findViewById(R.id.like_heart);
        recycler = view.findViewById(R.id.single_frag_recycler);

    }

}
