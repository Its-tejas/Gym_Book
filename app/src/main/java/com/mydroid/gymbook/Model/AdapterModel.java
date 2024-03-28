package com.mydroid.gymbook.Model;

public class AdapterModel {
    public int id;
    public String pic, name, phone, amount, date;

    public AdapterModel() {
    }

    public AdapterModel(int id, String name, String phone, String amount, String date) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.date = date;
    }

    public AdapterModel(int id, String pic, String name, String phone, String amount, String date) {
        this.id = id;
        this.pic = pic;
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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
