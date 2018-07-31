package com.example.madina.exploretashkent;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.example.madina.exploretashkent.Models.ItemUnderCategory;
import com.example.madina.exploretashkent.Models.Rating;
import com.example.madina.exploretashkent.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class ItemDetail extends AppCompatActivity implements RatingDialogListener{

    WebView itemDescription;
    ImageView itemImage;
    TextView txtAddress, txtHours;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnFavorite, btnRating;
    RatingBar ratingBar;
    ImageButton btnCall, btnLocation;

    String itemId = "";
    String phone, desc, latitude, longitude, name, address, hours;


    FirebaseDatabase database;
    DatabaseReference items;
    DatabaseReference ratingTabel;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbarItem);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        Picasso.with(this).load(R.drawable.ic_arrow_back_black_24dp);

        //Firebase
        database = FirebaseDatabase.getInstance();
        items = database.getReference("UnderCategories");
        ratingTabel = database.getReference("Rating");
        users = database.getReference("Users");

        btnCall = (ImageButton) findViewById(R.id.btnCall);
        btnLocation = (ImageButton)findViewById(R.id.btnLocation);
        btnLocation = (ImageButton) findViewById(R.id.btnLocation);
        btnRating = (FloatingActionButton) findViewById(R.id.btnRating);
        itemDescription = (WebView)findViewById(R.id.item_description);
        txtAddress = (TextView)findViewById(R.id.address);
        txtHours = (TextView)findViewById(R.id.hours);
        itemImage = (ImageView)findViewById(R.id.item_image);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppbar);


        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingDialog();
            }
        });


        //Get Food Id from Internet
        if(getIntent() != null)
            itemId = getIntent().getStringExtra("ItemId");
        if(!itemId.isEmpty())
        {
            getItemDetail(itemId);
            getRatingItem(itemId);
        }



        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone));
                startActivity(intent);
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemDetail.this, MapsActivity.class);
                intent.putExtra("Latitude", latitude); //Send Latitude to new activity
                intent.putExtra("Longitude", longitude); //Send Longitude to new activity
                intent.putExtra("Name", name); //Send name to new activity
                startActivity(intent);
            }
        });

    }


    private void getRatingItem(String itemId) {

        Query itemRating = ratingTabel.orderByChild("itemId").equalTo(itemId);
        itemRating.addValueEventListener(new ValueEventListener() {
            int count=0, sum=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum+= Integer.parseInt(item.getRateValue());
                    count++;
                }
                if(count != 0)
                {
                    float average = sum / count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite Good", "Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(ItemDetail.this)
                .show();
    }

    private void getItemDetail(final String itemId) {
        items.child(itemId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ItemUnderCategory itemUnderCategory = dataSnapshot.getValue(ItemUnderCategory.class);

                //Set Image
                Picasso.with(getBaseContext()).load(itemUnderCategory.getImage())
                        .into(itemImage);

                collapsingToolbarLayout.setTitle(itemUnderCategory.getName());


                desc = "<html><body><p align=\"justify\">";
                desc+= itemUnderCategory.getDescription();
                desc+= "</p></body></html>";
                itemDescription.loadData(desc, "text/html", "utf-8");

                hours = itemUnderCategory.getHours();

                String hoursText = "<b>" + "Working hours: " + "</b> " + hours;
                txtHours.setText(Html.fromHtml(hoursText));

                phone = itemUnderCategory.getPhone();
                address = itemUnderCategory.getAddress();

                String addressText = "<b>" + "Address:" + "</b> " + address;
                txtAddress.setText(Html.fromHtml(addressText));

                name = itemUnderCategory.getName();
                latitude = itemUnderCategory.getLatitude();
                longitude = itemUnderCategory.getLongitude();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onPositiveButtonClicked(int value, String comments) {
        //Get Rating and upload to firebase database

       final String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final Rating rating = new Rating(currentUserUid,
                itemId,
                String.valueOf(value),
                comments);
        ratingTabel.child(currentUserUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(currentUserUid).exists())
                {
                    //Remove old value(not necessary function)
                    //ratingTabel.child(currentUserUid).removeValue();

                    //Update new value
                    ratingTabel.child(currentUserUid).setValue(rating);
                }
                else
                {
                    //Update new value
                    ratingTabel.child(currentUserUid).setValue(rating);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}
