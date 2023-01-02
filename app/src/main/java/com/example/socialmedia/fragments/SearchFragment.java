package com.example.socialmedia.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.R;
import com.example.socialmedia.adapters.SearchAdapter;
import com.example.socialmedia.prevalent.prevalentuser;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private SinglePostFragment spf;
    private Bundle args;
    private LinearLayout search;
    private Handler handler;
    private Runnable runnable;
    private LinearLayout linearLayout;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);



        handler = new Handler();
        final int delay = 500; // 1000 milliseconds == 1 second

        runnable= new Runnable() {
            int counter=0;
            public void run() {
                System.out.println("search fragment handler"+" "+counter);
                if(!prevalentuser.feed.isEmpty() || counter==5) {

                    setAdapeter();
                    System.out.println("search fragment handler stopped");

                }else{
                    handler.postDelayed(this,delay);
                    counter++;
                }
            }
        };

        handler.postDelayed(runnable, delay);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.homepage_framelayout, new SearchViewFragment()).commit();
            }
        });
    }

    private void setAdapeter() {

        spf = new SinglePostFragment();
        args = new Bundle();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        searchAdapter = new SearchAdapter(prevalentuser.feed, getContext(), new SearchAdapter.OpenPost() {
            @Override
            public void openPost(String Id, String imageurl, String caption, String uname) {


                args.putString("imageurl", imageurl);
                args.putString("caption", caption);
                args.putString("username", uname);
                args.putString("postId", Id);

                spf.setArguments(args);


                requireActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.homepage_framelayout, spf).commit();

            }
        });
        linearLayout.setVisibility(View.GONE);
        recyclerView.setAdapter(searchAdapter);

    }


    private void init(View view) {
        recyclerView = view.findViewById(R.id.search_recycler);
        search = view.findViewById(R.id.tap_to_search);
        linearLayout=view.findViewById(R.id.search_frag_progress);
    }


}