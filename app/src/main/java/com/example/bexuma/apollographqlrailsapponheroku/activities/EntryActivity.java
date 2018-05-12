package com.example.bexuma.apollographqlrailsapponheroku.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.UserLocalStore;
import com.example.bexuma.apollographqlrailsapponheroku.fragments.PostsListFragment;
import com.example.bexuma.apollographqlrailsapponheroku.fragments.SignInFragment;
import com.example.bexuma.apollographqlrailsapponheroku.fragments.SignUpFragment;

import static com.example.bexuma.apollographqlrailsapponheroku.activities.MainActivity.userLocalStore;

public class EntryActivity extends AppCompatActivity {

    public static final String TAG = "EntryActivity";

    public static EntryActivity getEntryActivity() {
        return entryActivity;
    }

    public static void setEntryActivity(EntryActivity entryActivity) {
        EntryActivity.entryActivity = entryActivity;
    }

    private static EntryActivity entryActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome to YD");
        setSupportActionBar(toolbar);

        EntryActivity.setEntryActivity(this);

        userLocalStore = new UserLocalStore(this);

    }

    private boolean authenticated() {
        return userLocalStore.getUserLoggedIn();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticated()) {
            Intent startMainActivity = new Intent(this, MainActivity.class);

            startMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(startMainActivity);
            finish();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragmentHolder = fragmentManager.findFragmentById(R.id.fragmentHolder);

            if (fragmentHolder == null) {
                fragmentHolder = new SignInFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.fragmentHolder, fragmentHolder)
                        .commit();
            }
        }
    }

    public void openSignUpFragment() {
        SignUpFragment signUpFragment = new SignUpFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHolder, signUpFragment)
                .commit();
    }

    public void openSignInFragment() {
        SignInFragment signInFragment = new SignInFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHolder, signInFragment)
                .commit();
    }

}
