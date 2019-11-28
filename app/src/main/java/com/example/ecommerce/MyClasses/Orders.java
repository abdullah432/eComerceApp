package com.example.ecommerce.MyClasses;

public class Orders {
    private String name, address, contact, city, Price, State, Date, Time, userID;

    public Orders(){}

    public Orders(String name, String address, String contact, String city, String price, String state, String date, String time, String userID) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.city = city;
        Price = price;
        State = state;
        Date = date;
        Time = time;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUserID(){
        return userID;
    }

    public void setUserID(){
        this.userID = userID;
    }
}
