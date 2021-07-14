package com.example.myapplication.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class PurchaseModel {
    private String buyersId;
    private String gameName;
    private String productName;
    private String nepaliPrice;
    private boolean orderCompleted;
    private String gamerTag;
    @ServerTimestamp
    private Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public PurchaseModel() {
    }

    public PurchaseModel(String buyersId, String gameName, String productName, String nepaliPrice, boolean orderCompleted, String gamerTag) {
        this.buyersId = buyersId;
        this.gameName = gameName;
        this.productName = productName;
        this.nepaliPrice = nepaliPrice;
        this.orderCompleted = orderCompleted;
        this.gamerTag = gamerTag;
        this.timestamp = new Date();
    }

    public String getBuyersId() {
        return buyersId;
    }

    public void setBuyersId(String buyersId) {
        this.buyersId = buyersId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getNepaliPrice() {
        return nepaliPrice;
    }

    public void setNepaliPrice(String nepaliPrice) {
        this.nepaliPrice = nepaliPrice;
    }

    public boolean isOrderCompleted() {
        return orderCompleted;
    }

    public void setOrderCompleted(boolean orderCompleted) {
        this.orderCompleted = orderCompleted;
    }

    public String getGamerTag() {
        return gamerTag;
    }

    public void setGamerTag(String gamerTag) {
        this.gamerTag = gamerTag;
    }
}
