package com.dvinfosys.smokeeffectphotomaker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dvinfosys.smokeeffectphotomaker.Activity.AppHub;
import com.dvinfosys.smokeeffectphotomaker.R;
import com.dvinfosys.smokeeffectphotomaker.Response.TopAppListResponse;


import java.util.List;

/**
 * Created by vaksys-1 on 3/7/17.
 */

public class TopAppsRecyclerAdapter extends RecyclerView.Adapter<TopAppsRecyclerAdapter.MyViewHolder> {

    Context context;
    List<TopAppListResponse.CategoryEntity> row;
    Animation animFadeIn, animFadeOut;


    public TopAppsRecyclerAdapter(AppHub appHub, List<TopAppListResponse.CategoryEntity> data) {
        this.context = appHub;
        this.row = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topapps_recyclerview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Glide.with(context).load(row.get(position).getImage()).into(holder.appImages);
        holder.appsName.setText(row.get(position).getName());
        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(row.get(position).getLink()));
                    context.startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(row.get(position).getLink()));
                    context.startActivity(intent);
                }
            }
        });

//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
//        animation.setDuration(500);
//        holder.itemView.startAnimation(animation);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(row.get(position).getLink()));
                    context.startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(row.get(position).getLink()));
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return row.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView appImages;
        private final TextView appsName;
        private final LinearLayout recyclerItemLayout;
        private final Button downloadBtn;

        public MyViewHolder(View itemView) {
            super(itemView);

            appImages = (ImageView) itemView.findViewById(R.id.apps_imgs_iv);
            appsName = (TextView) itemView.findViewById(R.id.apps_name_tv);
            downloadBtn = (Button) itemView.findViewById(R.id.download_btn);
            recyclerItemLayout = (LinearLayout) itemView.findViewById(R.id.recycler_item_layout);
        }
    }
}
