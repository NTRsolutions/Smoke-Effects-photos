package com.dvinfosys.smokeeffectphotomaker.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dvinfosys.smokeeffectphotomaker.Activity.EditImage;
import com.dvinfosys.smokeeffectphotomaker.R;


import java.util.ArrayList;

/**
 * Created by vaksys-1 on 21/6/17.
 */

public class StickerRecyclerViewAdapter extends RecyclerView.Adapter<StickerRecyclerViewAdapter.MyViewHolder>{

    Context context;
    private ArrayList<Integer> effectImageList;
    public StickerRecyclerViewAdapter(EditImage editImage, ArrayList<Integer> effectImageList) {
        this.context = editImage;
        this.effectImageList = effectImageList;
    }

    @Override
    public StickerRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewGroup group = (ViewGroup) layoutInflater.inflate(R.layout.recycle_adapter, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(group);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(StickerRecyclerViewAdapter.MyViewHolder holder, int position) {
        final Integer name = effectImageList.get(position);
        holder.recyclerImgItem.setBackgroundResource(name);

    }

    @Override
    public int getItemCount() {
        return effectImageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView recyclerImgItem;
        private ItemClickListener mListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            recyclerImgItem = (ImageView)itemView.findViewById(R.id.recycler_item_img);
            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener listener) {
            this.mListener = listener;
        }

        @Override
        public void onClick(View v) {

        }
    }
    public interface ItemClickListener {
        void onClickItem(int pos);

    }
}
