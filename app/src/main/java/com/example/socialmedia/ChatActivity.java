package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialmedia.adapters.ChatAdapter;
import com.example.socialmedia.prevalent.prevalentuser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ImageView profile,send;
    RecyclerView recyclerView;
    TextView username;
    EditText message;
    FirebaseFirestore firestore;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();

        Intent intent = getIntent();
        String receiver=intent.getStringExtra("receiver");
        username.setText(receiver);

        String ourUsername=prevalentuser.ourData.getUsername();

        LinearLayoutManager feedLayoutManager = new LinearLayoutManager(this);
        feedLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(feedLayoutManager);

        ArrayList<String> list=new ArrayList<>();
        ChatAdapter chatAdapter=new ChatAdapter(list,this);
        recyclerView.setAdapter(chatAdapter);


        firestore.collection("POSTSANDSTORIES").document(receiver).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String url=documentSnapshot.getString("profileUrl");

                Glide.with(getApplicationContext())
                        .load(url)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(profile);

                db.child(prevalentuser.ourData.getUsername()).child(receiver).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot data:snapshot.getChildren()){
                            list.add(data.getValue().toString());
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=message.getText().toString();
                if(!msg.isEmpty()){

                    Calendar calendar = Calendar.getInstance();
                    String s=String.valueOf(calendar.getTimeInMillis());
                    String newMsg=ourUsername+": "+msg;
                    db.child(ourUsername).child(receiver).child(s).setValue(newMsg);
                    db.child(receiver).child(ourUsername).child(s).setValue(newMsg);
                    message.getText().clear();

                }
            }
        });

    }

    void init(){

        profile=findViewById(R.id.chat_profile);
        username=findViewById(R.id.chat_username);
        message=findViewById(R.id.chat_msg);
        send=findViewById(R.id.chat_send);
        firestore=FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.chats_rv);
        db=FirebaseDatabase.getInstance().getReference();

    }


}