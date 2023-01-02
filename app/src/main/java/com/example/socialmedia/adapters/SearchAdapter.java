package com.example.socialmedia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmedia.R;
import com.example.socialmedia.models.FeedModel;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder> {

    private List<FeedModel> data;
    private Context context;
    private int width;
    private OpenPost open;

    public SearchAdapter(List<FeedModel> data, Context context, OpenPost open) {
        this.data = data;
        this.context = context;
        calculateWidth();
        this.open = open;
    }

    @NonNull
    @Override
    public SearchAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new SearchAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchAdapterViewHolder holder, int position) {
        holder.post.getLayoutParams().height = width;
        holder.post.getLayoutParams().width = width;

        FeedModel ud = data.get(position);

        String thumbnail = ud.getThumbnailUrl();
        String imageurl = ud.getImageUrl();
        String caption = ud.getCaption();

        Glide.with(context).load(thumbnail).centerCrop().into(holder.post);

        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open.openPost(ud.getId(), imageurl, caption, ud.getUsername());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SearchAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView post;

        public SearchAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            post = itemView.findViewById(R.id.post);
        }
    }

    private void calculateWidth() {
        width = context.getResources().getDisplayMetrics().widthPixels / 3;
    }

    public interface OpenPost {

        void openPost(String Id, String imageurl, String caption, String uname);

    }
}
