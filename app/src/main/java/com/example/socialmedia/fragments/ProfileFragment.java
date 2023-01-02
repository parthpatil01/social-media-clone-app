package com.example.socialmedia.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmedia.R;
import com.example.socialmedia.SettingsActivity;
import com.example.socialmedia.adapters.EditProfileAdapter;
import com.example.socialmedia.prevalent.prevalentuser;
import com.example.socialmedia.registration.RegisterActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private Toolbar profileToolbar;
    private ImageView profileMenu, profilePicture;
    private AppBarLayout appBarLayout;
    private TextView settings, logoutButton, usernameText, posts, followers, following, bio, name;
    private View bottomSheetView;
    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView recyclerView;
    private SinglePostFragment spf;
    private Bundle args;
    private Button editProfile;
    private Handler handler;
    private Runnable runnable;
    private EditProfileFragment editProfileFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_fragement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        setData();



        spf = new SinglePostFragment();
        editProfileFragment = new EditProfileFragment();
        args = new Bundle();


        handler = new Handler();
        final int delay = 500; // 1000 milliseconds == 1 second

        runnable= new Runnable() {
            int counter=0;
            public void run() {
                System.out.println("profile fragment handler"+" "+counter);
                if(prevalentuser.ourData.getPostData()!=null || counter==5) {

                    setRecyclerView();
                    System.out.println("profile fragment handler stopped");

                }else{
                    handler.postDelayed(this,delay);
                    counter++;
                }
            }
        };

        handler.postDelayed(runnable, delay);

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.Theme_AppCompat_BottomSheetDialogTheme);
                bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet,
                        (LinearLayout) view.findViewById(R.id.bottomSheetContainer));
                logoutButton = (TextView) bottomSheetView.findViewById(R.id.log_out);
                settings = (TextView) bottomSheetView.findViewById(R.id.settings_dialog);
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                logOut();
                setUpSettings();
            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requireActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.homepage_framelayout, editProfileFragment).commit();
            }
        });

    }


    public void init(View view) {
        profileToolbar = view.findViewById(R.id.profile_toolbar);
        profileMenu = (ImageView) profileToolbar.getChildAt(1);
        appBarLayout = view.findViewById(R.id.collapseToolbar);
        editProfile = appBarLayout.findViewById(R.id.editProfile);
        usernameText = view.findViewById(R.id.username_profrag);
        posts = appBarLayout.findViewById(R.id.posts);
        followers = appBarLayout.findViewById(R.id.followers);
        following = appBarLayout.findViewById(R.id.following);
        recyclerView = view.findViewById(R.id.editProfileRecycler);
        profilePicture = view.findViewById(R.id.profile_fragment_image);
        name = view.findViewById(R.id.profile_fragment_name);
        bio = view.findViewById(R.id.profile_fragment_bio);

    }

    private void logOut() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                bottomSheetDialog.cancel();
                Intent intent = new Intent(getContext(), RegisterActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void setUpSettings() {
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                bottomSheetDialog.cancel();
            }
        });
    }

    public void setData() {
        usernameText.setText(prevalentuser.ourData.getUsername());
        followers.setText(prevalentuser.followers);
        following.setText(prevalentuser.following);

        Glide.with(getContext()).load(prevalentuser.ourData.getProfilePic()).placeholder(R.drawable.profile).into(profilePicture);

        String n = prevalentuser.ourData.getName();
        String b = prevalentuser.ourData.getBio();

        if (n != null && !n.equals("")) {
            name.setVisibility(View.VISIBLE);
            name.setText(prevalentuser.ourData.getName());
        }
        if (b != null && !b.equals("")) {
            bio.setVisibility(View.VISIBLE);
            bio.setText(prevalentuser.ourData.getBio());
        }
    }


    private void setRecyclerView() {
        posts.setText(prevalentuser.ourData.getTotalPosts());

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(true);

        EditProfileAdapter editProfileAdapter = new EditProfileAdapter(prevalentuser.ourData, getContext(), new EditProfileAdapter.OpenPost() {
            @Override
            public void openPost(String Id, String imageurl, String caption) {

//                args.putString("position", String.valueOf(position));
                args.putString("imageurl", imageurl);
                args.putString("caption", caption);
                args.putString("username", prevalentuser.ourData.getUsername());
                args.putString("postId", Id);

                spf.setArguments(args);
                requireActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.homepage_framelayout, spf).commit();

            }
        });
        recyclerView.setAdapter(editProfileAdapter);


    }


}