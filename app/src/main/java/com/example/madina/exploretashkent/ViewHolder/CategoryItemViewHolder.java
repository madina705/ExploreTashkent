package com.example.madina.exploretashkent.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madina.exploretashkent.Interface.ItemClickListener;
import com.example.madina.exploretashkent.R;

/**
 * Created by Madina on 2/5/2018.
 */

public class CategoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView itemName;
    public ImageView itemImage, favImage;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private ItemClickListener itemClickListener;

    public CategoryItemViewHolder(View itemView) {

        super(itemView);

        itemName = (TextView)itemView.findViewById(R.id.underCategory_name);
        itemImage = (ImageView) itemView.findViewById(R.id.underCategory_image);
        favImage = (ImageView) itemView.findViewById(R.id.fav);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.OnClick(view, getAdapterPosition(), false);
    }
}
