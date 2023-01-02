package com.example.socialmedia.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmedia.R;
import com.example.socialmedia.models.LikesFollowersFollowing;

import java.util.List;

public class ViewCommentsAdapter extends RecyclerView.Adapter<ViewCommentsAdapter.ViewCommentsViewHolder> {

    private Context context;
    private List<LikesFollowersFollowing> list;
    private CommentSelected cs;


    public ViewCommentsAdapter(Context context, List<LikesFollowersFollowing> list, CommentSelected cs) {
        this.context = context;
        this.list = list;
        this.cs = cs;
    }

    @NonNull
    @Override
    public ViewCommentsAdapter.ViewCommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments_item, parent, false);
        return new ViewCommentsAdapter.ViewCommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCommentsAdapter.ViewCommentsViewHolder holder, int position) {
        LikesFollowersFollowing l = list.get(position);
        if (!l.getComment().isEmpty()) {
            holder.username.setText(l.getUsername());
            holder.comment.setText(l.getComment());
            Glide.with(context).load(l.getProfileUrl()).centerCrop().into(holder.profile);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewCommentsViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile;
        public TextView username, comment;

        public ViewCommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.comments_id);
            username = itemView.findViewById(R.id.comments_username);
            comment = itemView.findViewById(R.id.comments_view_item);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int p = getLayoutPosition();
                    cs.onLongCommentSelected(p,view);
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int p=getLayoutPosition();
                    cs.onCommentSelected(p,view);
                }
            });

        }
    }

    public interface CommentSelected {
        void onLongCommentSelected(int position,View view);
        void onCommentSelected(int position,View view);
    }

}

