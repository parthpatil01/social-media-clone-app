package com.example.socialmedia.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmedia.R;
import com.example.socialmedia.adapters.EditProfileAdapter;
import com.example.socialmedia.models.UsersData;
import com.example.socialmedia.prevalent.prevalentuser;
import com.example.socialmedia.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OtherUserProfileFragment extends Fragment {

    private static final String TAG = "OtheUserProfileFragment";
    private ImageView backButton, profile;
    private TextView username, posts, followers, following,ofName,ofBio;
    private Button privateFollowButton, followingButton;
    private LinearLayout privateAccount, follownigAndMessage, otherUserProgress,nameAndBio;
    private RecyclerView recyclerView;
    private SinglePostFragment spf;
    private Bundle args;
    private String uname, ourUsername;
    private UsersData ud;
    public Handler handler;
    public Runnable runnable;
    private Dialog dialog;
    private BottomSheetDialog bottomSheetDialog;
    private View bottomSheetView;
    private FirebaseFirestore firestore;
    private Button BUnfollow;

    public OtherUserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_other_user_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//        dialog.setContentView(R.layout.dialog_loading);
//        dialog.setCancelable(false);
//        dialog.show();

        uname = getArguments().getString("otherUser");
        ourUsername = prevalentuser.ourData.getUsername();
        init(view);

        args = new Bundle();
        spf = new SinglePostFragment();
        ud = new UsersData();


        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFollowUnfollow();

            }
        });

        privateFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                privateDocumentClick();

            }
        });

        getData();

    }


    private void getData() {
        firestore = FirebaseFirestore.getInstance();

        FirebaseUtil.firebaseRetriveData(uname, new FirebaseUtil.FirebaseInterface() {
            @Override
            public void onFirebaseRetrieveComplete(UsersData usersData) {

                setRecycler(usersData);
                posts.setText(usersData.getTotalPosts());
                followers.setText(usersData.getFollowers());

                if(usersData.getName()!=null || usersData.getBio()!=null){
                    nameAndBio.setVisibility(View.VISIBLE);
                    ofName.setText(usersData.getName());
                    ofBio.setText(usersData.getBio());
                }
                Glide.with(getContext()).load(usersData.getProfilePic()).placeholder(R.color.lightest_grey).into(profile);
            }

        }, 50);


        Task<DocumentSnapshot> task3 = firestore.collection("POSTSANDSTORIES").document(uname).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.getString("status").equals("open")) {
                    openUsernameExists();
                } else
                    privateUsernameExists();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }
        });


        username.setText(uname);

    }


    private void openUsernameExists() {

        firestore.collection("POSTSANDSTORIES").document(uname).collection("FOLLOWERS").document(ourUsername).
                get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                privateAccount.setVisibility(View.GONE);
                follownigAndMessage.setVisibility(View.VISIBLE);

                if (documentSnapshot.exists()) {
                    // we are a follower
                    showFollowingButton();
                } else {
                    // not a follower
                    showFollowButton();
                }
//                dialog.cancel();
                otherUserProgress.setVisibility(View.GONE);
            }

        });

    }

    private void openFollowUnfollow() {
        bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.Theme_AppCompat_BottomSheetDialogTheme);
        bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.unfollow,
                null);
        bottomSheetDialog.setContentView(bottomSheetView);

        firestore.collection("POSTSANDSTORIES").document(uname).collection("FOLLOWERS").document(ourUsername).
                get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {



                    bottomSheetDialog.show();

                    BUnfollow = bottomSheetView.findViewById(R.id.bottom_sheet_unfollow);
                    BUnfollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            firestore.collection("POSTSANDSTORIES").document(uname).collection("FOLLOWERS").document(ourUsername).delete();
                            firestore.collection("POSTSANDSTORIES").document(ourUsername).collection("FOLLOWING").document(uname).delete();

                            bottomSheetDialog.cancel();

                            handler = new Handler();
                            final int delay = 500; // 1000 milliseconds == 1 second

                            runnable = new Runnable() {

                                public void run() {
                                    System.out.println("Other user fragment handler");

                                    OtherUserProfileFragment oupf = new OtherUserProfileFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("otherUser", uname);
                                    oupf.setArguments(bundle);

                                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homepage_framelayout, oupf).commit();

                                }
                            };

                            handler.postDelayed(runnable, delay);


                        }
                    });


                } else {

                    firestore.collection("POSTSANDSTORIES").document(uname).collection("FOLLOWERS").document(ourUsername)
                            .set(new HashMap<>());
                    firestore.collection("POSTSANDSTORIES").document(ourUsername).collection("FOLLOWING").document(uname)
                            .set(new HashMap<>());
                    showFollowingButton();

                }

            }

        });

    }

    private void privateUsernameExists() {


        firestore.collection("POSTSANDSTORIES").document(uname).collection("FOLLOWERS").document(ourUsername).
                get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                privateRequestExists();

            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                privateAccount.setVisibility(View.GONE);
                follownigAndMessage.setVisibility(View.VISIBLE);

                showFollowingButton();
//                dialog.cancel();
                otherUserProgress.setVisibility(View.GONE);
            }


        });

    }

    private void privateRequestExists() {

        follownigAndMessage.setVisibility(View.GONE);
        privateAccount.setVisibility(View.VISIBLE);

        firestore.collection("POSTSANDSTORIES").document(uname).collection("REQUESTS").document(ourUsername).
                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    showPrivateRequestButton();
                } else showPrivateFollowButton();
