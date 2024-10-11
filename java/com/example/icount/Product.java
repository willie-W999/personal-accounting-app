package com.example.icount;

public class Product {
    private long id;
    private String date;
    private String  category ;
    private String item;
    private double pay;
    public Product() {
    }
    public Product(long id, String date,String item, double pay,String category) {
        this.id = id;
        this.date = date;
        this.item = item;
        this.pay = pay;
        this.category = category;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}


