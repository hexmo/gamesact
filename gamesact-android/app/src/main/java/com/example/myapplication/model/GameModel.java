package com.example.myapplication.model;

import java.io.Serializable;
import java.util.List;

public class GameModel implements Serializable {
    private String name;
    private String currency;
    private String cover;
    private List<String> prices;

    public GameModel() {
    }

    public GameModel(String name, String currency, String cover, List<String> prices) {
        this.name = name;
        this.currency = currency;
        this.cover = cover;
        this.prices = prices;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getPrices() {
        return prices;
    }

    public void setPrices(List<String> prices) {
        this.prices = prices;
    }
}
