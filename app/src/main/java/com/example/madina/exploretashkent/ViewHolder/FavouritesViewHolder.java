package com.example.madina.exploretashkent.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.madina.exploretashkent.Interface.ItemClickListener;
import com.example.madina.exploretashkent.R;

/**
 * Created by Madina on 4/2/2018.
 */

public class FavouritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView itemName, itemPhone;
    public ImageView itemImage, delete;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private ItemClickListener itemClickListener;

    public FavouritesViewHolder(View itemView) {

        super(itemView);

        itemName = (TextView)itemView.findViewById(R.id.item_Name);
        itemImage = (ImageView) itemView.findViewById(R.id.item_Image);
        delete = (ImageView)itemView.findViewById(R.id.delete);
        //itemPhone = (TextView) itemView.findViewById(R.id.item_phone);


        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.OnClick(view, getAdapterPosition(), false);
    }
}
