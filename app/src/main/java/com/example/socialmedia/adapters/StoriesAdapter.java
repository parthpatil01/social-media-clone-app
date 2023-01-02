package com.example.socialmedia.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmedia.R;
import com.example.socialmedia.Stories;
import com.example.socialmedia.fragments.StoryFragment;
import com.example.socialmedia.models.StoriesModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesViewHolder> {

    private List<StoriesModel> mList;
    private Context mContext;

    public StoriesAdapter(List<StoriesModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.stories_item,parent,false);
        return new StoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesAdapter.StoriesViewHolder holder, int position) {

        StoriesModel user=mList.get(position);

        holder.username.setText(user.getUsername());

        holder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.itemView.getContext(), Stories.class);
                Stories.initPosition=position;
                holder.itemView.getContext().startActivity(intent);
            }
        });

        Glide.with(holder.itemView.getContext())
                .load(user.getProfileUrl())
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(holder.profilePic);



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class StoriesViewHolder  extends RecyclerView.ViewHolder{

        public ImageView profilePic; // previous circle image view
        public TextView username;

        public StoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic=itemView.findViewById(R.id.stories_profile_image);
            username=itemView.findViewById(R.id.stories_username);

        }
    }

}
