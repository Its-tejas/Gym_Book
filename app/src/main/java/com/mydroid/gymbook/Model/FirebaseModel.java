package com.mydroid.gymbook.Model;

public class FirebaseModel {

    public String pic_path, name, phone, amount, date;

    public FirebaseModel(String pic_path, String name, String phone, String amount, String date) {
        this.pic_path = pic_path;
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.date = date;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
