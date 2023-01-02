package com.example.socialmedia.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmedia.R;
import com.example.socialmedia.SquareImage;
import com.example.socialmedia.models.UsersData;

import java.util.List;

public class EditProfileAdapter extends RecyclerView.Adapter<EditProfileAdapter.EPViewHolder> {

    private UsersData od;
    private int mWidth;
    private Context mContext;
    private OpenPost mOpenPost;


    public EditProfileAdapter(UsersData od, Context mContext,OpenPost openPost) {
        this.od = od;
        this.mContext = mContext;
        this.mOpenPost=openPost;
        getWidth();
    }

    @NonNull
    @Override
    public EPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.editprofileitem,parent,false);
        return new EPViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditProfileAdapter.EPViewHolder holder, int position) {


        holder.image.getLayoutParams().width=mWidth;
        holder.image.getLayoutParams().height=mWidth;

        UsersData.PostData pd=od.getPostData().get(position);

        String thumbnails=pd.getThumbnailUrl();
        String imageurl=pd.getImgUrl();
        String caption=pd.getCaption();


        Glide.with(mContext).load(thumbnails).placeholder(R.color.lightest_grey).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOpenPost.openPost(pd.getId(),imageurl,caption);
            }
        });

    }

    @Override
    public int getItemCount() {
        return od.getPostData().size();
    }

    public class EPViewHolder extends RecyclerView.ViewHolder{

        public SquareImage image;

        public EPViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.sqedimage);
        }
    }

    private void getWidth(){
        mWidth=mContext.getResources().getDisplayMetrics().widthPixels/3;
    }

    public interface OpenPost {

        void openPost(String Id,String imageurl, String caption);

    }

}
