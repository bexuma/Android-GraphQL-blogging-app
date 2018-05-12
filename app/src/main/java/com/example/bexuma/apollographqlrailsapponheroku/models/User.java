package com.example.bexuma.apollographqlrailsapponheroku.models;

public class User {

    public static boolean isSignedIn() {
        return signedIn;
    }

    public static void setSignedIn(boolean signedIn) {
        User.signedIn = signedIn;
    }

    public static boolean signedIn = false;
    private static String token;
    private String name;
    private String email;

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

    public static String getToken() {
        return token;
    }

}
