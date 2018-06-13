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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dvinfosys.smokeeffectphotomaker.R;
import com.dvinfosys.smokeeffectphotomaker.Response.ExitDialogOnlineResponse;

import java.util.List;

/**
 * Created by vaksys-1 on 2/8/17.
 */

public class ExitRecyclerViewAdapter extends RecyclerView.Adapter<ExitRecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<ExitDialogOnlineResponse.CategoryEntity> row;


    public ExitRecyclerViewAdapter(Context settings, List<ExitDialogOnlineResponse.CategoryEntity> data) {
        this.context = settings;
        this.row = data;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exit_app_recyclerview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Glide.with(context).load(row.get(position).getImage()).into(holder.appImages);
        holder.appsName.setText(row.get(position).getName());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animation.setDuration(500);
        holder.itemView.startAnimation(animation);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(row.get(position).getLink()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(row.get(position).getLink()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

        public MyViewHolder(View itemView) {
            super(itemView);

            appImages = (ImageView) itemView.findViewById(R.id.apps_imgs_iv);
            appsName = (TextView) itemView.findViewById(R.id.apps_name_tv);
        }
    }
}
