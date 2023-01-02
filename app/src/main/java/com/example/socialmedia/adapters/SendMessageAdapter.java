package com.example.socialmedia.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmedia.ChatActivity;
import com.example.socialmedia.R;

import java.util.List;

public class SendMessageAdapter extends RecyclerView.Adapter<SendMessageAdapter.SendMessageViewHolder>{

    private List<String[]> mList;
    private Context mContext;

    public SendMessageAdapter(List<String[]> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SendMessageAdapter.SendMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.send_message_layout,parent,false);
        return new SendMessageAdapter.SendMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SendMessageAdapter.SendMessageViewHolder holder, int position) {

        String usr=mList.get(position)[0];
        holder.username.setText(usr);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("receiver",usr);
                mContext.startActivity(intent);
            }
        });

        Glide.with(holder.itemView.getContext())
                .load(mList.get(position)[1])
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(holder.pfp);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SendMessageViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView pfp;
        public LinearLayout layout;

        public SendMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.sm_profile_username);
            pfp=itemView.findViewById(R.id.sm_profile_image);
            layout=itemView.findViewById(R.id.sm_linear_container);

        }

    }
}
