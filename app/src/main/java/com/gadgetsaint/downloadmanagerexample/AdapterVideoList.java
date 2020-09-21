package com.gadgetsaint.downloadmanagerexample;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;

import java.util.ArrayList;
import java.util.List;

public class AdapterVideoList extends RecyclerView.Adapter<AdapterVideoList.MyViewHolder> {
    List<Model> video_list = new ArrayList<>();
    MyOnClickListener listener;
    Context context;

    public AdapterVideoList(Context context, List<Model> list) {
        this.context = context;
        this.video_list = list;
        notifyDataSetChanged();
    }

    public void setListener(MyOnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video, viewGroup,
                false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Model current = video_list.get(i);
        myViewHolder.title.setText(current.getTitle());

        Glide.with(context)
                .asBitmap()
                .load(current.getUrl()) // or URI/path
                .thumbnail(0.1f)
                .override(50,50)
                .into(myViewHolder.image_view);

        myViewHolder.Bind(listener, current);
    }

    @Override
    public int getItemCount() {
        return video_list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, download, download_all;
        ImageView image_view;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            image_view = itemView.findViewById(R.id.image_view);
            download = itemView.findViewById(R.id.download);
            download_all = itemView.findViewById(R.id.download_all);

        }

        void Bind(MyOnClickListener listener, Model model) {

            download.setOnClickListener(v -> {
                listener.OnClick(model, "download");
            });

            download_all.setOnClickListener(v -> {
                listener.OnClick(model, "download all");
            });


        }
    }

    public interface MyOnClickListener {
        void OnClick(Model model, String mode);
    }

}
