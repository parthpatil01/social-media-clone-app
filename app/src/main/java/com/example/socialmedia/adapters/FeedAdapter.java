package com.example.socialmedia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmedia.Homepage;
import com.example.socialmedia.R;
import com.example.socialmedia.models.FeedModel;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private List<FeedModel> mList;
    private Context mContext;
    private SendToOtherUserFrag stou;
    private LikeOrRemove lor;
    private ViewLikes vl;
    private ViewComments vc;

    public FeedAdapter(List<FeedModel> mList, Context mContext, SendToOtherUserFrag stou,LikeOrRemove lor,ViewLikes vl,ViewComments vc) {
        this.mList = mList;
        this.mContext = mContext;
        this.stou = stou;
        this.lor=lor;
        this.vl=vl;
        this.vc=vc;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_view, parent, false);

        return new FeedViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.FeedViewHolder holder, int position) {

        FeedModel fm = mList.get(position);
        String username = fm.getUsername();
        holder.username.setText(username);
        holder.likes.setText(fm.getLikes() + " likes");
        String c = fm.getCaption();

        if (c == null) {
            holder.cv.setVisibility(View.GONE);
        } else {
            holder.userCap.setText(username);
            holder.caption.setText(c);
        }

        if(fm.isLiked()){
            holder.heart.setImageResource(R.drawable.like_red);
        }else holder.heart.setImageResource(R.drawable.like);

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stou.send(username);
            }
        });

        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lor.likeRemove(username,fm.getId(),holder,position);
            }
        });

        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vl.viewLikes(fm.getId(), username);
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vc.viewComments(fm.getId(),username);
            }
        });

        Glide.with(holder.itemView.getContext())
                .load(fm.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(holder.post);

    }

    @Override
    public int getItemCount() {

        return mList.size();

    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {

        public TextView username, likes, time, comments, userCap, caption;
        public ImageView post, heart;
        public LinearLayout cv;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.userName_single);
            likes = itemView.findViewById(R.id.view_likes);
            time = itemView.findViewById(R.id.time);
            comments = itemView.findViewById(R.id.comments);
            post = itemView.findViewById(R.id.myPost);
            userCap = itemView.findViewById(R.id.caption_username);
            caption = itemView.findViewById(R.id.user_caption);
            cv = itemView.findViewById(R.id.visible_if_cap);
            heart = itemView.findViewById(R.id.like_heart);
        }
    }


    public interface SendToOtherUserFrag {
        void send(String username);
    }

    public interface LikeOrRemove {
        void likeRemove(String username, String postId,FeedAdapter.FeedViewHolder holder,int position);
    }

    public interface ViewLikes{
        void viewLikes(String postId,String username);
    }

    public interface ViewComments{
        void viewComments(String postId,String username);
    }
}



/*







 */