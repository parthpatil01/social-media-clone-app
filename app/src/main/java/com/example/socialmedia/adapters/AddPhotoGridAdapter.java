package com.example.socialmedia.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmedia.R;

import java.util.ArrayList;

public class AddPhotoGridAdapter extends RecyclerView.Adapter<AddPhotoGridAdapter.gridViewHolder> {

    private Context mContext;
    private int deviceWidth;
    private ArrayList<String> arrayList;
    private selectImage select;


    public AddPhotoGridAdapter(Context mContext, ArrayList<String> arrayList,selectImage select) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.select=select;
    }

    @NonNull
    @Override
    public gridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.addphotogridadapter,parent,false);
        displayMetrics();
        return new gridViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AddPhotoGridAdapter.gridViewHolder holder, int position) {

        holder.gridImage.getLayoutParams().width=deviceWidth;
        holder.gridImage.getLayoutParams().height=deviceWidth;
        Glide.with(mContext).load(arrayList.get(position)).centerCrop().into(holder.gridImage);

        holder.gridImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.imgClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class gridViewHolder extends RecyclerView.ViewHolder{

        public ImageView gridImage;

        public gridViewHolder(@NonNull View itemView) {
            super(itemView);
            gridImage=itemView.findViewById(R.id.gridImage);
        }
    }

    public void displayMetrics(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        deviceWidth = displaymetrics.widthPixels / 4;
    }

    public interface selectImage {
        void imgClick(int position);
    }

}
