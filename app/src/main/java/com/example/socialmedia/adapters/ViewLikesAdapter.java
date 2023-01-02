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

public class ViewLikesAdapter extends RecyclerView.Adapter<ViewLikesAdapter.ViewLikesViewHolder> {

    private Context context;
    private List<LikesFollowersFollowing> list;

    public ViewLikesAdapter(Context context, List<LikesFollowersFollowing> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewLikesAdapter.ViewLikesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.liked_by, parent, false);
        return new ViewLikesAdapter.ViewLikesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewLikesAdapter.ViewLikesViewHolder holder, int position) {
        LikesFollowersFollowing l=list.get(position);
        holder.username.setText(l.getUsername());
        holder.name.setText(l.getName());
        Glide.with(context).load(l.getProfileUrl()).centerCrop().into(holder.profile);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewLikesViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile;
        public TextView username, name;

        public ViewLikesViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.liked_by_username);
            username = itemView.findViewById(R.id.username_liked_by);
            name=itemView.findViewById(R.id.name_liked_by);

        }
    }


}
