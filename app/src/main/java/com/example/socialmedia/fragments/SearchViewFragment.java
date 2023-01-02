package com.example.socialmedia.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.socialmedia.R;
import com.example.socialmedia.models.UsersData;
import com.example.socialmedia.utils.FirebaseUtil;

public class SearchViewFragment extends Fragment {

    private SearchView searchView;
    private TextView usernameText;
    private ImageView profileImage;
    private LinearLayout user;

    public SearchViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        final SearchManager searchManager = (SearchManager) getActivity().getSystemService(getContext().SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setIconifiedByDefault(false);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showInputMethod(view.findFocus());
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                FirebaseUtil.searchProfile(query, new FirebaseUtil.FirebaseInterface() {
                    @Override
                    public void onFirebaseRetrieveComplete(UsersData usersData) {

                        user.setVisibility(View.VISIBLE);
                        usernameText.setText(query);
                        Glide.with(getContext()).load(usersData.getProfilePic()).placeholder(R.drawable.profile).into(profileImage);

                        user.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle args = new Bundle();
                                OtherUserProfileFragment fragment = new OtherUserProfileFragment();

                                args.putString("otherUser", usernameText.getText().toString());
                                fragment.setArguments(args);

                                requireActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).
                                        replace(R.id.homepage_framelayout, fragment).commit();
                            }
                        });
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void init(View view) {
       searchView = view.findViewById(R.id.search_username);
       usernameText=view.findViewById(R.id.search_profile_username);
       profileImage=view.findViewById(R.id.search_profile_image);
       user=view.findViewById(R.id.user_search_search);
    }

    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

}