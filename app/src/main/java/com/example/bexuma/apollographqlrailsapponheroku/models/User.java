package com.example.bexuma.apollographqlrailsapponheroku.models;

public class User {

    private String name;
    private String email;
    private String token;

    public User(String name, String email, String token) {
        this.name = name;
        this.email = email;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

}
