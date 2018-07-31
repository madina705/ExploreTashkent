package com.example.madina.exploretashkent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;
    DatabaseReference users;
    CardView hotelCardView;
    CardView cateringCardView;
    CardView taxiCardView;
    CardView attractionsCardView;

    FirebaseAuth auth;

    TextView loggedUserEmail;
    TextView loggedUserName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        users = database.getReference("Users");
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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

        hotelCardView = (CardView) findViewById(R.id.hotelCardView);
        cateringCardView = (CardView) findViewById(R.id.cateringCardView);
        taxiCardView = (CardView) findViewById(R.id.taxiCardView);
        attractionsCardView = (CardView) findViewById(R.id.attractionsCardView);

        hotelCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listUnderCategory = new Intent(Home.this, ListUnderCategory.class);
                //We get key as Category Id is used as a key
                listUnderCategory.putExtra("CategoryId", category.child("Category").child("02").getKey());
                listUnderCategory.putExtra("modelName","HOTELS");
                startActivity(listUnderCategory);
            }
        });

        cateringCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listUnderCategory = new Intent(Home.this, ListUnderCategory.class);
                //We get key as Category Id is used as a key
                listUnderCategory.putExtra("CategoryId", category.child("Category").child("01").getKey());
                listUnderCategory.putExtra("modelName","CAFE AND RESTAURANTS");
                startActivity(listUnderCategory);
            }
        });



        taxiCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listUnderCategory = new Intent(Home.this, ListUnderCategory.class);
                //We get key as Category Id is used as a key
                listUnderCategory.putExtra("CategoryId", category.child("Category").child("04").getKey());
                listUnderCategory.putExtra("modelName","TAXI SERVICES");
                startActivity(listUnderCategory);
            }
        });



        attractionsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listUnderCategory = new Intent(Home.this, ListUnderCategory.class);
                //We get key as Category Id is used as a key
                listUnderCategory.putExtra("CategoryId", category.child("Category").child("03").getKey());
                listUnderCategory.putExtra("modelName","POPULAR ATTRACTIONS");
                startActivity(listUnderCategory);
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

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

        }
        else if (id == R.id.nav_favourite) {
            Intent favouriteActivity = new Intent(Home.this, FavouritesActivity.class);
            startActivity(favouriteActivity);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        else if (id == R.id.nav_log_out) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Home.this, MainActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
