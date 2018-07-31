package com.example.madina.exploretashkent.Models;

/**
 * Created by Madina on 4/1/2018.
 */

public class Favourites {
    private String ItemId;
    private String UserUid;
    private String ItemName;
    private String ItemPhone;
    private String ItemCategoryId;
    private String ItemImage;

    public Favourites() {
    }

    public Favourites(String itemId, String userUid, String itemName, String itemPhone, String itemCategoryId, String itemImage) {
        ItemId = itemId;
        UserUid = userUid;
        ItemName = itemName;
        ItemPhone = itemPhone;
        ItemCategoryId = itemCategoryId;
        ItemImage = itemImage;

    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemPhone() {
        return ItemPhone;
    }

    public void setItemPhone(String itemPhone) {
        ItemPhone = itemPhone;
    }

    public String getItemCategoryId() {
        return ItemCategoryId;
    }

    public void setItemCategoryId(String itemCategoryId) {
        ItemCategoryId = itemCategoryId;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getUserUid() {
        return UserUid;
    }

    public void setUserUid(String userUid) {
        UserUid = userUid;
    }

}
