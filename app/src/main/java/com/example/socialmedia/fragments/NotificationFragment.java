package com.example.socialmedia.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmedia.R;
import com.example.socialmedia.adapters.RequestsAdapter;
import com.example.socialmedia.models.Requests;
import com.example.socialmedia.models.UsersData;
import com.example.socialmedia.prevalent.prevalentuser;
import com.example.socialmedia.utils.FeedAdapterUtil;
import com.example.socialmedia.utils.FirebaseUtil;
import com.example.socialmedia.utils.RequestsAdapterUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.notification_recycler);


        LinearLayoutManager feedLayoutManager = new LinearLayoutManager(getContext());
        feedLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(feedLayoutManager);

        recyclerView.setAdapter(RequestsAdapterUtil.getAdapter());

    }
}