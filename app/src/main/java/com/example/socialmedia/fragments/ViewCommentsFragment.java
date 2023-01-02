package com.example.socialmedia.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.R;
import com.example.socialmedia.adapters.ViewCommentsAdapter;
import com.example.socialmedia.models.LikesFollowersFollowing;
import com.example.socialmedia.prevalent.prevalentuser;
import com.example.socialmedia.utils.FirebaseUtil;

import java.util.List;


public class ViewCommentsFragment extends Fragment {

    private RecyclerView rv;
    private LinearLayout viewComments;
    private RelativeLayout clickToCancel;
    private ViewCommentsAdapter vcad;
    private TextView postComment;
    private EditText comment;
    private List<LikesFollowersFollowing> listCom;
    private int pos = -1;
    private View commentView;
    private String ourUsername;
    private ImageView delete;
    private String postId, username;

    public ViewCommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_comments, container, false);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(linearLayout);


        postId = getArguments().getString("postId");
        username = getArguments().getString("username");
        ourUsername = prevalentuser.ourData.getUsername();

        FirebaseUtil.getTotalComments(postId, username, new FirebaseUtil.TotalComments() {
            @Override
            public void onResult(List<LikesFollowersFollowing> list) {
                listCom = list;
                System.out.println("list "+list);
                Handler handler = new Handler();
                final int delay = 500; // 1000 milliseconds == 1 second

                Runnable runnable = new Runnable() {
                    int counter = 0;

                    public void run() {
                        if (list.isEmpty() && counter != 5) {
                            System.out.println("view comments handler running");
                            counter++;
                            handler.postDelayed(this, delay);
                        } else {

                            viewComments.setVisibility(View.GONE);
                            handler.removeCallbacks(this);
                            vcad = new ViewCommentsAdapter(getContext(), list, new ViewCommentsAdapter.CommentSelected() {
                                @Override
                                public void onLongCommentSelected(int position, View view1) {
                                    System.out.println("long comment");
                                    if (pos == -1) {
                                        pos = position;
                                        commentView = view1;
                                        view1.setBackgroundColor(getResources().getColor(R.color.light_blue));

                                        checkIfuser();

                                    } else {
                                        pos = -1;
                                        commentView.setBackgroundResource(0);
                                        delete.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCommentSelected(int position, View view) {
                                    System.out.println("short comment");
                                    pos = -1;
                                    if (commentView != null) {
                                        commentView.setBackgroundResource(0);
                                        delete.setVisibility(View.GONE);
                                    }
                                }
                            });

                            rv.setAdapter(vcad);
                        }
                    }
                };

                handler.postDelayed(runnable, delay);


            }
        });


        rv.setOnTouchListener((view12, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                System.out.println(" motion action here");
                pos = -1;
                if (commentView != null) {
                    commentView.setBackgroundResource(0);
                    delete.setVisibility(View.GONE);
                }
            }
            return false;
        });


        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String s = comment.getText().toString();

                if (!s.equals("")) {
                    FirebaseUtil.postComment(username, postId, comment.getText().toString(), new FirebaseUtil.PostComments() {
                        @Override
                        public void onResult() {

                            LikesFollowersFollowing l = new LikesFollowersFollowing();
                            l.setComment(s);
                            comment.getText().clear();
                            l.setUsername(ourUsername);
                            l.setProfileUrl(prevalentuser.ourData.getProfilePic());

                            listCom.add(0, l);
                            vcad.notifyItemInserted(0);

                        }
                    });
                } else Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void checkIfuser() {

        if (ourUsername.equals(((TextView) commentView.findViewById(R.id.comments_username)).getText().toString())) {
            String comment = ((TextView) commentView.findViewById(R.id.comments_view_item)).getText().toString();
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listCom.remove(pos);
                    vcad.notifyItemRemoved(pos);
                    FirebaseUtil.deleteComment(username, postId, comment);
                    commentView.setBackgroundResource(0);
                    pos = -1;
                    delete.setVisibility(View.GONE);

                }
            });
        }

    }

    private void init(View view) {
        rv = view.findViewById(R.id.view_commets_fragment_recycler);
        viewComments = view.findViewById(R.id.view_comments_progress);
        postComment = view.findViewById(R.id.post_comment);
        comment = view.findViewById(R.id.post_comment_edittext);
//        clickToCancel = view.findViewById(R.id.click_to_cancel);
        delete = view.findViewById(R.id.other_profile_toolbar_delete);
    }
}