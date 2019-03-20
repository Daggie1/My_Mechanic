package com.example.anonymous.Mymechanic;

public class UsersModel {
    String id,userName,email,picUrl,userNumber,passWord;

    public UsersModel() {
    }

    public UsersModel(String id, String userName, String email, String picUrl, String userNumber, String passWord) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.picUrl = picUrl;
        this.userNumber = userNumber;
        this.passWord = passWord;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public String getPassWord() {
        return passWord;
    }
}
