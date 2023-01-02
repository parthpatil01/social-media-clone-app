package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.example.socialmedia.adapters.ChatAdapter;
import com.example.socialmedia.adapters.SendMessageAdapter;
import com.example.socialmedia.fragments.OtherUserProfileFragment;
import com.example.socialmedia.models.UsersData;
import com.example.socialmedia.prevalent.prevalentuser;
import com.example.socialmedia.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SendMessage extends AppCompatActivity {

    private SearchView searchView;
    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        init();

        final SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

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

        LinearLayoutManager feedLayoutManager = new LinearLayoutManager(this);
        feedLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(feedLayoutManager);

        ArrayList<String[]> list=new ArrayList<>();
        SendMessageAdapter smAdapter=new SendMessageAdapter(list,this);
        recyclerView.setAdapter(smAdapter);

        ArrayList<String> l1=new ArrayList<>();

        db.child(prevalentuser.ourData.getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                l1.clear();
                list.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    l1.add(dataSnapshot.getKey());
                }

                for(String s:l1){
                    firestore.collection("POSTSANDSTORIES").document(s).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String[] sArray=new String[2];
                            sArray[0]=s;
                            sArray[1]=documentSnapshot.getString("profileUrl");
                            list.add(sArray);
                            smAdapter.notifyDataSetChanged();
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Task<DocumentSnapshot> s=firestore.collection("POSTSANDSTORIES").document(query).get();
                s.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                            intent.putExtra("receiver",query);
                            startActivity(intent);

                        }
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

    void init(){

        searchView=findViewById(R.id.sm_search);
        firestore=FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.sm_recycler_view);
        db= FirebaseDatabase.getInstance().getReference();

    }

    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }
}