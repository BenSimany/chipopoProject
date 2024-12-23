package com.example.chipopoproject.models;
public class DataModel {

    private String name;
    private String price;
    private String quantity;
    private int id_;

    public DataModel(String name, String price, int image, int id_) {
        this.name = name;
        this.price = price;
        this.id_ = id_;
        this.quantity = "0";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getId_() {
        return id_;
    }

}








