package com.example.ecommerce.MyClasses;

public class Users {
    private String name,email,password,homeAddress,image;

    public Users(){

    }

    public Users(String name, String email, String password, String homeAddress, String image) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.homeAddress = homeAddress;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
