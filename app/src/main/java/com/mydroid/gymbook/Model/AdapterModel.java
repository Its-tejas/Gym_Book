package com.mydroid.gymbook.Model;

public class AdapterModel {
    public int id;
    public String pic_path, name, phone, amount, date, firebase_id;

    public AdapterModel() {
    }

    public AdapterModel(int id, String name, String phone, String amount, String date) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.date = date;
    }

    public AdapterModel(int id, String pic_path, String name, String phone, String amount, String date) {
        this.id = id;
        this.pic_path = pic_path;
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.date = date;
    }

    public AdapterModel(String pic_path, String name, String phone, String amount, String date) {
        this.pic_path = pic_path;
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.date = date;
    }

    public AdapterModel(int id, String pic_path, String name, String phone, String amount, String date, String firebase_id) {
        this.id = id;
        this.pic_path = pic_path;
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.date = date;
        this.firebase_id = firebase_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }
}
