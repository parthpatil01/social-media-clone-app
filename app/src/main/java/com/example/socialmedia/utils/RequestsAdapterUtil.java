package com.example.socialmedia.utils;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.socialmedia.R;
import com.example.socialmedia.adapters.RequestsAdapter;
import com.example.socialmedia.fragments.OtherUserProfileFragment;
import com.example.socialmedia.prevalent.prevalentuser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class RequestsAdapterUtil {

    private static RequestsAdapter requestsAdapter;
    private static FragmentActivity fragmentActivity;


    public static void setRequestAdapter() {

        requestsAdapter = new RequestsAdapter(prevalentuser.requests, new RequestsAdapter.ConfirmDelete() {
            @Override
            public void onResult(String status, String username, int position) {

                if (status.equals("confirm")) {

                    Task<Void> t1 = FirebaseFirestore.getInstance().collection("POSTSANDSTORIES").document(prevalentuser.ourData.getUsername()).collection("FOLLOWERS")
                            .document(username).set(new HashMap<>());
                    Task<Void> t2 = FirebaseFirestore.getInstance().collection("POSTSANDSTORIES").document(username).collection("FOLLOWING")
                            .document(prevalentuser.ourData.getUsername()).set(new HashMap<>());

                    Task<Void> t3 = FirebaseFirestore.getInstance().collection("POSTSANDSTORIES").document(prevalentuser.ourData.getUsername()).collection("REQUESTS")
                            .document(username).delete();

                    Tasks.whenAllSuccess(t1, t2, t3).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> objects) {
                            prevalentuser.requests.remove(position);
                            RequestsAdapterUtil.getAdapter().notifyDataSetChanged();
                        }
                    });

                } else {

                    FirebaseFirestore.getInstance().collection("POSTSANDSTORIES").document(prevalentuser.ourData.getUsername()).collection("REQUESTS")
                            .document(username).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            prevalentuser.requests.remove(position);
                            RequestsAdapterUtil.getAdapter().notifyDataSetChanged();
                        }
                    });

                }

            }
        }, new RequestsAdapter.OpenProfile() {
            @Override
            public void onResult(String username) {
                Bundle args = new Bundle();
                OtherUserProfileFragment fragment = new OtherUserProfileFragment();

                args.putString("otherUser", username);
                fragment.setArguments(args);

                fragmentActivity.getSupportFragmentManager().beginTransaction().addToBackStack(null).
                        replace(R.id.homepage_framelayout, fragment).commit();
            }
        });

    }


    public static void setContext(FragmentActivity fa) {
        fragmentActivity = fa;
    }

    public static RequestsAdapter getAdapter() {
        return requestsAdapter;
    }
}
