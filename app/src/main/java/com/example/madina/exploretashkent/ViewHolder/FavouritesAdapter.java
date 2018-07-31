package com.example.madina.exploretashkent.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.madina.exploretashkent.Database.Database;
import com.example.madina.exploretashkent.FavouritesActivity;
import com.example.madina.exploretashkent.Interface.ItemClickListener;
import com.example.madina.exploretashkent.ItemDetail;
import com.example.madina.exploretashkent.ListUnderCategory;
import com.example.madina.exploretashkent.Models.Favourites;
import com.example.madina.exploretashkent.Models.ItemUnderCategory;
import com.example.madina.exploretashkent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Madina on 2/21/2018.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesViewHolder> {

    private Context context;
    private List<Favourites> favouritesList;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FavouritesAdapter adapter;

    Database localDb;


    public FavouritesAdapter(Context context, List<Favourites> favouritesList) {
        this.context = context;
        this.favouritesList = favouritesList;
    }

    @Override
    public FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.favourites_item, parent, false);
        return new FavouritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavouritesViewHolder viewHolder, final int position) {


        Picasso.with(context).load(favouritesList.get(position).getItemImage())
                .into(viewHolder.itemImage);
        viewHolder.itemName.setText(favouritesList.get(position).getItemName());
//        viewHolder.itemPhone.setText(favouritesList.get(position).getItemPhone());

        final Favourites local = favouritesList.get(position);
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnClick(View view, int position, boolean isLongClick) {
                Intent itemDetail = new Intent(context, ItemDetail.class);
                itemDetail.putExtra("ItemId", local.getItemId()); //Sending item id to new activity
                context.startActivity(itemDetail);
            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localDb = new Database(context);
                localDb.removeFromFavourites(local.getItemId(), user.getUid());
                favouritesList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, favouritesList.size());

            }
        });


  }

    @Override
    public int getItemCount() {
        return favouritesList.size()    ;
    }


    public void removeItem(int position)
    {
        favouritesList.remove(position);
        notifyItemRemoved(position);
    }



    public void restoreItem(Favourites item, int position)
    {
        favouritesList.add(position, item);
        notifyItemInserted(position);
    }


}
