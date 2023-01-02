package com.example.socialmedia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmedia.R;
import com.example.socialmedia.models.Requests;

import java.util.ArrayList;
import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestsViewHolder> {


    private List<Requests> list;
    private ConfirmDelete confirmDelete;
    private OpenProfile op;

    public RequestsAdapter(List<Requests> list,ConfirmDelete confirmDelete,OpenProfile op) {

        this.list = list;
        this.confirmDelete=confirmDelete;
        this.op=op;
    }

    @NonNull
    @Override
    public RequestsAdapter.RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);

        return new RequestsAdapter.RequestsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RequestsAdapter.RequestsViewHolder holder, int position) {

        String username=list.get(position).getUsername();

        Glide.with(holder.itemView.getContext()).load(list.get(position).getProfilePic()).centerCrop().into(holder.profile);
        holder.username.setText(username);

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op.onResult(username);
            }
        });

        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirmDelete.onResult("confirm",username,position);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete.onResult("delete",username,position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RequestsViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile;
        public TextView username;
        public Button confirm, delete;

        public RequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.request_profile);
            username = itemView.findViewById(R.id.request_username);
            confirm = itemView.findViewById(R.id.request_confirm);
            delete = itemView.findViewById(R.id.request_delete);
        }
    }

    public interface ConfirmDelete{
        void onResult(String status,String username,int position);
    }

    public interface OpenProfile{
        void onResult(String username);
    }
}
