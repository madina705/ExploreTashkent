package com.example.madina.exploretashkent.Models;

/**
 * Created by Madina on 2/8/2018.
 */

public class Rating {

    private String userPhone;
    private String itemId;
    private String rateValue;
    private String comment;

    public Rating() {
    }

    public Rating(String userPhone, String itemId, String rateValue, String comment) {
        this.userPhone = userPhone;
        this.itemId = itemId;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
