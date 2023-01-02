package com.example.socialmedia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.socialmedia.R;
import com.example.socialmedia.models.StoriesModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    private List<String> mList;
    private Context mContext;

    public ChatAdapter(List<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.chat_message_layout,parent,false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        holder.msg.setText(mList.get(position));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{
        public TextView msg;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            msg=itemView.findViewById(R.id.chat_msg_layout_txt);
        }

    }



}
