package com.example.madina.exploretashkent;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madina.exploretashkent.Database.Database;
import com.example.madina.exploretashkent.Interface.ItemClickListener;
import com.example.madina.exploretashkent.Models.*;
import com.example.madina.exploretashkent.ViewHolder.CategoryItemViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import  com.example.madina.exploretashkent.Models.Favourites;

import java.util.ArrayList;
import java.util.List;

public class ListUnderCategory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    FirebaseDatabase database;
    DatabaseReference categoryItemList;
    DatabaseReference users;

    String categoryId = "";
    String modelName = "";

    TextView loggedUserEmail;
    TextView loggedUserName;

    FirebaseRecyclerAdapter<ItemUnderCategory, CategoryItemViewHolder> adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    //FAVOURITES
    Database localDB;


    //Search Functionality
    FirebaseRecyclerAdapter<ItemUnderCategory, CategoryItemViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_under_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.underCatogory_toolbar);


        //Firebase
        database = FirebaseDatabase.getInstance();
        categoryItemList = database.getReference("UnderCategories");
        users = database.getReference("Users");

        //LOCAL DATABASE
        localDB = new Database(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        loggedUserName = (TextView)headerView.findViewById(R.id.txtLoggedUserName);
        loggedUserEmail = (TextView)headerView.findViewById(R.id.txtLoggedUserEmail);

        if(user == null) {
            loggedUserName.setText("User Email");
            loggedUserEmail.setText("User Email");

        } else {
            users.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    loggedUserName.setText(snapshot.child("name").getValue().toString());
                    loggedUserEmail.setText(snapshot.child("email").getValue().toString());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }

        recyclerView = (RecyclerView)findViewById(R.id.recycler_listUnderCategory);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent here
        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
            modelName = getIntent().getStringExtra("modelName");
        if(!categoryId.isEmpty()&& categoryId != null)
        {
            loadListUnderCategory(categoryId);
        }

        toolbar.setTitle(modelName);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        //Search Functionality
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter Search Item");
        loadSuggestion();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //When user changes his text,  we will change the suggest list accordingly

                List<String> suggest = new ArrayList<String>();
                for (String search: suggestList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When search bar is close
                //Restore original suggest adapter
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //When search finish
                //Show result of search adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<ItemUnderCategory, CategoryItemViewHolder>(
                ItemUnderCategory.class,
                R.layout.under_category_item,
                CategoryItemViewHolder.class,
                categoryItemList.orderByChild("Name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(CategoryItemViewHolder viewHolder, ItemUnderCategory model, int position) {
                viewHolder.itemName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.itemImage);



                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                        Intent ItemDetail = new Intent(ListUnderCategory.this, com.example.madina.exploretashkent.ItemDetail.class);
                        ItemDetail.putExtra("ItemId", searchAdapter.getRef(position).getKey()); //Send Item Id to new activity
                        startActivity(ItemDetail);
                    }
                });

            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggestion() {
        categoryItemList.orderByChild("CategoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                        {
                            ItemUnderCategory item = postSnapshot.getValue(ItemUnderCategory.class);
                            suggestList.add(item.getName()); //Adding name of the item to the list of suggestions
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent favouriteActivity = new Intent(ListUnderCategory.this, Home.class);
            startActivity(favouriteActivity);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        else if (id == R.id.nav_favourite) {
            Intent favouriteActivity = new Intent(ListUnderCategory.this, FavouritesActivity.class);
            startActivity(favouriteActivity);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        else if (id == R.id.nav_log_out) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ListUnderCategory.this, MainActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadListUnderCategory(final String categoryId) {
        adapter = new FirebaseRecyclerAdapter<ItemUnderCategory, CategoryItemViewHolder>(ItemUnderCategory.class,
                R.layout.under_category_item,
                CategoryItemViewHolder.class,
                categoryItemList.orderByChild("CategoryId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(final CategoryItemViewHolder viewHolder, final ItemUnderCategory model, final int position) {
                viewHolder.itemName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.itemImage);

                final Favourites favourites = new Favourites();
                favourites.setItemId(adapter.getRef(position).getKey());
                favourites.setUserUid(user.getUid());
                favourites.setItemName(model.getName());
                favourites.setItemPhone(model.getPhone());
                favourites.setItemCategoryId(categoryId);
                favourites.setItemImage(model.getImage());


                //Add Favorites
                if(localDB.isFavourite(favourites.getItemId(), favourites.getUserUid()))
                    viewHolder.favImage.setImageResource(R.drawable.ic_favorite_black_24dp);

                //Click to change state of Favourites
                viewHolder.favImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {




                        if(!localDB.isFavourite(favourites.getItemId(), favourites.getUserUid()))
                        {
                            localDB.addToFavourites(favourites);
                            viewHolder.favImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(ListUnderCategory.this, ""+model.getName()+" was added to Favorites", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            localDB.removeFromFavourites(favourites.getItemId(), favourites.getUserUid());
                            viewHolder.favImage.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(ListUnderCategory.this, ""+model.getName()+" was removed from Favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                final ItemUnderCategory local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                        Intent ItemDetail = new Intent(ListUnderCategory.this, com.example.madina.exploretashkent.ItemDetail.class);
                        ItemDetail.putExtra("ItemId", adapter.getRef(position).getKey()); //Send Item Id to new activity
                        startActivity(ItemDetail);
                    }
                });
            }
        };

        //Set adapter
        recyclerView.setAdapter(adapter);
    }
}
