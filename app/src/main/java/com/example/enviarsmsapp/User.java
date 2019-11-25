package com.example.enviarsmsapp;

public class User {

    public User(String id, String token, String numberPhone) {
        this.id = id;
        this.token = token;
        this.numberPhone = numberPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String tokem) {
        this.token = tokem;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    private String id;
    private String token;
    private String numberPhone;

}
