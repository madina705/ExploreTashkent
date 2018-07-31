package com.example.madina.exploretashkent.Models;

/**
 * Created by Madina on 1/5/2018.
 */

public class ItemUnderCategory {
    private String Name;
    private String Image;
    private String Description;
    private String ItemId;
    private String Phone;
    private String Latitude;
    private String Longitude;
    private String Address;


    public ItemUnderCategory() {
    }

    public ItemUnderCategory(String name, String image, String description, String itemId, String phone, String latitude, String longitude, String address, String hours) {
        Name = name;
        Image = image;
        Description = description;
        ItemId = itemId;
        Phone = phone;
        Latitude = latitude;
        Longitude = longitude;
        Address = address;
        Hours = hours;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getHours() {
        return Hours;
    }

    public void setHours(String hours) {
        Hours = hours;
    }

    private String Hours;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

}
