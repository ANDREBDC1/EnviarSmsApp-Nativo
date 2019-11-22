package com.example.enviarsmsapp;

public class User {

    public User(String id, String tokem, String numberPhone) {
        this.id = id;
        this.tokem = tokem;
        this.numberPhone = numberPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokem() {
        return tokem;
    }

    public void setTokem(String tokem) {
        this.tokem = tokem;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    private String id;
    private String tokem;
    private String numberPhone;

}
