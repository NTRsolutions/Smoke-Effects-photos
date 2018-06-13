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

import com.dvinfosys.smokeeffectphotomaker.Response.VerticalMoreAppsResponse;

import java.util.List;

/**
 * Created by vaksys-1 on 31/7/17.
 */

public class VerticalMoreAppsRecyclerViewAdapter extends RecyclerView.Adapter<VerticalMoreAppsRecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<VerticalMoreAppsResponse.CategoryEntity> row;
    Animation animFadeIn, animFadeOut;


    public VerticalMoreAppsRecyclerViewAdapter(AppHub appHub, List<VerticalMoreAppsResponse.CategoryEntity> data) {
        this.context = appHub;
        this.row = data;
    }

    @Override
    public VerticalMoreAppsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_recyclerview_item, parent, false);
        return new VerticalMoreAppsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VerticalMoreAppsRecyclerViewAdapter.MyViewHolder holder, final int position) {

        Glide.with(context).load(row.get(position).getImage()).into(holder.verticalAppImages);
        holder.verticalAppsName.setText(row.get(position).getName());

//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
//        animation.setDuration(500);
//        holder.itemView.startAnimation(animation);

        holder.verticalDownloadBtn.setOnClickListener(new View.OnClickListener() {
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

        private final ImageView verticalAppImages;
        private final TextView verticalAppsName;
        private final Button verticalDownloadBtn;

        public MyViewHolder(View itemView) {
            super(itemView);

            verticalAppImages = (ImageView) itemView.findViewById(R.id.vertical_apps_imgs_iv);
            verticalAppsName = (TextView) itemView.findViewById(R.id.vertical_apps_name_tv);
            verticalDownloadBtn = (Button) itemView.findViewById(R.id.vertical_download_btn);
        }
    }
}
