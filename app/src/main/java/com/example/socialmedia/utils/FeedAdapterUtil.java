package com.example.socialmedia.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.example.socialmedia.R;
import com.example.socialmedia.adapters.FeedAdapter;
import com.example.socialmedia.fragments.OtherUserProfileFragment;
import com.example.socialmedia.fragments.ViewCommentsFragment;
import com.example.socialmedia.fragments.ViewLikesFragment;
import com.example.socialmedia.models.FeedModel;
import com.example.socialmedia.prevalent.prevalentuser;

import java.util.List;

public class FeedAdapterUtil {
    private static Context context;
    private static FragmentActivity fa;
    private static List<FeedModel> feedModelData;
    public static FeedAdapter feedAdapter;

    public static void setAdapter(){


        feedAdapter = new FeedAdapter(prevalentuser.feedModelData, context, new FeedAdapter.SendToOtherUserFrag() {
            @Override
            public void send(String username) {

                Bundle args = new Bundle();
                OtherUserProfileFragment fragment = new OtherUserProfileFragment();

                args.putString("otherUser", username);
                fragment.setArguments(args);

                fa.getSupportFragmentManager().beginTransaction().addToBackStack(null).
                        replace(R.id.homepage_framelayout, fragment).commit();

            }
        }, new FeedAdapter.LikeOrRemove() {
            @Override
            public void likeRemove(String usernmae, String postId, FeedAdapter.FeedViewHolder holder, int position) {

                FirebaseUtil.likeOrRemove(usernmae, postId, new FirebaseUtil.LikesORRemoves() {
                    @Override
                    public void onResult(boolean existed) {
                        FeedModel fm = prevalentuser.feedModelData.get(position);
                        if (existed) {
                            int x = fm.getLikes() - 1;
//                            holder.heart.setImageResource(R.drawable.like);
//                            holder.likes.setText(x + " likes");

                            fm.setLiked(false);
                            fm.setLikes(x);
                            FeedAdapterUtil.getAdapter().notifyDataSetChanged();

                        } else {
                            int x = fm.getLikes() + 1;
//                            holder.heart.setImageResource(R.drawable.like_red);
//                            holder.likes.setText(x + " likes");

                            fm.setLiked(true);
                            fm.setLikes(x);
                            FeedAdapterUtil.getAdapter().notifyDataSetChanged();

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

                fa.getSupportFragmentManager().beginTransaction().addToBackStack(null).
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

                fa.getSupportFragmentManager().beginTransaction().addToBackStack(null).
                        add(R.id.homepage_framelayout, fragment).commit();

            }
        });

    }

    public static void setContent(FragmentActivity fragmentActivity, Context cont){

        fa=fragmentActivity;
        context=cont;

    }

    public static void setFeedList(List<FeedModel> data){
        feedModelData=data;
    }

    public static FeedAdapter getAdapter(){
        return feedAdapter;
    }
}
