package com.example.bexuma.apollographqlrailsapponheroku;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bexuma.apollographqlrailsapponheroku.models.User;

public class UserLocalStore {
    private static final String SP_NAME = "userDetails";
    private SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor editor = userLocalDatabase.edit();
        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putString("token", user.getToken());

        editor.apply();

    }

    public User getLoggedInUser() {
        String name = userLocalDatabase.getString("name", "");
        String email = userLocalDatabase.getString("email", "");
        String token = userLocalDatabase.getString("token", null);

        return new User(name, email, token);
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor editor = userLocalDatabase.edit();
        editor.putBoolean("loggedIn", loggedIn);
        editor.apply();
    }

    public void clearUserData() {
        SharedPreferences.Editor editor = userLocalDatabase.edit();
        editor.clear();
        editor.apply();
    }

    public boolean getUserLoggedIn() {
        return userLocalDatabase.getBoolean("loggedIn", false);
    }

    public String getUserToken() {
        return userLocalDatabase.getString("token", null);
    }
}
