package com.example.madina.exploretashkent.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.madina.exploretashkent.Models.Favourites;
import com.example.madina.exploretashkent.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madina on 1/27/2018.
 */

public class Database extends SQLiteAssetHelper{

    private static final String DB_NAME = "ExploreTashkentDB.db";
    private static final int DB_VER = 1;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public Database(Context context){
        super(context, DB_NAME, null, DB_VER);
    }

    public void addToFavourites(Favourites item)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favourites(" +
                "ItemId, UserUid, ItemName, ItemPhone, ItemCategoryId, ItemImage)" +
                "VALUES('%s','%s','%s','%s','%s','%s');",
                item.getItemId(),
                user.getUid(),
                item.getItemName(),
                item.getItemPhone(),
                item.getItemCategoryId(),
                item.getItemImage());
        db.execSQL(query);
    }

    public void removeFromFavourites(String itemId, String userUid)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favourites WHERE ItemId ='%s' and UserUid='%s';", itemId, userUid);
        db.execSQL(query);
    }
    public boolean isFavourite(String itemId, String userUid)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favourites WHERE ItemId ='%s' and UserUid='%s';", itemId, userUid);

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public List<Favourites> getAllFavorites(String UserUid)
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        String[] sqlSelect = {"UserUid", "ItemId", "ItemName", "ItemPhone", "ItemCategoryId", "ItemImage"};
        String sqlTable ="Favourites";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, "UserUid=?", new String[]{UserUid}, null, null, null);

        final List<Favourites> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                result.add(new Favourites(
                        c.getString(c.getColumnIndex("ItemId")),
                        c.getString(c.getColumnIndex("UserUid")),
                        c.getString(c.getColumnIndex("ItemName")),
                        c.getString(c.getColumnIndex("ItemPhone")),
                        c.getString(c.getColumnIndex("ItemCategoryId")),
                        c.getString(c.getColumnIndex("ItemImage"))

                ));
            }while (c.moveToNext());
        }
        return result;
    }

}
