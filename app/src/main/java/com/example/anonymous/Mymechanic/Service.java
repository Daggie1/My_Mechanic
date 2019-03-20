package com.example.anonymous.Mymechanic;

public class Service  {

    private  String id,name,picUrl,priceRange;

    public Service() {
    }

    public Service(String id, String name, String picUrl, String priceRange) {
        this.id = id;
        this.name = name;
        this.picUrl = picUrl;
        this.priceRange = priceRange;
    }

    public Service(String id, String name, String picUrl) {
        this.id = id;
        this.name = name ;
        this.picUrl = picUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getPriceRange() {
        return priceRange;
    }
}
