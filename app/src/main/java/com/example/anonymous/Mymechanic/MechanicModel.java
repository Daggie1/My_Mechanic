package com.example.anonymous.Mymechanic;


public class MechanicModel {

    public MechanicModel() {
    }

    public String id,number,name,email,serviceID,PhotoUrl;
    float rating;
    public MechanicModel(String id, String name, String email, String number,String Url, String serviceID,float rating) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.number = number;
        this.serviceID = serviceID;
        this.PhotoUrl=Url;
        this.rating=rating;
    }

    public String getId() {
        return id;
    }

    public String getMechname() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMechnumber() {
        return number;
    }

    public String getServiceID() {
        return serviceID;
    }

    public String getPickUrl() {
        return PhotoUrl;
    }
    public float getRating(){
        return rating;
    }
}