//                dialog.cancel();
                otherUserProgress.setVisibility(View.GONE);
            }
        });

    }

    private void privateDocumentClick() {


        firestore.collection("POSTSANDSTORIES").document(uname).collection("REQUESTS").document(ourUsername).
                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    firestore.collection("POSTSANDSTORIES").document(uname).collection("REQUESTS").document(ourUsername)
                            .delete();
                    showPrivateFollowButton();

                } else {

                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                    int strDate = Integer.parseInt(formatter.format(date));

                    Map<String, Integer> map = new HashMap<>();
                    map.put("date", strDate);

                    firestore.collection("POSTSANDSTORIES").document(uname).collection("REQUESTS").document(ourUsername)
                            .set(map);
                    showPrivateRequestButton();
                }
            }
        });


    }



    /*


     */

    private void showFollowingButton() {
        followingButton.setText("Following");
        followingButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        followingButton.setBackgroundResource(R.drawable.button);
    }

    private void showFollowButton() {
        followingButton.setText("Follow");
        followingButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        followingButton.setBackgroundResource(R.drawable.login_button);
    }

    private void showPrivateFollowButton() {
        privateFollowButton.setText("Follow");
        privateFollowButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        privateFollowButton.setBackgroundResource(R.drawable.login_button);
    }

    private void showPrivateRequestButton() {
        privateFollowButton.setText("Requested");
        privateFollowButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        privateFollowButton.setBackgroundResource(R.drawable.button);
    }


    private void setRecycler(UsersData ud) {

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(true);

        EditProfileAdapter editProfileAdapter = new EditProfileAdapter(ud, getContext(), new EditProfileAdapter.OpenPost() {
            @Override
            public void openPost(String id, String imageurl, String caption) {

                args.putString("imageurl", imageurl);
                args.putString("caption", caption);
                args.putString("username", uname);
                args.putString("postId", id);

                spf.setArguments(args);

//                requireActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
//                        .replace(R.id.homepage_framelayout, spf).commit();

                requireActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.homepage_framelayout, spf).commit();
            }
        });

        recyclerView.setAdapter(editProfileAdapter);


    }

    private void init(View view) {

        backButton = view.findViewById(R.id.other_profile_toolbar_back);
        privateFollowButton = view.findViewById(R.id.private_follow);
        followingButton = view.findViewById(R.id.following_button);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        username = view.findViewById(R.id.other_username_profrag);
        profile = view.findViewById(R.id.other_user_pp);
        privateAccount = view.findViewById(R.id.privateAccount);
        recyclerView = view.findViewById(R.id.otherProfileRecycler);
        follownigAndMessage = view.findViewById(R.id.following_message);
        otherUserProgress = view.findViewById(R.id.other_user_progress);

        ofName = view.findViewById(R.id.other_profile_name);
        ofBio = view.findViewById(R.id.other_profile_bio);
        nameAndBio=view.findViewById(R.id.name_and_bio);
    }
}